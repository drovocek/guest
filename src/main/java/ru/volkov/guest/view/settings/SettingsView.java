package ru.volkov.guest.view.settings;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.volkov.guest.AbstractView;
import ru.volkov.guest.MainAppLayout;

@Tag("settings-view")
@PageTitle("Settings")
@Route(value = "settings", layout = MainAppLayout.class)
public class SettingsView extends AbstractView {

}