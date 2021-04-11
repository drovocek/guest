package ru.volkov.guest.view.user;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.templatemodel.TemplateModel;
import lombok.RequiredArgsConstructor;
import ru.volkov.guest.component.CustomGrid;
import ru.volkov.guest.data.entity.User;
import ru.volkov.guest.data.service.user.UserService;

import javax.annotation.PostConstruct;

import static ru.volkov.guest.util.ConfigHelper.addColumns;
import static ru.volkov.guest.util.ConfigHelper.getDefNotify;

@RequiredArgsConstructor
@JsModule("./views/user/user-view.js")
@CssImport("./views/user/user-view.css")
@Tag("user-view")
@PageTitle("User")
public class UsersView extends PolymerTemplate<TemplateModel> {

    @Id("grid")
    private CustomGrid<User> grid;
    @Id("rootLayout")
    private VerticalLayout rootLayout;

    private final UserService userService;

    private final UserForm formTab;

    @PostConstruct
    private void initView() {
        addGridColumns(grid);
        grid.initBaseConfig();

        formTab.addListener(UserSaveEvent.class, event -> grid.refreshGrid());
        rootLayout.add(formTab);
    }

    private void addGridColumns(CustomGrid<User> grid) {
        grid.addComponentColumn((user) ->
        {
            Icon icon = grid.getSquareIconByStatus(user.getEnabled());
            icon.addClickListener(event -> changeEnabled(user.getId()));
            return icon;
        })
                .setHeader("Enabled")
                .setSortProperty("enabled");

        addColumns(grid, User.class);

        grid.addColumn(new LocalDateTimeRenderer<>(User::getLastActivity))
                .setHeader("Last activity");
    }

    private void changeEnabled(Integer id) {
        userService.get(id).filter(user -> {
            user.setEnabled(!user.getEnabled());
            userService.update(user);
            grid.refreshGrid();
            getDefNotify("Status updated").addThemeName("success");
            grid.showInRoot("Status updated", "Status updated");
            return true;
        }).orElseThrow(() -> new NotFoundException("User not found"));
    }
}
