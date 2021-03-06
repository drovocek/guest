package ru.volkov.guest.meet;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.volkov.guest.AbstractView;
import ru.volkov.guest.MainAppLayout;

@Tag("meet-view")
@PageTitle("Meet")
@Route(value = "meet", layout = MainAppLayout.class)
public class MeetView extends AbstractView {
    @Override
    public String getViewName() {
        return getClass().getName();
    }
}