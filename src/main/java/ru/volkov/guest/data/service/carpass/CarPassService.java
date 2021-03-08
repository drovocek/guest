package ru.volkov.guest.data.service.carpass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;
import ru.volkov.guest.data.entity.CarPass;

import java.time.LocalDate;
import java.util.List;

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

    public List<CarPass> getAllSorted() {
        return repository.findAll(statAndDate);
    }
}
