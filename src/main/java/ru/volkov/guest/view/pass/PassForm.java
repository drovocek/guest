package ru.volkov.guest.view.pass;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
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
import ru.volkov.guest.data.service.AuthService;
import ru.volkov.guest.data.service.carpass.CarPassService;
import ru.volkov.guest.util.exception.NotFoundException;
import ru.volkov.guest.view.RootView;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static ru.volkov.guest.util.ConfigHelper.getDefNotify;

@UIScope
@RequiredArgsConstructor
@SpringComponent
public class PassForm extends Composite<SlideTab> implements BeforeLeaveObserver {

    private final TextField regNum = new TextField("Registration number");
    private final DatePicker arrivalDate = new DatePicker("Arrival date");

    private final Button confirm = new Button("Add");
    private final Button clear = new Button("Clear");
    private final HorizontalLayout buttonsGroup = new HorizontalLayout(confirm, clear);

    private final Div formContainer = new Div(regNum, arrivalDate, buttonsGroup);
    private final SlideTab formTab = new SlideTabBuilder(formContainer, "Form")
            .mode(SlideMode.BOTTOM)
            .autoCollapseSlider(false)
            .tabPosition(SlideTabPosition.BEGINNING)
            .build();

    private final AuthService authService;
    private final CarPassService carPassService;

    private final BeanValidationBinder<CarPass> binder = new BeanValidationBinder<>(CarPass.class);
    private CarPass carPass = new CarPass();

    @Override
    protected SlideTab initContent() {
        initButtons();
        configFields();
        initBinder();
        configStyles();
        return formTab;
    }

    @Override
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return super.addListener(eventType, listener);
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent beforeLeaveEvent) {
        clearAndClose();
    }

    private void configStyles() {
        formContainer.setClassName("form");
        confirm.addClassName("saveButton");
        clear.addClassName("clearButton");
    }

    private void configFields() {
        regNum.setPlaceholder("c065mk78");
        regNum.setClearButtonVisible(true);
        regNum.setMinLength(8);
        regNum.setMaxLength(8);
        regNum.setRequired(true);

        arrivalDate.setValue(LocalDate.now());
        arrivalDate.setMin(LocalDate.now());
        arrivalDate.setMax(LocalDate.now().plus(1, ChronoUnit.YEARS));
        regNum.setRequired(true);
    }

    private void initBinder() {
        binder.forField(regNum)
                .withValidator(name -> name.length() == 8, "Registration number size must be 8")
                .bind("regNum");

        binder.bindInstanceFields(this);
    }

    private void initButtons() {
        clear.addClickListener(event -> clear());
        confirm.addClickListener(event -> {
            if (binder.writeBeanIfValid(this.carPass)) {
                save();
                clearAndClose();
                fireEvent(new PassSaveEvent(this, false));
            }
        });
    }

    private void clear() {
        this.carPass = new CarPass();
        binder.readBean(carPass);
        arrivalDate.setValue(LocalDate.now());

        confirm.setText("Add");
        confirm.removeClassName("updateButton");
        confirm.addClassName("saveButton");
    }

    public void fillAndOpen(CarPass carPass) {
        System.out.println("bean: " + carPass);
        this.carPass = carPass;
        binder.readBean(carPass);

        confirm.setText("Update");
        confirm.removeClassName("saveButton");
        confirm.addClassName("updateButton");

        open();
    }

    public void save() {
        authService.getAuthUser().ifPresent(authUser -> {
            if (carPass.getId() == null) {
                getDefNotify("Pass created").addThemeName("success");
                showInRoot("Pass created", "Pass updated");
            } else if (carPass.getUser().getId().equals(authUser.getId())) {
                getDefNotify("Pass updated").addThemeName("success");
                showInRoot("Success updated", "Pass updated");
            } else {
                throw new NotFoundException("You have not Pass with some id");
            }
            carPassService.update(carPass);
        });
    }

    public void showInRoot(String title, String description) {
        UI.getCurrent().getChildren()
                .filter(component -> component.getClass() == RootView.class)
                .findFirst().ifPresent(root -> ((RootView) root).setNotify(title, description));
    }

    public void open() {
        if (!formTab.isExpanded()) {
            formTab.toggle();
        }
    }

    public void clearAndClose() {
        if (formTab.isExpanded()) {
            formTab.toggle();
            clear();
        }
    }
}
