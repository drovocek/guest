package ru.volkov.guest.view.admin.company;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.data.provider.QuerySortOrderBuilder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.vaadin.artur.helpers.CrudServiceDataProvider;
import ru.volkov.guest.data.entity.Company;
import ru.volkov.guest.data.service.company.CompanyService;

@JsModule("./views/admin/company/company-view.js")
@CssImport("./views/admin/company/company-view.css")
@Tag("company-view")
@PageTitle("Company")
public class CompaniesView extends PolymerTemplate<TemplateModel> {

    @Id("grid")
    private Grid<Company> grid;

    private final CompanyService service;

    public CompaniesView(CompanyService service) {
        this.service = service;

        grid.addColumn(Company::getName)
                .setHeader("Name")
                .setSortProperty("name");
        grid.addColumn(Company::getEmail)
                .setHeader("Email")
                .setSortProperty("email");
        grid.addColumn(Company::getPhone)
                .setHeader("Phone")
                .setSortProperty("phone");
        grid.addColumn(Company::getPassword)
                .setHeader("Password")
                .setSortProperty("password");
        grid.addColumn(Company::getRegDate)
                .setHeader("Registration Date")
                .setSortProperty("regDate");

        CrudServiceDataProvider<Company, Integer> dataProvider = new CrudServiceDataProvider<>(service);
        dataProvider.setSortOrders(
                new QuerySortOrderBuilder()
                        .thenDesc("regDate")
//                        .thenAsc("name")
                        .build());

        grid.setDataProvider(dataProvider);
    }
}
