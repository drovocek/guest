package ru.volkov.guest.data.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
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

    private Role role;

    //    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
//    private Company company;
    private String name;
    private String email;
    private String phone;
    private String passwordSalt;
    private String passwordHash;

    private String company;

    private LocalDate regDate = LocalDate.now();
    private LocalDateTime lastActivity = LocalDateTime.now();
    private int passCount;

//    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
//    private List<CarPass> passes = new ArrayList<>();

    public User(String name, String email, String phone, Role role, String password) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.passwordSalt = RandomStringUtils.random(32);
        this.passwordHash = DigestUtils.sha1Hex(password.concat(passwordSalt));
    }

    public boolean checkPassword(String password) {
        return DigestUtils.sha1Hex(password.concat(passwordSalt)).equals(passwordHash);
    }
}
