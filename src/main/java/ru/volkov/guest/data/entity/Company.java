package ru.volkov.guest.data.entity;

import lombok.*;
import ru.volkov.guest.data.AbstractEntity;

import javax.persistence.Entity;
import java.io.Serializable;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Company extends AbstractEntity implements Serializable {

    private String name;
    private String email;
    private String phone;
    private String password;
    private LocalDate regDate = LocalDate.now();
    private int passCount;

//    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
//    private List<CarPass> passes = new ArrayList<>();
}
