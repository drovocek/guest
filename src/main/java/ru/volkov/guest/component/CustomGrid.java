package ru.volkov.guest.component;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.provider.DataProvider;
import lombok.RequiredArgsConstructor;
import ru.volkov.guest.data.AbstractEntity;
import ru.volkov.guest.data.service.GridAndFormService;
import ru.volkov.guest.view.RootView;

import java.util.Optional;

import static ru.volkov.guest.util.ConfigHelper.addGridStyles;

@RequiredArgsConstructor
public class CustomGrid<T> extends Grid<T> {

    private final FillCloseOpenForm<T> formTab;
    private final GridAndFormService<T> service;

    public void initBaseConfig() {
        addGridListeners(this, service);
        addGridStyles(this);
        addDataProvider(this, service);
        addGridConfig(this);
    }

    public Icon getSquareIconByStatus(boolean status) {
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

    private void addGridListeners(Grid<T> grid, GridAndFormService<T> service) {
        grid.asSingleSelect().addValueChangeListener(event ->
                Optional.ofNullable(event.getValue())
                        .ifPresentOrElse(
                                bean -> formTab.fillAndOpen(service.getById(((AbstractEntity) bean).getId())),
                                formTab::clearAndClose
                        ));
    }

    private void addDataProvider(Grid<T> grid, GridAndFormService<T> service) {
        grid.setDataProvider(createDataProvider(service));
    }

    private DataProvider<T, Void> createDataProvider(GridAndFormService<T> service) {
        return DataProvider.fromCallbacks(
                query -> service.getSortedPage(query.getOffset(), query.getLimit(), query.getSortOrders()),
                query -> service.getCount());
    }

    private void addGridConfig(Grid<T> grid) {
        grid.setMultiSort(true);
    }

    public void refreshGrid() {
        this.select(null);
        this.getDataProvider().refreshAll();
    }

    public void showInRoot(String title, String description) {
        UI.getCurrent().getChildren()
                .filter(component -> component.getClass() == RootView.class)
                .findFirst().ifPresent(root -> ((RootView) root).setNotify(title, description));
    }
}
