package ru.volkov.guest.view.login;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route("logout")
@PageTitle("LogOut")
public class LogOutView extends Composite<VerticalLayout> {
    public LogOutView() {
//        UI.getCurrent().getPage().setLocation("login");
//        VaadinSession.getCurrent().getSession().invalidate();
//        VaadinSession.getCurrent().close();
    }
}
