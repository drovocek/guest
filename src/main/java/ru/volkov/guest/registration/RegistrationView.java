package ru.volkov.guest.registration;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import ru.volkov.guest.AbstractView;
import ru.volkov.guest.MainAppLayout;

@Tag("registration-view")
@PageTitle("Registration")
@RouteAlias(value = "", layout = MainAppLayout.class)
@Route(value = "registration", layout = MainAppLayout.class)
public class RegistrationView extends AbstractView {
    @Override
    public String getViewName() {
        return getClass().getName();
    }
}