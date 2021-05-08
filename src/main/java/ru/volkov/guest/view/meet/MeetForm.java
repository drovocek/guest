package ru.volkov.guest.view.meet;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.RequiredArgsConstructor;
import org.vaadin.erik.SlideMode;
import org.vaadin.erik.SlideTab;
import org.vaadin.erik.SlideTabBuilder;
import org.vaadin.erik.SlideTabPosition;
import ru.volkov.guest.data.entity.CarPass;
import ru.volkov.guest.data.service.carpass.CarPassService;
import ru.volkov.guest.util.LogOperator;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UIScope
@RequiredArgsConstructor
@SpringComponent
public class MeetForm extends Composite<SlideTab> implements BeforeLeaveObserver {

    private final ComboBox<CarPass> companyName = new ComboBox<>();
    private final DatePicker arrivalDate = new DatePicker();

    private final Button confirm = new Button("Find");
    private final Button clear = new Button("Clear");
    private final HorizontalLayout buttonsGroup = new HorizontalLayout(confirm, clear);

    private final Div formContainer = new Div(companyName, arrivalDate, buttonsGroup);
    private final SlideTab formTab = new SlideTabBuilder(formContainer, "Filter")
            .mode(SlideMode.BOTTOM)
            .autoCollapseSlider(false)
            .tabPosition(SlideTabPosition.BEGINNING)
            .build();

    private final CarPassService service;

    private final LogOperator log = new LogOperator(this.getClass());

    @Override
    protected SlideTab initContent() {
        log.onConsole(true).onError(false).onInfo(false);
        initButtons();
        configFields();
        configStyles();
        return formTab;
    }

    private void initButtons() {
        log.console();
        clear.addClickListener(event -> clear());
        confirm.addClickListener(event -> filter());
    }

    public void filter() {
        log.console();
        List<CarPass> filtered = getFilteredByDate();

        if (!companyName.isEmpty()) {
            filtered = filterByName(filtered);
        } else {
            updateCompanyName(filtered);
        }

        fireEvent(new PassFilterEvent(this, false, filtered));
    }

    private List<CarPass> getFilteredByDate() {
        log.console();
        return service.getAllSortedByDate(arrivalDate.getValue());
    }

    private List<CarPass> filterByName(List<CarPass> data) {
        log.console();
        return data.stream()
                .filter(carPass -> {
                    String dataRootName = carPass.getRootName();
                    String filterRootName = companyName.getValue().getRootName();
                    if (dataRootName == null && filterRootName == null) {
                        return carPass.getCreatorName().equals(companyName.getValue().getCreatorName());
                    } else if (dataRootName == null) {
                        return carPass.getCreatorName().equals(filterRootName);
                    }
                    return dataRootName.equals(companyName.getValue().getCreatorName());
                })
                .collect(Collectors.toList());
    }

    private void updateCompanyName(List<CarPass> data) {
        log.console();
        Map<String, CarPass> passes = new HashMap<>();
        data.forEach(carPass -> {
            String rootName = carPass.getRootName();
            if (rootName == null) {
                passes.put(carPass.getCreatorName(), carPass);
            } else {
                passes.put(rootName, carPass);
            }
        });
        companyName.setItems(passes.values());
    }

    private void configStyles() {
        log.console();
        formContainer.setClassName("form");
        confirm.addClassName("saveButton");
        clear.addClassName("clearButton");
    }

    private void configFields() {
        log.console();
        arrivalDate.setMin(LocalDate.now());
        arrivalDate.setValue(LocalDate.now());

        companyName.setClearButtonVisible(true);
        companyName.setItemLabelGenerator((ItemLabelGenerator<CarPass>) carPass -> {
            String rootName = carPass.getRootName();
            if (rootName == null) {
                return carPass.getCreatorName();
            }
            return rootName;
        });

        List<CarPass> filtered = getFilteredByDate();
        updateCompanyName(filtered);
    }

    public void clearAndClose() {
        log.console();
        if (formTab.isExpanded()) {
            formTab.toggle();
            clear();
        }
    }

    private void clear() {
        log.console();
        arrivalDate.setValue(LocalDate.now());
        companyName.setValue(null);
    }

    @Override
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return super.addListener(eventType, listener);
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent beforeLeaveEvent) {
        clearAndClose();
    }
}
