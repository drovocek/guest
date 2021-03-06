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
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import lombok.extern.slf4j.Slf4j;
import ru.volkov.guest.MainAppLayout;
import ru.volkov.guest.data.entity.CarPass;
import ru.volkov.guest.data.service.CarPassService;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@JsModule("./views/getpass/get-pass-view.js")
@CssImport("./views/getpass/get-pass-view.css")
@Tag("get-pass-view")
@PageTitle("Get pass")
@Route(value = "getpass", layout = MainAppLayout.class)
public class GetPassView extends PolymerTemplate<TemplateModel> {

    @Id("regNum")
    private TextField regNum;
    @Id("arrivalDate")
    private DatePicker arrivalDate;

    @Id("get")
    private Button get;
    @Id("clear")
    private Button clear;

    private final CarPassService service;
    private final Binder<CarPass> binder = new Binder(CarPass.class);

    public GetPassView(CarPassService service) {
        this.service = service;
        regNum.setPlaceholder("c065mk78");
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
    private void createCarPass() {
        CarPass newPass = binder.getBean();
        if (binder.validate().isOk()) {
            newPass.setRegDataTime(LocalDateTime.now());
            newPass.setCompanyName("OOO Roga-Kopita");
            service.update(newPass);
            Notification
                    .show("Pass created")
                    .addThemeName("success");
            clearForm();
        }
    }

    @EventHandler
    private void clearForm() {
        CarPass newPass = new CarPass();
        newPass.setArrivalDate(LocalDate.now());
        binder.setBean(newPass);
    }
}