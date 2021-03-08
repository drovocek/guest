package ru.volkov.guest.view.registration;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import ru.volkov.guest.MainAppLayout;
import ru.volkov.guest.data.entity.Company;
import ru.volkov.guest.data.service.company.CompanyService;

@JsModule("./views/registration/registration-view.js")
@CssImport("./views/registration/registration-view.css")
@Tag("registration-view")
@PageTitle("Registration")
@Route(value = "registration", layout = MainAppLayout.class)
public class RegistrationView extends PolymerTemplate<TemplateModel> {

    @Id("name")
    private TextField name;
    @Id("email")
    private EmailField email;
    @Id("phone")
    private TextField phone;
    @Id("enterPass")
    private PasswordField enterPass;
    @Id("confirmPass")
    private PasswordField confirmPass;

    @Id("enter")
    private Button enter;
    @Id("clear")
    private Button clear;

    private final Binder<Company> binder = new Binder(Company.class);
    private final CompanyService service;

    public RegistrationView(CompanyService service) {
        this.service = service;

        binder.setBean(new Company());
        binder.forField(name)
                .withValidator(new StringLengthValidator(
                        "Please add the company name", 1, null))
                .bind(Company::getName, Company::setName);

        binder.forField(email)
                .withValidator((e) -> !email.getValue().trim().isEmpty(),
                        "Email cannot be empty")
                .withValidator(new EmailValidator("Incorrect email address"))
                .bind(Company::getEmail, Company::setEmail);

        binder.forField(phone)
                .withValidator((p) -> !phone.getValue().trim().isEmpty(),
                        "Phone cannot be empty")
                .bind(Company::getPhone, Company::setPhone);

        binder.forField(enterPass)
                .withValidator((p) -> !enterPass.getValue().trim().isEmpty(),
                        "Password cannot be empty")
                .withValidator((p) -> enterPass.getValue().equals(confirmPass.getValue()),
                        "Password must be equals")
                .bind(Company::getPassword, Company::setPassword);

        binder.forField(confirmPass)
                .withValidator((p) -> !confirmPass.getValue().trim().isEmpty(),
                        "Password cannot be empty")
                .withValidator((p) -> enterPass.getValue().equals(confirmPass.getValue()),
                        "Password must be equals")
                .bind(Company::getPassword, Company::setPassword);

        name.setRequiredIndicatorVisible(true);
        email.setRequiredIndicatorVisible(true);
        phone.setRequiredIndicatorVisible(true);
        enterPass.setRequiredIndicatorVisible(true);
        confirmPass.setRequiredIndicatorVisible(true);
    }

    @EventHandler
    private void save() {
        if (binder.validate().isOk()) {
            service.update(binder.getBean());
            Notification.show(
                    String.format("Company %s registered", name.getValue()))
                    .addThemeName("success");
            clearForm();
            navigateToMainPage();
        }
    }

    @EventHandler
    private void clearForm() {
        binder.setBean(new Company());
    }

    private void navigateToMainPage() {
        getUI().ifPresent(ui -> ui.navigate("company"));
    }
}