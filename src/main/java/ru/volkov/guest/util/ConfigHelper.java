package ru.volkov.guest.util;

import com.google.common.collect.Maps;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import lombok.extern.slf4j.Slf4j;
import ru.volkov.guest.data.entity.User;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public final class ConfigHelper {

    public static void addColumns(Grid<?> grid, Class<?> beanType) {
        getHeaders(beanType).forEach((param, header) ->
                grid.addColumn(obj -> getBeanGetters(beanType).get(param).apply(obj))
                        .setHeader(header)
                        .setSortProperty(param));
    }

    private static LinkedHashMap<String, String> getHeaders(Class<?> beanType) {
        log.info("getHeaders({})", beanType.getSimpleName());
        CashStorage.manageHeadersCash(10);
        return CashStorage.headersByClassCash.computeIfAbsent(beanType, (bt) ->
                Arrays.stream(bt.getDeclaredFields())
                        .map(field -> {
                            log.info("getHeaders(NEW)");
                            String annotationVal = Arrays.stream(field.getAnnotations())
                                    .filter(annotation -> annotation.annotationType().equals(GridHeader.class))
                                    .map(annotation -> ((GridHeader) annotation).name())
                                    .findFirst().orElse(null);
                            return Maps.immutableEntry(field.getName(), annotationVal);
                        })
                        .filter(entry -> !(entry.getValue() == null))
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (existing, replacement) -> existing,
                                LinkedHashMap::new)));
    }

    private static Map<String, Function<Object, Object>> getBeanGetters(Class<?> beanType) {
        log.info("getBeanGetters({})", beanType.getSimpleName());
        CashStorage.manageGettersCash(10);
        return CashStorage.gettersByClassCash.computeIfAbsent(beanType, (bt) ->
                Arrays.stream(bt.getDeclaredFields())
                        .filter(field -> Arrays.stream(field.getAnnotations()).anyMatch(annotation -> annotation.annotationType().equals(GridHeader.class)))
                        .map(field -> {
                            log.info("getBeanGetters(NEW)");
                            Function<Object, Object> getterFunc = (entity) -> {
                                String paramName = field.getName();
                                String getterName = "get".concat(Character.toUpperCase(paramName.charAt(0)) + paramName.substring(1));
                                try {
                                    Method getterMethod = bt.getMethod(getterName);
                                    return getterMethod.invoke(entity);
                                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                                    e.printStackTrace();
                                }
                                return null;
                            };
                            return Maps.immutableEntry(field.getName(), getterFunc);
                        })
                        .filter(entry -> !(entry.getValue() == null))
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue
                        )));
    }

    public static void addGridStyles(Grid<?> grid) {
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();
    }

    private final static class CashStorage {
        private final static Map<Class<?>, LinkedHashMap<String, String>> headersByClassCash = new HashMap<>();
        private final static Map<Class<?>, Map<String, Function<Object, Object>>> gettersByClassCash = new HashMap<>();

        private static void manageHeadersCash(int maxSize) {
            log.info("manageHeadersCash({})", headersByClassCash.size());
            if (headersByClassCash.size() > maxSize) {
                headersByClassCash.clear();
            }
        }

        private static void manageGettersCash(int maxSize) {
            log.info("manageGettersCash({})", gettersByClassCash.size());
            if (gettersByClassCash.size() > maxSize) {
                gettersByClassCash.clear();
            }
        }
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface GridHeader {
        String name() default "default";
    }
}