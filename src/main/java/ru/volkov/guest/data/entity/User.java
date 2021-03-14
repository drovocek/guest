package ru.volkov.guest.data.entity;

import lombok.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.annotations.Formula;
import ru.volkov.guest.data.AbstractEntity;

import javax.persistence.Entity;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class User extends AbstractEntity implements Serializable {

    private String userName = "default";
    private String fullName;
    private Integer rootId;
    private String email = "default@email.ru";
    private String phone;

    private boolean enabled = true;
    private String passwordSalt;
    private String passwordHash;

    private LocalDate regDate = LocalDate.now();
    private LocalDateTime lastActivity = LocalDateTime.now();

    private Role role;

    @Formula("(SELECT COUNT(*) FROM Car_Pass cp WHERE cp.user_id = id)")
    private int passCount;
    @Formula("(SELECT COUNT(*) FROM User u WHERE u.id = root_id)")
    private int childrenCount;
    @Formula("(SELECT u.full_name FROM User u WHERE u.id = root_id)")
    private String rootName;

    public User(Integer rootId, String userName, String fullName, String rootName, String email, String phone, Role role, String password) {
        this.rootId = rootId;
        this.userName = userName;
        this.fullName = fullName;
        this.rootName = rootName;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.passwordSalt = RandomStringUtils.random(32);
        this.passwordHash = DigestUtils.sha1Hex(password.concat(passwordSalt));
    }

    public boolean checkPassword(String password) {
        return DigestUtils.sha1Hex(password.concat(passwordSalt)).equals(passwordHash);
    }

    public String getUserName() {
        if (userName.equals("default") && email != null && email.contains("@")) {
            userName = email.substring(0, email.indexOf("@"));
            return userName;
        }
        return userName;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + getId() + '\'' +
                ", userName='" + userName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", rootId=" + rootId +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", enabled=" + enabled +
                ", passwordSalt='" + passwordSalt + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", regDate=" + regDate +
                ", lastActivity=" + lastActivity +
                ", role=" + role +
                ", passCount=" + passCount +
                ", childrenCount=" + childrenCount +
                ", rootName='" + rootName + '\'' +
                '}';
    }
}
