package ru.volkov.guest.util.exception;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.dom.Element;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PrefixUtil {

    private static Stream<Element> getElementsInSlot(HasElement target,
                                                     String slot) {
        return target.getElement().getChildren()
                .filter(child -> slot.equals(child.getAttribute("slot")));
    }

    public static void setPrefixComponent(Component target, Component component) {
        clearSlot(target,"prefix");

        if (component != null) {
            component.getElement().setAttribute("slot", "prefix");
            target.getElement().appendChild(component.getElement());
        }
    }

    private static void clearSlot(Component target, String slot) {
        getElementsInSlot(target, slot).collect(Collectors.toList())
                .forEach(target.getElement()::removeChild);
    }

    private static Component getChildInSlot(HasElement target, String slot) {
        Optional<Element> element = getElementsInSlot(target, slot).findFirst();
        return element.map(value -> value.getComponent().get()).orElse(null);
    }

    public static Component getPrefixComponent(Component target) {
        return getChildInSlot(target, "prefix");
    }
}
