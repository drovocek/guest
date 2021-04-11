package ru.volkov.guest.data.service.user;

import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;
import ru.volkov.guest.data.OffsetBasedPageRequest;
import ru.volkov.guest.data.entity.CarPass;
import ru.volkov.guest.data.entity.User;
import ru.volkov.guest.data.service.GridAndFormService;
import ru.volkov.guest.util.exception.NotFoundException;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Service
public class UserService extends CrudService<User, Integer> implements GridAndFormService<User> {

    private UserRepository repository;
    private final static String NOT_FOUND_TEMPLATE = "Not found %s with that %s";

    public UserService(@Autowired UserRepository repository) {
        this.repository = repository;
    }

    @Override
    protected UserRepository getRepository() {
        return repository;
    }

    public User getById(Integer id) {
        return get(id).orElseThrow(() -> new NotFoundException("Not found user with that id"));
    }

    public User getByUserName(String userName) {
        log.info("\n getByUserName: {}", userName);
        return Optional.ofNullable(repository.getByUserName(userName))
                .orElseThrow(() -> new NotFoundException("Not found user with that userName"));
    }

    public User getByActivationCode(String activationCode) {
        return Optional.ofNullable(repository.getByActivationCode(activationCode))
                .orElseThrow(() -> new NotFoundException("Not found user with that activationCode"));
    }

    public boolean isActivated(String activationCode) {
        return Optional.ofNullable(repository.getByActivationCode(activationCode))
                .filter(user -> user.getPasswordHash() != null)
                .isPresent();
    }

    public Stream<User> getSortedPage(int offset, int limit, List<QuerySortOrder> sortOrders) {
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

    public int getCount() {
        return (int) repository.count();
    }
}
