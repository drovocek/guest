package ru.volkov.guest.view.pass;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.spring.annotation.UIScope;

@UIScope
public class PassSaveEvent extends ComponentEvent<PassForm> {
    public PassSaveEvent(PassForm source, boolean fromClient) {
        super(source, fromClient);
    }
}
