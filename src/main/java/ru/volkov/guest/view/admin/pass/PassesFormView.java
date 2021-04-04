package ru.volkov.guest.view.admin.pass;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
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

@VaadinSessionScope
@RequiredArgsConstructor
@Component
public class PassesFormView extends Composite<SlideTab> {

    private TextField regNum;
    private DatePicker arrivalDate;
    private Button confirm;
    private Button clear;
    private Div formContainer;
    private SlideTab formTab;

    private final AuthService authService;
    private final CarPassService carPassService;

    private final BeanValidationBinder<CarPass> binder = new BeanValidationBinder<>(CarPass.class);
    private CarPass carPass = new CarPass();

    @Override
    protected SlideTab initContent() {
        regNum = new TextField("Registration number");
        arrivalDate = new DatePicker("Arrival date");
        confirm = new Button("Update");
        clear = new Button("Clear");
        formContainer = new Div(regNum, arrivalDate, new HorizontalLayout(confirm, clear));
        formTab = new SlideTabBuilder(formContainer, "Form")
                .mode(SlideMode.BOTTOM)
                .autoCollapseSlider(false)
                .tabPosition(SlideTabPosition.BEGINNING)
                .build();

        initButtons();
        configFields();
        initBinder();
        configStyles();

        return formTab;
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
        confirm.addClickListener(event -> save());
        clear.addClickListener(event -> clear());
    }

    private void clear() {
        this.carPass = new CarPass();
        binder.readBean(carPass);
        arrivalDate.setValue(LocalDate.now());

        confirm.setText("Save");
        confirm.removeClassName("updateButton");
        confirm.addClassName("saveButton");
    }

    public void fillAndOpen(CarPass carPass) {
        System.out.println("bean: " + carPass);
        open();
        this.carPass = carPass;
        binder.readBean(carPass);

        confirm.setText("Update");
        confirm.removeClassName("saveButton");
        confirm.addClassName("updateButton");
    }

    public void save() {
        if (binder.writeBeanIfValid(this.carPass)) {
            authService.getAuthUser().ifPresent(authUser -> {
                if (carPass.getId() == null) {
                    getDefNotify("User created").addThemeName("success");
                    showInRoot("User created", "User updated");
                } else if (carPass.getUser().getId().equals(authUser.getId())) {
                    getDefNotify("Car pass updated").addThemeName("success");
                    showInRoot("Success updated", "Car pass updated");
                } else {
                    throw new NotFoundException("You have not car pass with some id");
                }
                carPassService.update(carPass);
                clearAndClose();
            });
        }
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
