package ru.volkov.guest.component;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.vaadin.erik.SlideMode;
import org.vaadin.erik.SlideTab;
import org.vaadin.erik.SlideTabBuilder;
import org.vaadin.erik.SlideTabPosition;

public class CustomSlideTab extends SlideTab {

    public CustomSlideTab() {
        super(new SlideTabBuilder(new VerticalLayout(), "Form")
                .mode(SlideMode.BOTTOM)
                .autoCollapseSlider(false)
                .tabPosition(SlideTabPosition.END));
    }
}
