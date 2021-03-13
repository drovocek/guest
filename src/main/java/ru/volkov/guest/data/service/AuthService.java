package ru.volkov.guest.data.service;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;
import lombok.Getter;
import org.springframework.stereotype.Service;
import ru.volkov.guest.MainAppLayout;
import ru.volkov.guest.data.entity.Role;
import ru.volkov.guest.data.entity.User;
import ru.volkov.guest.data.service.user.UserRepository;
import ru.volkov.guest.util.exception.AuthException;
import ru.volkov.guest.util.exception.NotFoundException;
import ru.volkov.guest.view.admin.company.CompaniesView;
import ru.volkov.guest.view.admin.pass.PassesView;
import ru.volkov.guest.view.admin.user.UsersView;
import ru.volkov.guest.view.getpass.GetPassView;
import ru.volkov.guest.view.login.LogInView;
import ru.volkov.guest.view.login.LogOutView;
import ru.volkov.guest.view.meet.MeetView;
import ru.volkov.guest.view.settings.SettingsView;

import java.util.List;
import java.util.Optional;

import static com.vaadin.flow.component.icon.VaadinIcon.*;
import static ru.volkov.guest.data.service.AuthService.Routes.USERS;
import static ru.volkov.guest.data.service.AuthService.Routes.*;

@Service
public class AuthService {

    private final UserRepository repository;

    public AuthService(UserRepository repository) {
        this.repository = repository;
    }

    public void authenticate(String name, String password) {
        User user = repository.findUsersByName(name);
        if (user == null) {
            throw new NotFoundException("No such user");
        } else if (!user.checkPassword(password)) {
            throw new AuthException("Password incorrect");
        } else {
            VaadinSession.getCurrent().setAttribute(User.class, user);
            createRoutes(user.getRole());
        }
    }

    private void createRoutes(Role role) {
        getAuthRoutes(role).forEach(route ->
                RouteConfiguration.forSessionScope()
                        .setRoute(route.path, route.view, MainAppLayout.class));
    }

    public List<Routes> getAuthRoutes(Role role) {
        return switch (role) {
            case OWNER -> List.of(GET_PASS, MEET, COMPANIES, USERS, PASSES, SETTINGS, LOG_OUT);
            case COMPANY -> List.of(GET_PASS, USERS, PASSES, SETTINGS, LOG_OUT);
            case USER -> List.of(GET_PASS, PASSES, SETTINGS, LOG_OUT);
            case GUARD -> List.of(MEET, PASSES, SETTINGS, LOG_OUT);
        };
    }

    public Optional<User> getAuthUser() {
        return Optional.ofNullable(VaadinSession.getCurrent().getAttribute(User.class));
    }

    public Optional<List<Routes>> getAuthUserRoutes() {
        return getAuthUser().map(authUser -> getAuthRoutes(authUser.getRole()));
    }

    @Getter
    public enum Routes {
        GET_PASS("get-pass", "Get Pass", GetPassView.class, GOLF.create()),
        MEET("meet", "Meet", MeetView.class, CHECK_SQUARE_O.create()),
        COMPANIES("companies", "Companies", CompaniesView.class, PIGGY_BANK_COIN.create()),
        USERS("users", "Users", UsersView.class, VaadinIcon.USERS.create()),
        PASSES("passes", "Passes", PassesView.class, USER_CARD.create()),
        LOG_OUT("logout", "LogOut", LogOutView.class, ARROW_LEFT.create()),
        LOG_IN("login", "LogIn", LogInView.class, USER.create()),
        SETTINGS("settings", "Settings", SettingsView.class, COG.create());

        private final String path;
        private final String name;
        private final Class<? extends Component> view;
        private final Component icon;

        Routes(String path, String name, Class<? extends Component> view, Component icon) {
            this.path = path;
            this.name = name;
            this.view = view;
            this.icon = icon;
        }

        private static Tab asTab(Routes route) {
            final Tab tab = new Tab();
            tab.add(new RouterLink(route.path, route.view));
            ComponentUtil.setData(tab, Class.class, route.view);
            return tab;
        }

        public static Tab[] asTabs(List<Routes> routes) {
            return routes.stream().map(Routes::asTab).toArray(Tab[]::new);
        }
    }
}
