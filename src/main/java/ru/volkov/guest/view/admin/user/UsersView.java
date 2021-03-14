package ru.volkov.guest.view.admin.user;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.artur.helpers.CrudService;
import org.vaadin.artur.helpers.CrudServiceDataProvider;
import ru.volkov.guest.data.entity.Role;
import ru.volkov.guest.data.entity.User;
import ru.volkov.guest.data.service.AuthService;
import ru.volkov.guest.data.service.user.UserService;
import ru.volkov.guest.util.exception.PrefixUtil;

import java.util.Arrays;
import java.util.Optional;

import static com.vaadin.flow.component.notification.Notification.Position.BOTTOM_START;
import static ru.volkov.guest.data.entity.Role.*;

@JsModule("./views/admin/user/user-view.js")
@CssImport("./views/admin/user/user-view.css")
@Tag("user-view")
@PageTitle("User")
public class UsersView extends PolymerTemplate<TemplateModel> {

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

    private BeanValidationBinder<User> binder;
    private User user;

    private final AuthService authService;
    private final UserService userService;

    public UsersView(UserService userService, AuthService authService) {
        this.authService = authService;
        this.userService = userService;

        addGridColumns(grid);
        addGridStyles(grid);
        addGridListeners(grid);
        addGridDataProvider(grid, userService);
        addFormBinder();
        configFormComponents();
    }

    private void addFormBinder() {
        binder = new BeanValidationBinder<>(User.class);
        binder.forField(fullName)
                .withValidator(name -> name.length() > 5, "Name size must be more then 5")
                .bind("fullName");
        binder.forField(email)
                .withValidator(new EmailValidator("Enter a valid email address"))
                .bind("email");
        binder.forField(phone)
                .withValidator(phone -> phone.length() == 15, "Enter a valid phone number")
                .bind("phone");
        binder.bindInstanceFields(this);
    }

    private void configFormComponents() {
        enabled.setValue(true);

        role.setRequired(true);
        role.setItems(COMPANY, GUARD, EMPLOYEE);
        role.setValue(COMPANY);

        fullName.setValueChangeMode(ValueChangeMode.EAGER);
        fullName.setRequiredIndicatorVisible(true);
        fullName.setRequired(true);
        fullName.setClearButtonVisible(true);
        fullName.setPlaceholder("User full name");
        fullName.setMinLength(5);
        fullName.setMaxLength(40);

        email.setValueChangeMode(ValueChangeMode.EAGER);
        email.setRequiredIndicatorVisible(true);
        email.setClearButtonVisible(true);
        email.setPlaceholder("user@email.com");
        email.setMaxLength(30);

        phone.setRequiredIndicatorVisible(true);
        phone.setValueChangeMode(ValueChangeMode.EAGER);
        PrefixUtil.setPrefixComponent(phone, new Span("+7 "));
        phone.setMaxLength(15);
        phone.setClearButtonVisible(true);
//        phone.setPattern("[0-9.,]*");
//        phone.setPreventInvalidInput(true);
        phone.addValueChangeListener(event ->
                Optional.ofNullable(event.getValue()).ifPresent(value -> {
                    if (!event.getValue().contains(event.getOldValue())) {
                        phone.setValue("");
                    } else if (StringUtils.isNumericSpace(value.substring(value.length() - 1))) {
                        if (value.length() == 1) {
                            phone.setValue("(".concat(value));
                        } else if (value.length() == 4) {
                            phone.setValue(value.concat(") "));
                        } else if (value.length() == 9) {
                            phone.setValue(value.concat(" "));
                        } else if (value.length() == 12) {
                            phone.setValue(value.concat(" "));
                        }
                    } else {
                        phone.setValue(value.substring(0, value.length() - 1));
                    }
                })
        );
    }

    private void changeEnabled(Integer id) {
        userService.get(id).filter(user -> {
            user.setEnabled(!user.isEnabled());
            userService.update(user);
            refreshGrid();
            getDefNotify("Status updated").addThemeName("success");
            return true;
        }).orElseThrow(() -> new NotFoundException("User not found"));
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(User value) {
        this.user = value;
        binder.readBean(this.user);
    }

    private void addGridColumns(Grid<User> grid) {
        grid.addComponentColumn((user) ->
                createIconComponentByBoolean(
                        user.isEnabled(), () -> changeEnabled(user.getId())))
                .setHeader("Enabled")
                .setSortProperty("enabled");
        grid.addColumn(User::getRole)
                .setHeader("Role")
                .setSortProperty("role");
        grid.addColumn(User::getRootName)
                .setHeader("Root creator")
                .setSortProperty("rootName");
        grid.addColumn(User::getFullName)
                .setHeader("Full name")
                .setSortProperty("fullName");
        grid.addColumn(User::getEmail)
                .setHeader("Email")
                .setSortProperty("email");
        grid.addColumn(User::getPhone)
                .setHeader("Phone")
                .setSortProperty("phone");
        grid.addColumn(User::getChildrenCount)
                .setHeader("Number of child users")
                .setSortProperty("childrenCount");
        grid.addColumn(User::getPassCount)
                .setHeader("Pass count")
                .setSortProperty("passCount");
        grid.addColumn(User::getLastActivity)
                .setHeader("Last activity")
                .setSortProperty("lastActivity");
        grid.addColumn(User::getRegDate)
                .setHeader("Registration date")
                .setSortProperty("regDate");
        grid.addColumn(User::getUserName)
                .setHeader("User name")
                .setSortProperty("userName");
    }

    private void addGridStyles(Grid<User> grid) {
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();
    }

    private void addGridListeners(Grid<User> grid) {
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                Optional<User> user = userService.get(event.getValue().getId());
                if (user.isPresent()) {
                    System.out.println(user);
                    populateForm(user.get());
                } else {
                    refreshGrid();
                    throw new NotFoundException("User not found");
                }
            } else {
                clearForm();
            }
        });
    }

    private void addGridDataProvider(Grid<User> grid, CrudService<User, Integer> service) {
        grid.setDataProvider(new CrudServiceDataProvider<>(service));
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
        role.setValue(COMPANY);
    }

    @EventHandler
    public void save() {
        try {
            if (binder.isValid()) {
                if (this.user == null) {
                    this.user = new User();
                }
                binder.writeBean(this.user);
                authService.getAuthUser().ifPresent(authUser -> {
                    if (user.getRootId() == null || user.getRootId().equals(authUser.getId())) {
                        this.user.setRootId(authUser.getId());
                        this.user.setPhone("+7 ".concat(user.getPhone()));
                        userService.update(this.user);
                        clearForm();
                        refreshGrid();
                        getDefNotify("User created").addThemeName("success");
                    } else {
                        getDefNotify("You is not root, can't update not yours, choose another").addThemeName("error");
                    }
                });
            }
        } catch (ValidationException validationException) {
            getDefNotify("Save aborted").addThemeName("error");
        }
    }

    private Notification getDefNotify(String message) {
        return Notification
                .show(message, 2000, BOTTOM_START);
    }
}
