package ru.volkov.guest.getpass;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.volkov.guest.AbstractView;
import ru.volkov.guest.MainAppLayout;

@Tag("get-pass-view")
@PageTitle("Get pass")
@Route(value = "get-pass", layout = MainAppLayout.class)
public class GetPassView extends AbstractView {
    @Override
    public String getViewName() {
        return getClass().getName();
    }
}