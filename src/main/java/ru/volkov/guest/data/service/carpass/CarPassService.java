package ru.volkov.guest.data.service.carpass;

import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;
import ru.volkov.guest.data.OffsetBasedPageRequest;
import ru.volkov.guest.data.entity.CarPass;
import ru.volkov.guest.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class CarPassService extends CrudService<CarPass, Integer> {
    private final Sort regDataTime = Sort.by(Sort.Direction.DESC, "regDataTime");
    private final Sort passed = Sort.by(Sort.Direction.ASC, "passed");
    private final Sort statAndDate = passed.and(regDataTime);

    private CarPassRepository repository;

    public CarPassService(@Autowired CarPassRepository repository) {
        this.repository = repository;
    }

    @Override
    protected CarPassRepository getRepository() {
        return repository;
    }

    public List<CarPass> getAllSortedByDate(LocalDate date) {
        return repository.findAllByArrivalDate(date, statAndDate);
    }

    public Stream<CarPass> getSortedPage(int offset, int limit, List<QuerySortOrder> sortOrders) {
        Pageable sortedPage = new OffsetBasedPageRequest(offset, limit, getSort(sortOrders));
        return repository.findAll(sortedPage).get();
    }

    private Sort getSort(List<QuerySortOrder> sortOrders) {
        List<Sort.Order> orderList = sortOrders.stream().map(sortOrder -> {
            if (sortOrder.getDirection() == SortDirection.DESCENDING) {
                return Sort.Order.desc(sortOrder.getSorted());
            } else {
                return Sort.Order.asc(sortOrder.getSorted());
            }
        }).collect(LinkedList::new, LinkedList::add, LinkedList::addAll);
        return Sort.by(orderList);
    }

    public CarPass getById(Integer id) {
        return get(id).orElseThrow(() -> new NotFoundException("Not found user with that id"));
    }

    public int getCount() {
        return (int) repository.count();
    }
}
