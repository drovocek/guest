package ru.volkov.guest.view.admin.user;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.data.provider.QuerySortOrderBuilder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.vaadin.artur.helpers.CrudServiceDataProvider;
import ru.volkov.guest.MainAppLayout;
import ru.volkov.guest.data.entity.User;
import ru.volkov.guest.data.service.user.UserService;

@JsModule("./views/admin/user/user-view.js")
@CssImport("./views/admin/user/user-view.css")
@Tag("user-view")
@PageTitle("User")
@Route(value = "user", layout = MainAppLayout.class)
public class UserView extends PolymerTemplate<TemplateModel> {

    @Id("grid")
    private Grid<User> grid;

    private final UserService service;

    public UserView(UserService service) {
        this.service = service;

        grid.addColumn(User::getRole)
                .setHeader("Role")
                .setSortProperty("role");
        grid.addColumn(User::getCompany)
                .setHeader("Company")
                .setSortProperty("companyName");
        grid.addColumn(User::getName)
                .setHeader("Name")
                .setSortProperty("name");
        grid.addColumn(User::getEmail)
                .setHeader("Email")
                .setSortProperty("email");
        grid.addColumn(User::getPhone)
                .setHeader("Phone")
                .setSortProperty("phone");
        grid.addColumn(User::getPassCount)
                .setHeader("Pass count")
                .setSortProperty("passCount");
        grid.addColumn(User::getLastActivity)
                .setHeader("Last pass date")
                .setSortProperty("lastActivity");
        grid.addColumn(User::getRegDate)
                .setHeader("Registration date")
                .setSortProperty("regDate");

        CrudServiceDataProvider<User, Integer> dataProvider = new CrudServiceDataProvider<>(service);
        dataProvider.setSortOrders(
                new QuerySortOrderBuilder()
                        .thenDesc("regDate")
//                        .thenAsc("company")
                        .build());

        grid.setDataProvider(dataProvider);
    }
}
