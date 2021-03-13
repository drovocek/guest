package ru.volkov.guest.view.login;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.login.AbstractLogin;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import ru.volkov.guest.data.service.AuthService;

@Tag("login-view")
@PageTitle("Login")
@RouteAlias(value = "")
@Route(value = "login")
public class LoginView extends VerticalLayout {

    private final AuthService authService;

    //    https://vaadin.com/components/vaadin-login/java-examples
    public LoginView(AuthService authService) {
        this.authService = authService;
        LoginOverlay component = new LoginOverlay();
        component.addLoginListener(e -> component.close());
        component.setOpened(true);
        component.setTitle("Guest");
        component.setDescription("App for get pass");

        component.addLoginListener(e -> {
            if (authenticate(e)) {
                navigateToMainPage();
            } else {
                component.setError(true);
            }
        });

        //component.setEnabled(true)

        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setAdditionalInformation("To close the login form submit non-empty username and password");
        component.setI18n(i18n);

        add(component);
    }

    private void navigateToMainPage() {
        getUI().ifPresent(ui -> ui.navigate("get-pass"));
    }

    private boolean authenticate(AbstractLogin.LoginEvent e) {
        return authService.authenticate(e.getUsername(), e.getPassword());
    }
}
