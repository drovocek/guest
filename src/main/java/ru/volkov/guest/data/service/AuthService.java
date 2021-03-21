package ru.volkov.guest.data.service;

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
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import ru.volkov.guest.MainAppLayout;
import ru.volkov.guest.data.entity.Role;
import ru.volkov.guest.data.entity.User;
import ru.volkov.guest.data.service.user.UserService;
import ru.volkov.guest.util.exception.AuthException;
import ru.volkov.guest.view.admin.pass.PassesView;
import ru.volkov.guest.view.admin.user.UsersView;
import ru.volkov.guest.view.getpass.GetPassView;
import ru.volkov.guest.view.login.LogInView;
import ru.volkov.guest.view.login.LogOutView;
import ru.volkov.guest.view.meet.MeetView;
import ru.volkov.guest.view.settings.SettingsView;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.vaadin.flow.component.icon.VaadinIcon.*;
import static ru.volkov.guest.data.service.AuthService.Routes.USERS;
import static ru.volkov.guest.data.service.AuthService.Routes.*;

@UIScope
@Service
public class AuthService {

    private final UserService service;

    public AuthService(UserService service) {
        this.service = service;
    }

    public void authenticate(String userName, String password) {
        User user = service.getByUserName(userName);
        if (user.getPasswordHash() == null) {
            UI.getCurrent().navigate("registration",
                    new QueryParameters(
                            Collections.singletonMap("code", Collections.singletonList(user.getActivationCode()))));
        } else if (user.isValidPassword(password) && user.getEnabled()) {
            VaadinSession.getCurrent().setAttribute(User.class, user);
            createRoutes(user.getRole());
        } else {
            throw new AuthException("Password incorrect");
        }
    }

    private void createRoutes(Role role) {
        getAuthRoutes(role).forEach(route ->
                RouteConfiguration.forSessionScope()
                        .setRoute(route.path, route.view, MainAppLayout.class));
    }

    public List<Routes> getAuthRoutes(Role role) {
        return switch (role) {
            case OWNER -> List.of(GET_PASS, MEET, USERS, PASSES, SETTINGS, LOG_OUT);
            case COMPANY -> List.of(GET_PASS, USERS, PASSES, SETTINGS, LOG_OUT);
            case EMPLOYEE -> List.of(GET_PASS, PASSES, SETTINGS, LOG_OUT);
            case GUARD -> List.of(MEET, PASSES, SETTINGS, LOG_OUT);
        };
    }

    public Optional<User> getAuthUser() {
        return Optional.ofNullable(VaadinSession.getCurrent().getAttribute(User.class));
    }

    public Optional<List<Routes>> getAuthUserRoutes() {
        return getAuthUser().map(authUser -> getAuthRoutes(authUser.getRole()));
    }

    public void activate(String password, String activationCode) {
        User user = service.getByActivationCode(activationCode);
        updatePassword(user, password);
    }

    public void refreshPassword(String password) {
        getAuthUser().ifPresent(authUser -> updatePassword(authUser, password));
    }

    public boolean isActivated(String code) {
        return service.isActivated(code);
    }

    public void updatePassword(User user, String password) {
        String passwordSalt = RandomStringUtils.random(32);
        String passwordHash = DigestUtils.sha1Hex(password.concat(passwordSalt));
        user.setPasswordSalt(passwordSalt);
        user.setPasswordHash(passwordHash);
        user.setEnabled(true);
        service.update(user);
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
    }
}
