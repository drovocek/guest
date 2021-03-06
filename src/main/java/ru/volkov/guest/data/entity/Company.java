package ru.volkov.guest.data.entity;

import lombok.Data;
import ru.volkov.guest.data.AbstractEntity;

import javax.persistence.Entity;
import java.time.LocalDate;

@Data
@Entity
public class Company extends AbstractEntity {

    private String name;
    private String email;
    private String phone;
    private String password;
    private final LocalDate regDate = LocalDate.now();

//    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
//    private List<CarPass> passes = new ArrayList<>();
}
