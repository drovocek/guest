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
import com.vaadin.flow.templatemodel.TemplateModel;
import lombok.RequiredArgsConstructor;
import org.vaadin.artur.helpers.CrudServiceDataProvider;
import ru.volkov.guest.data.entity.CarPass;
import ru.volkov.guest.data.service.carpass.CarPassService;

import javax.annotation.PostConstruct;
import java.util.Optional;

import static ru.volkov.guest.util.ConfigHelper.addColumns;

@RequiredArgsConstructor
@JsModule("./views/admin/pass/pass-view.js")
@CssImport("./views/admin/pass/pass-view.css")
@Tag("pass-view")
@PageTitle("Pass")
public class PassesView extends PolymerTemplate<TemplateModel> {

    @Id("grid")
    private Grid<CarPass> grid;

    private final CarPassService carPassService;
    private final PassesFormView formTab;

    @PostConstruct
    public void initView() {
        initGrid();
        getElement().appendChild(formTab.getElement());
        formTab.addListener(PassesSaveEvent.class, event -> refreshGrid());
    }

    private void initGrid() {
        addGridColumns();
        addGridListeners();
        addDataProvider();
    }

    private void addGridColumns() {
        grid.addComponentColumn((carPass) -> getSquareIconByStatus(carPass.isPassed()))
                .setHeader("Passed")
                .setSortProperty("passed");

        addColumns(grid, CarPass.class);
    }

    private Icon getSquareIconByStatus(boolean status) {
        Icon icon;
        if (status) {
            icon = VaadinIcon.CHECK_SQUARE.create();
            icon.setColor("green");
        } else {
            icon = VaadinIcon.SQUARE_SHADOW.create();
            icon.setColor("orange");
        }
        return icon;
    }

    private void addGridListeners() {
        grid.asSingleSelect().addValueChangeListener(event ->
                Optional.ofNullable(event.getValue())
                        .ifPresentOrElse(
                                bean -> formTab.fillAndOpen(carPassService.getById(bean.getId())),
                                formTab::clearAndClose
                        ));
    }

    private void addDataProvider() {
        CrudServiceDataProvider<CarPass, Integer> dataProvider = new CrudServiceDataProvider<>(carPassService);
        dataProvider.setSortOrders(
                new QuerySortOrderBuilder()
                        .thenAsc("arrivalDate")
                        .thenDesc("regDataTime")
                        .build());

        grid.setDataProvider(dataProvider);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }
}
