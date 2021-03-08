package ru.volkov.guest.view.meet;

import com.github.appreciated.Swiper;
import com.github.appreciated.SwiperConfigBuilder;
import com.github.appreciated.card.RippleClickableCard;
import com.github.appreciated.config.Direction;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import lombok.extern.slf4j.Slf4j;
import ru.volkov.guest.MainAppLayout;
import ru.volkov.guest.data.entity.CarPass;
import ru.volkov.guest.data.service.carpass.CarPassService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@JsModule("./views/meet/meet-view.js")
@CssImport(value = "./views/meet/meet-view.css")
@Tag("meet-view")
@Route(value = "meet", layout = MainAppLayout.class)
@PageTitle("Meet")
public class MeetView extends PolymerTemplate<TemplateModel> {

    @Id("companyName")
    private ComboBox<CarPass> companyName;
    @Id("arrivalDate")
    private DatePicker arrivalDate;
    @Id("cardLayout")
    private VerticalLayout cardLayout;

    private final CarPassService service;

    public MeetView(CarPassService service) {
        this.service = service;
        arrivalDate.setMin(LocalDate.now());
        arrivalDate.setValue(LocalDate.now());

        companyName.setClearButtonVisible(true);
        companyName.setItemLabelGenerator((ItemLabelGenerator<CarPass>) CarPass::getCompanyName);

        arrivalDate.addValueChangeListener(event -> fillDataByFilters());
        companyName.addValueChangeListener(event -> fillDataByFilters());

        fillDataByFilters();
    }

    private void fillDataByFilters() {
        List<CarPass> data = service.getAllSortedByDate(arrivalDate.getValue());
        if (!companyName.isEmpty()) {
            data = data.stream()
                    .filter(val -> val.getCompanyName().equals(companyName.getValue().getCompanyName()))
                    .collect(Collectors.toList());
        } else {
            companyName.setItems(data);
        }
        cardLayout.removeAll();
        data.stream()
                .map(CarPassCardView::new)
                .map(this::getSlide)
                .map(s -> getSwiper(s, new RippleClickableCard()))
                .forEach(sw -> cardLayout.getElement().appendChild(sw.getElement()));
    }

    private Swiper getSwiper(Component... components) {
        Swiper sw = new Swiper(SwiperConfigBuilder.get()
                .withDirection(Direction.HORIZONTAL)
                .withAlignment(FlexComponent.Alignment.CENTER)
                .withJustifyContentMode(FlexComponent.JustifyContentMode.CENTER)
                .withAllowSlidePrev(true)
                .withAllowSlideNext(true)
                .build()
        );
        sw.onEnabledStateChanged(true);

        sw.getElement().addEventListener("dblclick", event ->
                getInByClassName(event.getSource(), "carPass")
                        .ifPresent(itm -> updateDate(Integer.parseInt(itm.getAttribute("id")))));

        sw.setHeight("55px");
        sw.setWidth("100%");
        sw.add(components);
        return sw;
    }

    private Optional<Element> getInByClassName(Element root, String className) {
        if (className.equals(root.getAttribute("class"))) {
            return Optional.of(root);
        }

        if (root.getChildCount() == 0) {
            return Optional.empty();
        }

        return root.getChildren()
                .map(child -> getInByClassName(child, className))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

    private RippleClickableCard getSlide(Component... component) {
        RippleClickableCard slide = new RippleClickableCard();
        slide.setElevationOnActionEnabled(true);
        slide.getTemplateDiv().setWidth("350px");
        slide.getTemplateDiv().setHeight("50px");
        slide.add(component);
        slide.getContent().setAlignItems(FlexComponent.Alignment.STRETCH);
        slide.getContent().setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        return slide;
    }

    private class CarPassCardView extends Composite<Div> {

        private final Div rootCover = new Div();
        private final Div regNumCover = new Div(new Label());
        private final Div companyNameCover = new Div(new Label());
        private final Div passedCover = new Div();

        public CarPassCardView(CarPass carPass) {
            rootCover.getElement()
                    .setAttribute("id", carPass.getId().toString())
                    .setAttribute("class", "carPass");
            regNumCover.getChildren().findFirst()
                    .ifPresent(label -> {
                        ((Label) label).setText(carPass.getRegNum());
                        label.getElement().getStyle().set("font-weight", "bold");
                    });
            companyNameCover.getChildren().findFirst()
                    .ifPresent(label -> ((Label) label).setText(carPass.getCompanyName()));

            Icon icon;
            if (carPass.isPassed()) {
                icon = VaadinIcon.CHECK_SQUARE.create();
                icon.setColor("green");
            } else {
                icon = VaadinIcon.SQUARE_SHADOW.create();
                icon.setColor("orange");
            }
            passedCover.getElement().addEventListener("click", event -> updateDate(carPass.getId()));
            passedCover.add(icon);

            passedCover.getElement().getStyle().set("margin-left", "30px");

            HorizontalLayout layout = new HorizontalLayout(passedCover, regNumCover, companyNameCover);
            layout.setAlignItems(FlexComponent.Alignment.STRETCH);
            layout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
            layout.setSizeFull();

            rootCover.add(layout);
            getContent().add(rootCover);
        }
    }

    private void updateDate(int id) {
        service.get(id).ifPresent(cp -> {
            cp.setPassed(!cp.isPassed());
            service.update(cp);
            fillDataByFilters();
            Notification
                    .show("Status updated")
                    .addThemeName("success");
        });
    }
}