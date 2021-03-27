package ru.volkov.guest.view.admin.user;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.templatemodel.TemplateModel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.vaadin.artur.helpers.CrudServiceDataProvider;
import org.vaadin.textfieldformatter.CustomStringBlockFormatter.Builder;
import ru.volkov.guest.data.entity.Role;
import ru.volkov.guest.data.entity.User;
import ru.volkov.guest.data.service.AuthService;
import ru.volkov.guest.data.service.MailService;
import ru.volkov.guest.data.service.user.UserService;
import ru.volkov.guest.view.RootView;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Optional;

import static ru.volkov.guest.data.entity.Role.*;
import static ru.volkov.guest.util.ConfigHelper.*;

@RequiredArgsConstructor
@JsModule("./views/admin/user/user-view.js")
@CssImport("./views/admin/user/user-view.css")
@Tag("user-view")
@PageTitle("User")
public class UsersView extends PolymerTemplate<TemplateModel> {

    private final static String PROD_ROOT = "https://getpass.herokuapp.com/";
    private final static String TEST_ROOT = "http://localhost:8080/";

    @Id("role")
    private ComboBox<Role> role;
    @Id("phone")
    private TextField phone;
    @Id("fullName")
    private TextField fullName;
    @Id("email")
    private EmailField email;
    @Id("enabled")
    private Checkbox enabled;

    @Id("grid")
    private Grid<User> grid;

    private final AuthService authService;
    private final UserService userService;
    private final MailService mailService;

    private final BeanValidationBinder<User> binder = new BeanValidationBinder<>(User.class);
    private User user = new User();

    @PostConstruct
    private void initView() {
        initGrid();
        initForm();
    }

    private void initGrid() {
        addGridColumns(grid);
        addGridListeners(grid);
        addGridStyles(grid);
        grid.setDataProvider(new CrudServiceDataProvider<>(userService));
    }

    private void initForm() {
        addFormBinder();
        configFormComponents();
    }

    private void addGridColumns(Grid<User> grid) {
        grid.addComponentColumn((user) ->
                createIconComponentByBoolean(user.getEnabled(), () -> changeEnabled(user.getId())))
                .setHeader("Enabled")
                .setSortProperty("enabled");

        addColumns(grid, User.class);

        grid.addColumn(new LocalDateTimeRenderer<>(User::getLastActivity))
                .setHeader("Last activity");
    }

    private void addGridListeners(Grid<User> grid) {
        grid.asSingleSelect().addValueChangeListener(event ->
                Optional.ofNullable(event.getValue())
                        .ifPresentOrElse(
                                bean -> {
                                    System.out.println("bean: " + bean);
                                    this.user = userService.getById(bean.getId());
                                    System.out.println("user: " + bean);
                                    populateForm(user);
                                },
                                this::clearForm
                        ));
    }

    private void addFormBinder() {
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

    private void configFormComponents() {
        role.setItems(COMPANY, GUARD, EMPLOYEE);
        role.setValue(COMPANY);

        new Builder()
                .blocks(0, 3, 3, 2, 2)
                .delimiters("(", ") ", "-", "-")
                .prefix("+7", true, " ")
                .numeric()
                .build().extend(phone);
    }

    private void changeEnabled(Integer id) {
        userService.get(id).filter(user -> {
            user.setEnabled(!user.getEnabled());
            userService.update(user);
            refreshGrid();
            getDefNotify("Status updated").addThemeName("success");
            showInRoot("Status updated", "Status updated");
            return true;
        }).orElseThrow(() -> new NotFoundException("User not found"));
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        this.user = new User();
        populateForm(null);
        role.setValue(COMPANY);
    }

    private void populateForm(User user) {
        binder.readBean(user);
    }

    private Icon createIconComponentByBoolean(boolean bool, Runnable... clickAction) {
        Icon icon;
        if (bool) {
            icon = VaadinIcon.CHECK_SQUARE.create();
            icon.setColor("green");
        } else {
            icon = VaadinIcon.SQUARE_SHADOW.create();
            icon.setColor("orange");
        }
        if (clickAction.length > 0) {
            Arrays.stream(clickAction).forEach(clickAct -> icon.addClickListener(event -> clickAct.run()));
        }
        return icon;
    }

    @EventHandler
    public void clear() {
        clearForm();
        refreshGrid();
    }

    @EventHandler
    public void save() {
        if (binder.writeBeanIfValid(this.user)) {
            authService.getAuthUser().ifPresent(authUser -> {
                if (user.getId() == null) {
                    create(user, authUser.getId());
                } else {
                    update(user, authUser.getId());
                }
                clearForm();
                refreshGrid();
            });
        }
    }

    private void create(User user, Integer rootId) {
        String userName = user.getEmail().substring(0, user.getEmail().indexOf("@"));
        user.setUserName(userName);
        user.setRootId(rootId);

        user.setActivationCode(RandomStringUtils.randomAlphanumeric(32));
        String activationUrl =
                TEST_ROOT
//                PROD_ROOT
                        .concat("registration?code=")
                        .concat(user.getActivationCode());
        mailService.sendMessage(
                user.getEmail(),
                activationUrl,
                "Confirmation email");

        userService.update(user);
        getDefNotify("User created").addThemeName("success");
        showInRoot("User created", "User updated");
    }

    private void update(User user, Integer rootId) {
        if (user.getRootId() == null || user.getRootId().equals(rootId)) {
            userService.update(user);
            getDefNotify("User updated").addThemeName("success");
            showInRoot("Success updated", "User updated");
        } else {
            getDefNotify("You is not root, can't update not yours, choose another").addThemeName("error");
            showInRoot("Update aborted", "You is not root, can't update not yours, choose another");
        }
    }

    public void showInRoot(String title, String description) {
        UI.getCurrent().getChildren()
                .filter(component -> component.getClass() == RootView.class)
                .findFirst().ifPresent(root -> ((RootView) root).setNotify(title, description));
    }
}
