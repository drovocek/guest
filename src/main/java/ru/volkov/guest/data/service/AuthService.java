package ru.volkov.guest.data.service;

import com.github.appreciated.app.layout.component.menu.left.items.LeftNavigationItem;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.stereotype.Service;
import ru.volkov.guest.MainAppLayout;
import ru.volkov.guest.data.entity.Role;
import ru.volkov.guest.data.entity.User;
import ru.volkov.guest.data.service.user.UserRepository;
import ru.volkov.guest.util.exception.AuthException;
import ru.volkov.guest.util.exception.NotFoundException;
import ru.volkov.guest.view.admin.company.CompaniesView;
import ru.volkov.guest.view.admin.pass.PassesView;
import ru.volkov.guest.view.getpass.GetPassView;
import ru.volkov.guest.view.login.LogInView;
import ru.volkov.guest.view.login.LogOutView;
import ru.volkov.guest.view.meet.MeetView;
import ru.volkov.guest.view.settings.SettingsView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.vaadin.flow.component.icon.VaadinIcon.*;

@Service
public class AuthService {

    private final UserRepository repository;
    private final Map<String, AuthRout> routs = new HashMap<>();

    public AuthService(UserRepository repository) {
        this.repository = repository;
        fillRoutsMap();
    }

    private void fillRoutsMap() {
        routs.put("GET_PASS", new AuthRout("get-pass", "Get Pass", GetPassView.class, GOLF.create()));
        routs.put("MEET", new AuthRout("meet", "Meet", MeetView.class, CHECK_SQUARE_O.create()));
        routs.put("COMPANIES", new AuthRout("companies", "Companies", CompaniesView.class, PIGGY_BANK_COIN.create()));
        routs.put("USERS", new AuthRout("passes", "Passes", PassesView.class, USER_CARD.create()));
        routs.put("PASSES", new AuthRout("passes", "Passes", PassesView.class, USER_CARD.create()));
        routs.put("LOG_OUT", new AuthRout("logout", "LogOut", LogOutView.class, ARROW_LEFT.create()));
        routs.put("LOG_IN", new AuthRout("login", "LogIn", LogInView.class, USER.create()));
        routs.put("SETTINGS", new AuthRout("settings", "Settings", SettingsView.class, COG.create()));
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
        getAuthRoutes(role).forEach(rts ->
                RouteConfiguration.forSessionScope().setRoute(rts.route, rts.view, MainAppLayout.class));
    }

    public List<AuthRout> getAuthRoutes(Role role) {
        return switch (role) {
            case OWNER -> List.of(
                    routs.get("GET_PASS"), routs.get("MEET"), routs.get("COMPANIES"),
                    routs.get("USERS"), routs.get("PASSES"), routs.get("SETTINGS"));
            case COMPANY -> List.of(
                    routs.get("GET_PASS"), routs.get("USERS"), routs.get("PASSES"),
                    routs.get("SETTINGS"));
            case USER -> List.of(
                    routs.get("GET_PASS"), routs.get("PASSES"), routs.get("SETTINGS"));
            case GUARD -> List.of(
                    routs.get("MEET"), routs.get("PASSES"), routs.get("SETTINGS"));
        };
    }

    public Optional<User> getAuthUser() {
        return Optional.ofNullable(VaadinSession.getCurrent().getAttribute(User.class));
    }

    public Optional<List<AuthRout>> getAuthUserRoutes() {
        return getAuthUser().map(authUser -> getAuthRoutes(authUser.getRole()));
    }

    public record AuthRout(String route, String name, Class<? extends Component> view, Component icon) {
    }

    public static LeftNavigationItem asNavItem(AuthRout route) {
        return new LeftNavigationItem(route.name, route.icon, route.view);
    }

    public static LeftNavigationItem[] asNavItems(List<AuthRout> routes) {
        return routes.stream().map(AuthService::asNavItem).toArray(LeftNavigationItem[]::new);
    }

//    @Getter
//    public enum Routes {
//        GET_PASS("get-pass", "Get Pass", GetPassView.class, GOLF.create()),
//        MEET("meet", "Meet", MeetView.class, CHECK_SQUARE_O.create()),
//        COMPANIES("companies", "Companies", CompaniesView.class, PIGGY_BANK_COIN.create()),
//        USERS("users", "Users", UsersView.class, VaadinIcon.USERS.create()),
//        PASSES("passes", "Passes", PassesView.class, USER_CARD.create()),
//        LOG_OUT("logout", "LogOut", LogOutView.class, ARROW_LEFT.create()),
//        LOG_IN("login", "LogIn", LogInView.class, USER.create()),
//        SETTINGS("settings", "Settings", SettingsView.class, COG.create());
//
//        private final String route;
//        private final String name;
//        private final Class<? extends Component> view;
//        private final Component icon;
//
//        Routes(String route, String name, Class<? extends Component> view, Component icon) {
//            this.route = route;
//            this.name = name;
//            this.view = view;
//            this.icon = icon;
//        }
//
//        public static LeftNavigationItem asNavItem(Routes route) {
//            return new LeftNavigationItem(route.getName(), route.getIcon(), route.getView());
//        }
//
//        public static LeftNavigationItem[] asNavItems(List<Routes> routes) {
//            return routes.stream().map(Routes::asNavItem).toArray(LeftNavigationItem[]::new);
//        }
//    }
}
