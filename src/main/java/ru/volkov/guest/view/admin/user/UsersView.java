package ru.volkov.guest.view.admin.user;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.vaadin.artur.helpers.CrudServiceDataProvider;
import ru.volkov.guest.data.entity.User;
import ru.volkov.guest.data.service.user.UserService;

import java.util.Optional;

@JsModule("./views/admin/user/user-view.js")
@CssImport("./views/admin/user/user-view.css")
@Tag("user-view")
@PageTitle("User")
public class UsersView extends PolymerTemplate<TemplateModel> {

    @Id("name")
    private TextField name;
    @Id("email")
    private EmailField email;
    @Id("phone")
    private TextField phone;
    @Id("enter")
    private Button enter;
    @Id("clear")
    private Button clear;
    @Id("grid")
    private Grid<User> grid;

    private BeanValidationBinder<User> binder;
    private User user;

    public UsersView(UserService service) {

        grid.addColumn(User::getRole)
                .setHeader("Role")
                .setSortProperty("role")
                .setAutoWidth(true);
        grid.addColumn(User::getCompany)
                .setHeader("Company")
                .setSortProperty("companyName")
                .setAutoWidth(true);
        grid.addColumn(User::getName)
                .setHeader("Name")
                .setSortProperty("name")
                .setAutoWidth(true);
        grid.addColumn(User::getEmail)
                .setHeader("Email")
                .setSortProperty("email")
                .setAutoWidth(true);
        grid.addColumn(User::getPhone)
                .setHeader("Phone")
                .setSortProperty("phone")
                .setAutoWidth(true);
        grid.addColumn(User::getPassCount)
                .setHeader("Pass count")
                .setSortProperty("passCount")
                .setAutoWidth(true);
        grid.addColumn(User::getLastActivity)
                .setHeader("Last pass date")
                .setSortProperty("lastActivity")
                .setAutoWidth(true);
        grid.addColumn(User::getRegDate)
                .setHeader("Registration date")
                .setSortProperty("regDate")
                .setAutoWidth(true);

        grid.setDataProvider(new CrudServiceDataProvider<>(service));
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                Optional<User> user = service.get(event.getValue().getId());
                // when a row is selected but the data is no longer available, refresh grid
                if (user.isPresent()) {
                    populateForm(user.get());
                } else {
                    refreshGrid();
                }
            } else {
                clearForm();
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(User.class);

        // Bind fields. This where you'd define e.g. validation rules
//        binder.forField(user).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
//                .bind("customer");

        binder.bindInstanceFields(this);

        clear.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        enter.addClickListener(e -> {
            try {
                if (this.user == null) {
                    this.user = new User();
                }
                binder.writeBean(this.user);

                service.update(this.user);
                clearForm();
                refreshGrid();
                Notification.show("CarPass details stored.");
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the carPass details.");
            }
        });
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
}
