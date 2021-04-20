package ru.volkov.guest.view.meet;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Getter;
import ru.volkov.guest.data.entity.CarPass;

import java.util.List;

@Getter
@UIScope
public class PassFilterEvent extends ComponentEvent<MeetForm> {

    private final List<CarPass> filtered;

    public PassFilterEvent(MeetForm source, boolean fromClient, List<CarPass> filtered) {
        super(source, fromClient);
        this.filtered = filtered;
    }
}
