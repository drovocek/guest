package ru.volkov.guest.view.admin.user;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
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
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.apache.commons.lang3.StringUtils;
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
import static ru.volkov.guest.util.ConfigHelper.addColumns;
import static ru.volkov.guest.util.ConfigHelper.addGridStyles;

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

    private final AuthService authService;
    private final UserService userService;

    public UsersView(UserService userService, AuthService authService) {
        this.authService = authService;
        this.userService = userService;

        addGridColumns(grid);
        addGridStyles(grid);
        addGridListeners(grid);
        grid.setDataProvider(new CrudServiceDataProvider<>(userService));
        addFormBinder();
        configFormComponents();
    }

    private void addFormBinder() {
        binder = new BeanValidationBinder<>(User.class);
        binder.forField(fullName)
                .withValidator(name -> name.length() > 3, "Name size must be more then 5")
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
        role.setItems(COMPANY, GUARD, EMPLOYEE);
        role.setValue(COMPANY);

        phone.setValueChangeMode(ValueChangeMode.EAGER);

        PrefixUtil.setPrefixComponent(phone, new Span("+7 "));

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
            user.setEnabled(!user.getEnabled());
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
        role.setValue(COMPANY);
    }

    private void populateForm(User user) {
        Optional.ofNullable(user).ifPresent(us ->
                us.setPhone(us.getPhone().substring(3)));
        binder.readBean(user);
    }

    private void addGridColumns(Grid<User> grid) {
        grid.addComponentColumn((user) ->
                createIconComponentByBoolean(user.getEnabled(), () -> changeEnabled(user.getId())))
                .setHeader("Enabled")
                .setSortProperty("enabled");
        addColumns(grid, User.class);
    }

    private void addGridListeners(Grid<User> grid) {
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                Optional<User> user = userService.get(event.getValue().getId());
                if (user.isPresent()) {
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

    private Notification getDefNotify(String message) {
        return Notification
                .show(message, 2000, BOTTOM_START);
    }

    @EventHandler
    public void clear() {
        clearForm();
        refreshGrid();
        role.setValue(COMPANY);
    }

    @EventHandler
    public void save() {
        User user = new User();
        if (binder.writeBeanIfValid(user)) {
            authService.getAuthUser().ifPresent(authUser -> {
                if (user.getRootId() == null || user.getRootId().equals(authUser.getId())) {
                    user.setRootId(authUser.getId());
                    user.setPhone("+7 ".concat(user.getPhone()));
                    userService.update(user);
                    clearForm();
                    refreshGrid();
                    getDefNotify("User created").addThemeName("success");
                } else {
                    getDefNotify("You is not root, can't update not yours, choose another").addThemeName("error");
                }
            });
        }
    }
}
