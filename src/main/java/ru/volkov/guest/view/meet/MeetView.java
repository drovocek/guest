package ru.volkov.guest.view.meet;

import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import lombok.extern.slf4j.Slf4j;
import ru.volkov.guest.MainAppLayout;
import ru.volkov.guest.data.entity.CarPass;
import ru.volkov.guest.data.service.CarPassService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@JsModule("./views/meet/meet-view.js")
@CssImport(value = "./views/meet/meet-view.css")
@CssImport(
        themeFor = "vaadin-grid",
        value = "./views/meet/cell-color.css"
)
@Tag("meet-view")
@Route(value = "meet", layout = MainAppLayout.class)
@PageTitle("Meet")
public class MeetView extends PolymerTemplate<TemplateModel> {

    @Id("companyName")
    private ComboBox<CarPass> companyName;
    @Id("arrivalDate")
    private DatePicker arrivalDate;
    @Id("grid")
    private Grid<CarPass> grid;

    public MeetView(CarPassService service) {
        grid.addColumn(CarPass::getRegNum)
                .setHeader("Registration Number")
                .setAutoWidth(true)
                .setSortProperty("regNum");
        grid.addColumn(CarPass::getCompanyName)
                .setHeader("Company")
                .setAutoWidth(true)
                .setSortProperty("companyName");
        grid.addColumn(CarPass::isPassed)
                .setHeader("Passed")
                .setAutoWidth(true)
                .setSortProperty("passed")
                .setClassNameGenerator(carPass -> !carPass.isPassed() ? "green" : "orange");
        ;
//        grid.setDataProvider(new CrudServiceDataProvider<>(service));

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        arrivalDate.setMin(LocalDate.now());
        arrivalDate.setValue(LocalDate.now());

        companyName.setClearButtonVisible(true);
        companyName.setItemLabelGenerator((ItemLabelGenerator<CarPass>) CarPass::getCompanyName);

        arrivalDate.addValueChangeListener(event -> {
            List<CarPass> all = service.getAllSortedByDate(event.getValue());
            companyName.setItems(all);
            grid.setItems(all);
        });

        companyName.addValueChangeListener(event -> {
            List<CarPass> all = service.getAllSortedByDate(arrivalDate.getValue());
            Predicate<CarPass> filter = (val) ->
                    Optional.ofNullable(companyName.getValue())
                            .map(v -> val.getCompanyName().equals(v.getCompanyName())).orElse(true);
            List<CarPass> filtered =
                    filterByCompanyName(all, filter);
            grid.setItems(filtered);
        });

        grid.addItemDoubleClickListener(event -> {
            CarPass item = event.getItem();
            item.setPassed(!item.isPassed());
            service.update(item);
            List<CarPass> all = service.getAllSortedByDate(arrivalDate.getValue());
            companyName.setItems(all);
            grid.setItems(all);
//            grid.getDataProvider().refreshAll();
            Notification
                    .show("Status updated")
                    .addThemeName("success");
        });

        List<CarPass> all = service.getAllSortedByDate(arrivalDate.getValue());

        companyName.setItems(all);
        grid.setItems(all);
    }

//    @Override
//    protected void onAttach(AttachEvent attachEvent) {
//        grid.setItems(service.getAllByDate(arrivalDate.getValue()));
//    }

    private List<CarPass> filterByCompanyName(List<CarPass> passes, Predicate<CarPass> filter) {
        return passes.stream().filter(filter).collect(Collectors.toList());
    }

}