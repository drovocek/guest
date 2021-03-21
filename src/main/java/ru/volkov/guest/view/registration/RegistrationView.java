package ru.volkov.guest.view.registration;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.volkov.guest.data.service.AuthService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static ru.volkov.guest.util.ConfigHelper.getDefNotify;

@Slf4j
@JsModule("./views/registration/registration-view.js")
@CssImport("./views/registration/registration-view.css")
@Tag("registration-view")
@Route("registration")
@PageTitle("Registration")
public class RegistrationView extends PolymerTemplate<TemplateModel> implements BeforeEnterObserver {

    @Id("enterPass")
    private PasswordField enterPass;
    @Id("confirmPass")
    private PasswordField confirmPass;

    @Id("enter")
    private Button enter;
    @Id("clear")
    private Button clear;

    private final AuthService authService;
    private BeanValidationBinder<Password> binder;
    private String code;

    @Getter
    @Setter
    public class Password {
        private String enterPass;
        private String confirmPass;
    }

    public RegistrationView(AuthService authService) {
        this.authService = authService;
        initForm();
    }

    private void initForm() {
        binder = new BeanValidationBinder<>(Password.class);

        binder.forField(enterPass)
                .withValidator((p) -> enterPass.getValue().equals(confirmPass.getValue()),
                        "Passwords must be equals")
                .bind("enterPass");

        binder.forField(confirmPass)
                .withValidator((p) -> enterPass.getValue().equals(confirmPass.getValue()),
                        "Passwords must be equals")
                .bind("confirmPass");

        binder.bindInstanceFields(this);
    }

    @EventHandler
    private void save() {
        Password password = new Password();
        if (binder.writeBeanIfValid(password)) {
            authService.activate(password.getEnterPass(), code);
            getDefNotify("You are registered");
            clearForm();
            navigateToMainPage();
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Map<String, List<String>> parameters = event.getLocation().getQueryParameters().getParameters();
        Optional.ofNullable(parameters.get("code")).ifPresent(paramList -> {
            String code = paramList.get(0);
            if (code != null && !authService.isActivated(code)) {
                this.code = code;
            } else {
                event.forwardTo("login");
            }
        });
        if (!parameters.containsKey("code")) {
            event.forwardTo("login");
        }
    }

    @EventHandler
    private void clearForm() {
        binder.setBean(new Password());
    }

    private void navigateToMainPage() {
        getUI().ifPresent(ui -> ui.navigate("login"));
    }
}