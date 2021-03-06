package com.example.application.views.guest;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.example.application.views.main.MainView;
import com.vaadin.flow.router.RouteAlias;

@CssImport("./views/guest/guest-view.css")
@Route(value = "guest", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@PageTitle("Guest")
public class GuestView extends Div {

    public GuestView() {
        addClassName("guest-view");
        add(new Text("Content placeholder"));
    }

}
