package ru.volkov.guest.login;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import ru.volkov.guest.MainAppLayout;
import ru.volkov.guest.MessageBean;
import ru.volkov.guest.cabinet.CabinetView;

@Tag("login-view")
@PageTitle("Login")
@Route(value = "login", layout = MainAppLayout.class)
public class LoginView extends VerticalLayout {

    public LoginView(@Autowired MessageBean bean, @Autowired MainAppLayout appLayout) {

        Button button = new Button("Navigate to View2",
                e -> this.getUI().ifPresent(ui -> ui.navigate(CabinetView.class)));
        add(button);

        // can access the AppLayout instance via dependency injection
        int notificationCount = appLayout.getNotifications().getNotificationSize();
        add(new Paragraph("You have " + notificationCount + " notification(s)"));

        add(getLabel());
        add(getLabel());
        add(getLabel());
        add(getLabel());
        add(getLabel());
        add(getLabel());

    }

    Paragraph getLabel() {
        Paragraph label = new Paragraph("........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ");
        label.setWidth("100%");
        return label;
    }

}
