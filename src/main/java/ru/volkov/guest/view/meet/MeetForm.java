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

import java.time.LocalDate;
import java.util.List;
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

    @Override
    protected SlideTab initContent() {
        initButtons();
        configFields();
        configStyles();
        return formTab;
    }

    private void initButtons() {
        clear.addClickListener(event -> clear());
        confirm.addClickListener(event -> filter());
    }

    public void filter() {
        List<CarPass> filtered = getFiltered();
        fireEvent(new PassFilterEvent(this, false, filtered));
    }

    private List<CarPass> getFiltered() {
        List<CarPass> data = service.getAllSortedByDate(arrivalDate.getValue());
        if (!companyName.isEmpty()) {
            data = data.stream()
                    .filter(val -> val.getRootName().equals(companyName.getValue().getRootName()))
                    .collect(Collectors.toList());
        } else {
            companyName.setItems(data);
        }

        return data;
    }

    private void configStyles() {
        formContainer.setClassName("form");
        confirm.addClassName("saveButton");
        clear.addClassName("clearButton");
    }

    private void configFields() {
        arrivalDate.setMin(LocalDate.now());
        arrivalDate.setValue(LocalDate.now());

        companyName.setClearButtonVisible(true);
        companyName.setItemLabelGenerator((ItemLabelGenerator<CarPass>) CarPass::getRootName);
    }

    public void clearAndClose() {
        if (formTab.isExpanded()) {
            formTab.toggle();
            clear();
        }
    }

    private void clear() {
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
