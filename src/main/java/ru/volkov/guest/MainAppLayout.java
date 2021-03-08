package ru.volkov.guest;

import com.github.appreciated.app.layout.addons.notification.DefaultNotificationHolder;
import com.github.appreciated.app.layout.addons.notification.component.NotificationButton;
import com.github.appreciated.app.layout.component.appbar.AppBarBuilder;
import com.github.appreciated.app.layout.component.applayout.LeftLayouts;
import com.github.appreciated.app.layout.component.builder.AppLayoutBuilder;
import com.github.appreciated.app.layout.component.menu.left.builder.LeftAppMenuBuilder;
import com.github.appreciated.app.layout.component.menu.left.builder.LeftSubMenuBuilder;
import com.github.appreciated.app.layout.component.menu.left.items.LeftHeaderItem;
import com.github.appreciated.app.layout.component.menu.left.items.LeftNavigationItem;
import com.github.appreciated.app.layout.component.router.AppLayoutRouterLayout;
import com.github.appreciated.app.layout.entity.DefaultBadgeHolder;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;
import ru.volkov.guest.view.admin.company.CompanyView;
import ru.volkov.guest.view.admin.pass.PassView;
import ru.volkov.guest.view.admin.user.UserView;
import ru.volkov.guest.view.getpass.GetPassView;
import ru.volkov.guest.view.login.LoginView;
import ru.volkov.guest.view.meet.MeetView;
import ru.volkov.guest.view.registration.RegistrationView;
import ru.volkov.guest.view.settings.SettingsView;

import static com.github.appreciated.app.layout.entity.Section.HEADER;

@Push
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
@PWA(name = "Project Base for Vaadin Flow with Spring", shortName = "Project Base")
@Component
@UIScope // optional but useful; allows access to this instance from views, see View1.
public class MainAppLayout extends AppLayoutRouterLayout<LeftLayouts.LeftResponsive> {
    private DefaultNotificationHolder notifications = new DefaultNotificationHolder();
    private DefaultBadgeHolder badge = new DefaultBadgeHolder(5);

    public MainAppLayout() {
        notifications.addClickListener(notification -> {/* ... */});

//        LeftNavigationItem menuEntry = new LeftNavigationItem("Menu", VaadinIcon.MENU.create(), SomeView.class);
//        badge.bind(menuEntry.getBadge());

        init(AppLayoutBuilder.get(LeftLayouts.LeftResponsive.class)
                .withTitle("Guest")
                .withAppBar(AppBarBuilder.get()
                        .add(new NotificationButton<>(VaadinIcon.BELL, notifications))
                        .build())
                .withAppMenu(LeftAppMenuBuilder.get()
                        .addToSection(HEADER, new LeftHeaderItem("Prototype", "Version 1.0.0", "/frontend/images/logo.png"))
                        .add(new LeftNavigationItem("Registration", VaadinIcon.ENTER.create(), RegistrationView.class),
                                new LeftNavigationItem("Login", VaadinIcon.USER.create(), LoginView.class),
                                new LeftNavigationItem("GetPass", VaadinIcon.GOLF.create(), GetPassView.class),
                                LeftSubMenuBuilder.get("Admin", VaadinIcon.TOOLS.create())
                                        .add(new LeftNavigationItem("Pass", VaadinIcon.USER_CARD.create(), PassView.class),
                                                new LeftNavigationItem("User", VaadinIcon.USERS.create(), UserView.class),
                                                new LeftNavigationItem("Company", VaadinIcon.PIGGY_BANK_COIN.create(), CompanyView.class))
                                        .build(),
                                new LeftNavigationItem("Meet", VaadinIcon.CHECK_SQUARE_O.create(), MeetView.class),
                                new LeftNavigationItem("Settings", VaadinIcon.COG.create(), SettingsView.class))
                        .build())
                .build());
//                                LeftSubMenuBuilder.get("My Submenu", VaadinIcon.PLUS.create())
//                                        .add(LeftSubMenuBuilder
//                                                        .get("My Submenu", VaadinIcon.PLUS.create())
//                                                        .add(new LeftNavigationItem("Charts", VaadinIcon.SPLINE_CHART.create(), CabinetView.class),
//                                                                new LeftNavigationItem("Contact", VaadinIcon.CONNECT.create(), GetPassView.class),
//                                                                new LeftNavigationItem("More", VaadinIcon.COG.create(), MeetView.class))
//                                                        .build(),
//                                                new LeftNavigationItem("Contact1", VaadinIcon.CONNECT.create(), GetPassView.class),
//                                                new LeftNavigationItem("More1", VaadinIcon.COG.create(), RegistrationView.class))
//                                        .build(),
//                                menuEntry)
//                        .addToSection(FOOTER, new LeftClickableItem("Clickable Entry", VaadinIcon.COG.create(), clickEvent -> Notification.show("onClick ...")))


    }

    public DefaultNotificationHolder getNotifications() {
        return notifications;
    }

    public DefaultBadgeHolder getBadge() {
        return badge;
    }
}
