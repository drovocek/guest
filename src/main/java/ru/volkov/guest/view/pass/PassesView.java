package ru.volkov.guest.view.pass;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.templatemodel.TemplateModel;
import lombok.RequiredArgsConstructor;
import ru.volkov.guest.data.entity.CarPass;
import ru.volkov.guest.data.service.carpass.CarPassService;

import javax.annotation.PostConstruct;
import java.util.Optional;

import static ru.volkov.guest.util.ConfigHelper.addColumns;
import static ru.volkov.guest.util.ConfigHelper.addGridStyles;

@RequiredArgsConstructor
@JsModule("./views/pass/pass-view.js")
@CssImport("./views/pass/pass-view.css")
@Tag("pass-view")
@PageTitle("Pass")
public class PassesView extends PolymerTemplate<TemplateModel> {

    @Id("grid")
    private Grid<CarPass> grid;
    @Id("rootLayout")
    private VerticalLayout rootLayout;

    private final CarPassService carPassService;
    private final PassesFormView formTab;

    @PostConstruct
    public void initView() {
        initGrid();
        formTab.addListener(PassesSaveEvent.class, event -> refreshGrid());
        rootLayout.add(formTab);
    }

    private void initGrid() {
        addGridColumns();
        addGridListeners();
        addDataProvider();
        addStyles();
        addGridConfig();
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
        grid.setDataProvider(createCarPassDataProvider(carPassService));
    }

    public DataProvider<CarPass, Void> createCarPassDataProvider(CarPassService carPassService) {
        return DataProvider.fromCallbacks(
                query -> carPassService.getSortedPage(query.getOffset(), query.getLimit(), query.getSortOrders()),
                query -> carPassService.getCount());
    }

    private void addStyles() {
        addGridStyles(grid);
    }

    private void addGridConfig() {
        grid.setMultiSort(true);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }
}
