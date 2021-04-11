package ru.volkov.guest.view.user;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.spring.annotation.UIScope;

@UIScope
public class UserSaveEvent extends ComponentEvent<UserForm> {
    public UserSaveEvent(UserForm source, boolean fromClient) {
        super(source, fromClient);
    }
}
