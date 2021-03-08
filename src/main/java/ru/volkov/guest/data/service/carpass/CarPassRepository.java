package ru.volkov.guest.data.service.carpass;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.volkov.guest.data.entity.CarPass;

import java.time.LocalDate;
import java.util.List;

public interface CarPassRepository extends JpaRepository<CarPass, Integer> {

    List<CarPass> findAllByArrivalDate(LocalDate date, Sort sorter);

}