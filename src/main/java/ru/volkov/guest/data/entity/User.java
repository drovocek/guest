package ru.volkov.guest.data.entity;

import lombok.*;
import ru.volkov.guest.data.AbstractEntity;

import javax.persistence.Entity;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class User extends AbstractEntity implements Serializable {

//    private Role role = Role.USER;

    //    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
//    private Company company;
    private String role;
    private String name;
    private String email;
    private String phone;
    private String password;

    private String company;

    private LocalDate regDate = LocalDate.now();
    private LocalDateTime lastActivity;
    private int passCount;

//    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
//    private List<CarPass> passes = new ArrayList<>();
}
