package ru.volkov.guest.data.service;

import com.vaadin.flow.data.provider.QuerySortOrder;

import java.util.List;
import java.util.stream.Stream;

public interface GridAndFormService<T> {

    int getCount();

    Stream<T> getSortedPage(int offset, int limit, List<QuerySortOrder> sortOrders);

    T getById(Integer id);
}
