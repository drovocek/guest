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
import ru.volkov.guest.view.getpass.GetPassView;
import ru.volkov.guest.view.settings.SettingsView;

@Slf4j
@Tag("login-view")
@PageTitle("LogIn")
//@RouteAlias(value = "")
@Route(value = "login")
public class LogInView extends VerticalLayout {//implements BeforeEnterObserver {

    private final AuthService authService;
    private final LoginOverlay login;
//
//    @Override
//    public void beforeEnter(BeforeEnterEvent event) {
//        log.info("\nbeforeEnter1");
//        if (authService.getAuthUser().isPresent()) {
//            log.info("\nbeforeEnter2");
//            login.close();
//            navigateToMainPage();
//        }
//    }

    //    https://vaadin.com/components/vaadin-login/java-examples
    public LogInView(AuthService authService) {
        this.authService = authService;
        this.login = new LoginOverlay();
//        login.addLoginListener(e -> login.close());
        login.setOpened(true);
        login.setTitle("Guest");
        login.setDescription("App for get pass");

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

        //component.setEnabled(true)

        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setAdditionalInformation("To close the login form submit non-empty username and password");
        login.setI18n(i18n);
        add(login);
    }

    private void navigateToMainPage() {
        getUI().ifPresent(ui -> ui.navigate(SettingsView.class));
//        authService.getAuthUserRoutes().ifPresent(routes ->
//                getUI().ifPresent(ui -> ui.navigate(routes.get(0).getRoute())));
    }

    private void authenticate(AbstractLogin.LoginEvent e) {
        authService.authenticate(e.getUsername(), e.getPassword());
    }
}
