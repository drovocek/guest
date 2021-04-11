package ru.volkov.guest.view.user;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.vaadin.erik.SlideMode;
import org.vaadin.erik.SlideTab;
import org.vaadin.erik.SlideTabBuilder;
import org.vaadin.erik.SlideTabPosition;
import org.vaadin.textfieldformatter.CustomStringBlockFormatter;
import ru.volkov.guest.component.FillCloseOpenForm;
import ru.volkov.guest.data.entity.Role;
import ru.volkov.guest.data.entity.User;
import ru.volkov.guest.data.service.AuthService;
import ru.volkov.guest.data.service.MailService;
import ru.volkov.guest.data.service.user.UserService;
import ru.volkov.guest.util.exception.NotFoundException;
import ru.volkov.guest.view.RootView;

import static ru.volkov.guest.data.entity.Role.*;
import static ru.volkov.guest.util.ConfigHelper.getDefNotify;

@UIScope
@RequiredArgsConstructor
@SpringComponent
public class UserForm extends Composite<SlideTab> implements BeforeLeaveObserver, FillCloseOpenForm<User> {

    private final static String PROD_ROOT = "https://getpass.herokuapp.com/";
    private final static String TEST_ROOT = "http://localhost:8080/";

    private final ComboBox<Role> role = new ComboBox<>("Role");
    private final TextField fullName = new TextField("Full name");
    private final TextField phone = new TextField("Phone");
    private final EmailField email = new EmailField("Email");

    private final Button confirm = new Button("Add");
    private final Button clear = new Button("Clear");
    private final HorizontalLayout buttonsGroup = new HorizontalLayout(confirm, clear);

    private final Div formContainer = new Div(role, fullName, phone, email, buttonsGroup);
    private final SlideTab formTab = new SlideTabBuilder(formContainer, "Form")
            .mode(SlideMode.BOTTOM)
            .autoCollapseSlider(false)
            .tabPosition(SlideTabPosition.BEGINNING)
            .build();


    private final AuthService authService;
    private final UserService userService;
    private final MailService mailService;

    private final BeanValidationBinder<User> binder = new BeanValidationBinder<>(User.class);
    private User user = new User();

    @Override
    protected SlideTab initContent() {
        initButtons();
        configFields();
        initBinder();
        configStyles();
        return formTab;
    }

    @Override
    protected <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return super.addListener(eventType, listener);
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent beforeLeaveEvent) {
        clearAndClose();
    }

    private void configStyles() {
        formContainer.setClassName("form");
        confirm.addClassName("saveButton");
        clear.addClassName("clearButton");
    }

    private void configFields() {
        role.setRequired(true);
        role.setItems(COMPANY, GUARD, EMPLOYEE);
        role.setValue(COMPANY);

        fullName.setPlaceholder("User full name");
        fullName.setClearButtonVisible(true);
        fullName.setMinLength(3);
        fullName.setMaxLength(40);
        fullName.setRequired(true);

        email.setPlaceholder("user@email.com");
        email.setClearButtonVisible(true);
        email.setMinLength(3);
        email.setMaxLength(40);
        email.setRequiredIndicatorVisible(true);

        phone.setClearButtonVisible(true);
        phone.setMinLength(18);
        phone.setMaxLength(18);
        phone.setRequired(true);
        new CustomStringBlockFormatter.Builder()
                .blocks(0, 3, 3, 2, 2)
                .delimiters("(", ") ", "-", "-")
                .prefix("+7", true, " ")
                .numeric()
                .build().extend(phone);
    }

    private void initBinder() {
        binder.forField(fullName)
                .withValidator(name -> name.length() > 3, "Name size must be more then 5")
                .bind("fullName");
        binder.forField(email)
                .withValidator(new EmailValidator("Enter a valid email address"))
                .bind("email");
        binder.forField(phone)
                .withValidator(phone -> phone.length() == 18, "Enter a valid phone number")
                .bind("phone");
        binder.bindInstanceFields(this);
    }

    private void initButtons() {
        clear.addClickListener(event -> clear());
        confirm.addClickListener(event -> {
            if (binder.writeBeanIfValid(this.user)) {
                save();
                clearAndClose();
                fireEvent(new UserSaveEvent(this, false));
            }
        });
    }

    private void clear() {
        this.user = new User();
        binder.readBean(user);
        role.setValue(COMPANY);

        confirm.setText("Add");
        confirm.removeClassName("updateButton");
        confirm.addClassName("saveButton");
    }

    public void fillAndOpen(User user) {
        System.out.println("bean: " + user);
        this.user = user;
        binder.readBean(user);

        confirm.setText("Update");
        confirm.removeClassName("saveButton");
        confirm.addClassName("updateButton");

        open();
    }

    public void save() {
        authService.getAuthUser().ifPresent(authUser -> {
            if (user.getId() == null) {
                String userName = user.getEmail().substring(0, user.getEmail().indexOf("@"));
                user.setUserName(userName);
                user.setRootId(authUser.getId());

                user.setActivationCode(RandomStringUtils.randomAlphanumeric(32));
                String activationUrl =
//                TEST_ROOT
                        PROD_ROOT
                                .concat("registration?code=")
                                .concat(user.getActivationCode());
                mailService.sendMessage(
                        user.getEmail(),
                        activationUrl,
                        "Confirmation email");
                getDefNotify("User created").addThemeName("success");
                showInRoot("User created", "Pass updated");
            } else if (user.getRootId() == null || user.getRootId().equals(authUser.getId())) {
                getDefNotify("User updated").addThemeName("success");
                showInRoot("Success updated", "Pass updated");
            } else {
                throw new NotFoundException("You have not User with some id");
            }
            userService.update(user);
        });
    }

    public void showInRoot(String title, String description) {
        UI.getCurrent().getChildren()
                .filter(component -> component.getClass() == RootView.class)
                .findFirst().ifPresent(root -> ((RootView) root).setNotify(title, description));
    }

    public void open() {
        if (!formTab.isExpanded()) {
            formTab.toggle();
        }
    }

    public void clearAndClose() {
        if (formTab.isExpanded()) {
            formTab.toggle();
            clear();
        }
    }
}
