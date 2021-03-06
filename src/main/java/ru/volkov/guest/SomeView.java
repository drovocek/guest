package ru.volkov.guest;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Tag("some-view")
@PageTitle("Some")
@Route(value = "some", layout = MainAppLayout.class)
public class SomeView extends AbstractView {

}