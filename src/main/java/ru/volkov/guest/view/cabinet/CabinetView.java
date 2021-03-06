package ru.volkov.guest.view.cabinet;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.volkov.guest.AbstractView;
import ru.volkov.guest.MainAppLayout;
import ru.volkov.guest.SubContent;

@Tag("cabinet-view")
@PageTitle("Cabinet")
@Route(value = "cabinet", layout = MainAppLayout.class)
public class CabinetView extends AbstractView {

    public CabinetView() {
        // Navigate to content that is not accessible from the menu directly. And see the results in the UI
        add(new Button("SubContent", buttonClickEvent -> UI.getCurrent().navigate(SubContent.class)));
    }


}