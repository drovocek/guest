package ru.volkov.guest.view.getpass;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.templatemodel.TemplateModel;
import lombok.extern.slf4j.Slf4j;
import ru.volkov.guest.data.entity.CarPass;
import ru.volkov.guest.data.service.AuthService;
import ru.volkov.guest.data.service.carpass.CarPassService;

import java.time.LocalDate;

@Slf4j
@JsModule("./views/getpass/get-pass-view.js")
@CssImport("./views/getpass/get-pass-view.css")
@Tag("get-pass-view")
@PageTitle("Get pass")
public class GetPassView extends PolymerTemplate<TemplateModel> {

    @Id("regNum")
    private TextField regNum;
    @Id("arrivalDate")
    private DatePicker arrivalDate;

    @Id("enter")
    private Button enter;
    @Id("clear")
    private Button clear;

    private final CarPassService carPassService;
    private final AuthService authService;
    private final Binder<CarPass> binder = new Binder(CarPass.class);

    public GetPassView(CarPassService carPassService, AuthService authService) {
        this.carPassService = carPassService;
        this.authService = authService;
        binder.bindInstanceFields(this);
        binder.forField(regNum)
                .withValidator(name -> name.length() > 8 && name.length() < 11,
                        "Length must be not less then 8 and not more then 10")
                .bind(CarPass::getRegNum, CarPass::setRegNum);
        clearForm();
        arrivalDate.setValue(LocalDate.now());
        arrivalDate.setMin(LocalDate.now());
    }

    @EventHandler
    private void save() {
        if (binder.validate().isOk()) {
            CarPass newPass = binder.getBean();
            authService.getAuthUser().ifPresent(authUser -> {
                newPass.setUser(authUser);
                newPass.setRootId(authUser.getRootId());
                carPassService.update(newPass);
                Notification
                        .show("Pass created")
                        .addThemeName("success");
                clearForm();
                navigateToMainPage();
            });
        }
    }

    @EventHandler
    private void clearForm() {
        CarPass newPass = new CarPass();
        newPass.setArrivalDate(LocalDate.now());
        binder.setBean(newPass);
    }

    private void navigateToMainPage() {
        getUI().ifPresent(ui -> ui.navigate("passes"));
    }
}