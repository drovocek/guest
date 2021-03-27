package ru.volkov.guest.view;

import com.github.appreciated.app.layout.addons.notification.DefaultNotificationHolder;
import com.github.appreciated.app.layout.addons.notification.component.NotificationButton;
import com.github.appreciated.app.layout.addons.notification.entity.DefaultNotification;
import com.github.appreciated.app.layout.component.appbar.AppBarBuilder;
import com.github.appreciated.app.layout.component.applayout.LeftLayouts;
import com.github.appreciated.app.layout.component.builder.AppLayoutBuilder;
import com.github.appreciated.app.layout.component.menu.left.builder.LeftAppMenuBuilder;
import com.github.appreciated.app.layout.component.menu.left.items.LeftHeaderItem;
import com.github.appreciated.app.layout.component.menu.left.items.LeftNavigationItem;
import com.github.appreciated.app.layout.component.router.AppLayoutRouterLayout;
import com.github.appreciated.app.layout.entity.DefaultBadgeHolder;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.RequiredArgsConstructor;
import ru.volkov.guest.data.service.AuthService;

import javax.annotation.PostConstruct;

import static com.github.appreciated.app.layout.entity.Section.HEADER;

@Push
@UIScope
@RequiredArgsConstructor
public class RootView extends AppLayoutRouterLayout<LeftLayouts.LeftResponsiveHybrid> {
    /**
     * Do not initialize here. This will lead to NPEs
     */

    private final AuthService service;

    private final DefaultNotificationHolder notifications = new DefaultNotificationHolder();
    private final DefaultBadgeHolder badge = new DefaultBadgeHolder(5);;

    @PostConstruct
    public void initView() {
//        LeftNavigationItem menuEntry = new LeftNavigationItem("Menu", VaadinIcon.MENU.create(), View6.class);
//        badge.bind(menuEntry.getBadge());
        init(AppLayoutBuilder.get(LeftLayouts.LeftResponsiveHybrid.class)
                .withTitle("Guest")
                .withAppBar(AppBarBuilder.get()
                        .add(new NotificationButton<>(VaadinIcon.BELL, notifications))
                        .build())
                .withAppMenu(LeftAppMenuBuilder.get()
                        .addToSection(
                                HEADER,
                                new LeftHeaderItem("Version 1.0.5", "", "/images/logo.png")
                        )
                        .add(createMenuItems())
                        .build())
                .build());
    }

    private Component[] createMenuItems() {
        return service.getAuthUserRoutes()
                .map(AuthService.Routes::asLeftNavigationItems)
                .orElseGet(() -> new LeftNavigationItem[0]);
    }

    public void setNotify(String title, String description) {
        notifications.add(new DefaultNotification(title, description));
        badge.increase();
    }

//    @Override
//    public void showRouterLayoutContent(HasElement content) {
//        super.showRouterLayoutContent(content);
//        if (content instanceof CompanyComponent1) {
//            // do something
//        }
//    }
}
