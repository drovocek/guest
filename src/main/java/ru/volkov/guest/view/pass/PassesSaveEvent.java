package ru.volkov.guest.view.pass;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.spring.annotation.UIScope;

@UIScope
public class PassesSaveEvent extends ComponentEvent<PassesFormView> {
    public PassesSaveEvent(PassesFormView source, boolean fromClient) {
        super(source, fromClient);
    }
}
