package ru.volkov.guest.view.statistics;

import com.github.appreciated.css.grid.GridLayoutComponent;
import com.github.appreciated.css.grid.sizes.Length;
import com.github.appreciated.css.grid.sizes.MinMax;
import com.github.appreciated.css.grid.sizes.Repeat;
import com.github.appreciated.layout.FlexibleGridLayout;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.vaadin.artur.helpers.CrudServiceDataProvider;
import ru.volkov.guest.MainAppLayout;
import ru.volkov.guest.data.entity.CarPass;
import ru.volkov.guest.data.service.CarPassService;

@JsModule("./views/statistics/statistics-view.js")
@CssImport("./views/statistics/statistics-view.css")
@Tag("statistics-view")
@PageTitle("Statistics")
@Route(value = "statistics", layout = MainAppLayout.class)
public class StatisticsView extends PolymerTemplate<TemplateModel> {

    @Id("grid")
    private Grid<CarPass> grid;

    public StatisticsView(CarPassService service) {
        grid.addColumn(CarPass::getRegNum)
                .setHeader("Registration Number")
                .setSortProperty("regNum");
        grid.addColumn(CarPass::getCompanyName)
                .setHeader("Company")
                .setSortProperty("companyName");
        grid.addColumn(CarPass::getArrivalDate)
                .setHeader("Arrival Date")
                .setSortProperty("arrivalDate");
        grid.addColumn(CarPass::isPassed)
                .setHeader("Passed")
                .setSortProperty("passed");
        grid.setDataProvider(new CrudServiceDataProvider<>(service));

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
    }
}
