package ru.volkov.guest;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.extern.slf4j.Slf4j;
import ru.volkov.guest.util.exception.AuthException;
import ru.volkov.guest.util.exception.NotFoundException;
import ru.volkov.guest.util.exception.NotYetImplementedException;

import static com.vaadin.flow.component.notification.Notification.Position.BOTTOM_START;

@Slf4j
@SpringComponent
public class ApplicationServiceInitListener implements VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addSessionInitListener(e -> {
            e.getSession().setErrorHandler(errorEvent -> {
                Throwable t = errorEvent.getThrowable();
                if (t instanceof NotFoundException ||
                        t instanceof AuthException ||
                        t instanceof NotYetImplementedException
                ) {
                    log.info("serviceInit(catch)");
                    Notification.show(t.getMessage(), 2000, BOTTOM_START).addThemeName("error");
                } else {
                    throw new RuntimeException(t);
                }
            });
        });
    }
}