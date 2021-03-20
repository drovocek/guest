package ru.volkov.guest.view.login;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.login.AbstractLogin;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import lombok.extern.slf4j.Slf4j;
import ru.volkov.guest.data.service.AuthService;
import ru.volkov.guest.util.exception.AuthException;
import ru.volkov.guest.util.exception.NotFoundException;
import ru.volkov.guest.util.exception.NotYetImplementedException;

@Slf4j
@Tag("login-view")
@PageTitle("LogIn")
@RouteAlias(value = "")
@Route(value = "login")
public class LogInView extends VerticalLayout implements BeforeEnterObserver {

    private final AuthService authService;
    private final LoginOverlay login;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (authService.getAuthUser().isPresent()) {
            login.close();
            authService.getAuthUserRoutes().ifPresent(routes ->
                    event.forwardTo(routes.get(0).getPath()));
        }
    }

    //    https://vaadin.com/components/vaadin-login/java-examples
    public LogInView(AuthService authService) {
        this.authService = authService;
        this.login = new LoginOverlay();
//        login.addLoginListener(e -> login.close());
        login.setOpened(true);
        login.setTitle("Guest");
        login.setDescription("App for get pass. For test use " +
                " username/password: \n" +
                "- owner/owner, \n" +
                "- company/company, \n" +
                "- employee/employee, \n" +
                "- guard/guard;");

        login.addLoginListener(e -> {
            try {
                authenticate(e);
                login.close();
                navigateToMainPage();
            } catch (AuthException | NotFoundException ex) {
                login.setError(true);
                throw ex;
            }
        });

        login.addForgotPasswordListener(e -> {
            throw new NotYetImplementedException();
        });

        //component.setEnabled(true)

        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setAdditionalInformation("To close the login form submit non-empty username and password");
        login.setI18n(i18n);
        add(login);
    }

    private void navigateToMainPage() {
        authService.getAuthUserRoutes().ifPresent(routes ->
                getUI().ifPresent(ui -> ui.navigate(routes.get(0).getView())));
    }

    private void authenticate(AbstractLogin.LoginEvent e) {
        authService.authenticate(e.getUsername(), e.getPassword());
    }
}
