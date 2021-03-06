package ru.volkov.guest.view.admin.pass;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.data.provider.QuerySortOrderBuilder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.vaadin.artur.helpers.CrudServiceDataProvider;
import ru.volkov.guest.MainAppLayout;
import ru.volkov.guest.data.entity.CarPass;
import ru.volkov.guest.data.service.carpass.CarPassService;

@JsModule("./views/admin/pass/pass-view.js")
@CssImport("./views/admin/pass/pass-view.css")
@Tag("pass-view")
@PageTitle("Pass")
@Route(value = "pass", layout = MainAppLayout.class)
public class PassView extends PolymerTemplate<TemplateModel> {

    @Id("grid")
    private Grid<CarPass> grid;

    private final CarPassService service;

    public PassView(CarPassService service) {
        this.service = service;

        grid.addComponentColumn((carPass) -> {
            Icon icon;
            if (carPass.isPassed()) {
                icon = VaadinIcon.CHECK_SQUARE.create();
                icon.setColor("green");
            } else {
                icon = VaadinIcon.SQUARE_SHADOW.create();
                icon.setColor("orange");
            }
            return icon;
        })
                .setHeader("Passed")
                .setSortProperty("passed");
        grid.addColumn(CarPass::getRegNum)
                .setHeader("Registration Number")
                .setSortProperty("regNum");
        grid.addColumn(CarPass::getCompanyName)
                .setHeader("Company")
                .setSortProperty("companyName");
        grid.addColumn(CarPass::getArrivalDate)
                .setHeader("Arrival Date")
                .setSortProperty("arrivalDate");

        CrudServiceDataProvider<CarPass, Integer> dataProvider = new CrudServiceDataProvider<>(service);
        dataProvider.setSortOrders(
                new QuerySortOrderBuilder()
                        .thenAsc("arrivalDate")
                        .thenDesc("regDataTime")
                        .build());

        grid.setDataProvider(dataProvider);
    }
}
