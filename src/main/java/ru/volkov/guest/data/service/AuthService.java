package ru.volkov.guest.data.service;

import com.github.appreciated.app.layout.component.menu.left.items.LeftNavigationItem;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import ru.volkov.guest.data.entity.Role;
import ru.volkov.guest.data.entity.User;
import ru.volkov.guest.data.service.user.UserService;
import ru.volkov.guest.util.exception.AuthException;
import ru.volkov.guest.view.RootView;
import ru.volkov.guest.view.pass.PassesView;
import ru.volkov.guest.view.user.UsersView;
import ru.volkov.guest.view.getpass.GetPassView;
import ru.volkov.guest.view.login.LogInView;
import ru.volkov.guest.view.login.LogOutView;
import ru.volkov.guest.view.meet.MeetView;
import ru.volkov.guest.view.settings.SettingsView;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.vaadin.flow.component.icon.VaadinIcon.*;
import static ru.volkov.guest.data.service.AuthService.Routes.USERS;
import static ru.volkov.guest.data.service.AuthService.Routes.*;

@UIScope
@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserService userService;
    private final MailService mailService;

    public void authenticate(String userName, String password) {
        User user = userService.getByUserName(userName);
        if (user.getPasswordHash() == null) {
            UI.getCurrent().navigate("registration",
                    new QueryParameters(
                            Collections.singletonMap("code", Collections.singletonList(user.getActivationCode()))));
        } else if (user.isValidPassword(password) && user.getEnabled()) {
            VaadinSession.getCurrent().setAttribute(User.class, user);
            createRoutes(user.getRole());
            user.setLastActivity(LocalDateTime.now());
            userService.update(user);
        } else {
            throw new AuthException("Password incorrect");
        }
    }

    private void createRoutes(Role role) {
        getAuthRoutes(role).forEach(route ->
                RouteConfiguration.forSessionScope()
                        .setRoute(route.path, route.view, RootView.class));
    }

    public List<Routes> getAuthRoutes(Role role) {
        return switch (role) {
            case OWNER -> List.of(USERS, PASSES, MEET, SETTINGS, LOG_OUT); //GET_PASS
            case COMPANY -> List.of(USERS, PASSES, SETTINGS, LOG_OUT); //GET_PASS
            case EMPLOYEE -> List.of(PASSES, SETTINGS, LOG_OUT); //GET_PASS
            case GUARD -> List.of(MEET, PASSES, SETTINGS, LOG_OUT); //GET_PASS
        };
    }

    public Optional<User> getAuthUser() {
        return Optional.ofNullable(VaadinSession.getCurrent().getAttribute(User.class));
    }

    public Optional<List<Routes>> getAuthUserRoutes() {
        return getAuthUser().map(authUser -> getAuthRoutes(authUser.getRole()));
    }

    public void activate(String password, String activationCode) {
        User user = userService.getByActivationCode(activationCode);
        updatePassword(user, password);
        mailService.sendMessage(
                user.getEmail(),
                String.format("Login: %s \nPassword: %s", user.getUserName(), password),
                "Registration info");
    }

    public void refreshPassword(String password) {
        getAuthUser().ifPresent(authUser -> updatePassword(authUser, password));
    }

    public boolean isActivated(String code) {
        return userService.isActivated(code);
    }

    public void updatePassword(User user, String password) {
        String passwordSalt = RandomStringUtils.random(32);
        String passwordHash = DigestUtils.sha1Hex(password.concat(passwordSalt));
        user.setPasswordSalt(passwordSalt);
        user.setPasswordHash(passwordHash);
        user.setEnabled(true);
        userService.update(user);
    }

    @Getter
    public enum Routes {
        GET_PASS("get-pass", "Get Pass", GetPassView.class, GOLF),
        MEET("meet", "Meet", MeetView.class, CHECK_SQUARE_O),
        USERS("users", "Users", UsersView.class, VaadinIcon.USERS),
        PASSES("passes", "Passes", PassesView.class, USER_CARD),
        LOG_OUT("logout", "LogOut", LogOutView.class, ARROW_LEFT),
        LOG_IN("login", "LogIn", LogInView.class, USER),
        SETTINGS("settings", "Settings", SettingsView.class, COG);

        private final String path;
        private final String name;
        private final Class<? extends Component> view;
        private final VaadinIcon icon;

        Routes(String path, String name, Class<? extends Component> view, VaadinIcon icon) {
            this.path = path;
            this.name = name;
            this.view = view;
            this.icon = icon;
        }

        private static Tab asTab(Routes route) {
            final Tab tab = new Tab();
            tab.add(route.icon.create());
            tab.add(new RouterLink(route.path, route.view));
            ComponentUtil.setData(tab, Class.class, route.view);
            return tab;
        }

        public static Tab[] asTabs(List<Routes> routes) {
            return routes.stream().map(Routes::asTab).toArray(Tab[]::new);
        }

        public static LeftNavigationItem asLeftNavigationItem(Routes route) {
            return new LeftNavigationItem(route.name, route.icon.create(), route.view);
        }

        public static LeftNavigationItem[] asLeftNavigationItems(List<Routes> routes) {
            return routes.stream().map(Routes::asLeftNavigationItem).toArray(LeftNavigationItem[]::new);
        }
    }
}
