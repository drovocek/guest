package ru.volkov.guest.data.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Formula;
import ru.volkov.guest.data.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static ru.volkov.guest.util.ConfigHelper.GridHeader;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "user")
@Entity
public class CarPass extends AbstractEntity implements Serializable {

    private boolean passed = false;

    @GridHeader(name = "Passed date/time")
    private LocalDateTime passedDataTime;

    @GridHeader(name = "Registration Number")
    private String regNum;

    @GridHeader(name = "Arrival Date")
    private LocalDate arrivalDate;

    @GridHeader(name = "Creator")
    @Formula("(SELECT u.full_name FROM User u WHERE u.id = user_id)")
    private String creatorName;

    @GridHeader(name = "Root creator")
    @Formula("(SELECT u.full_name FROM User u WHERE u.id = root_id)")
    private String rootName;

    @GridHeader(name = "Registration date/time")
    private LocalDateTime regDataTime = LocalDateTime.now();

    private Integer rootId;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

//    private boolean enabled = true;
//    @Formula("(SELECT u.full_name FROM User u WHERE u.id = root_id)")
//    private String fullName;

    public CarPass(Integer rootId, String regNum, LocalDate arrivalDate) {
        this.rootId = rootId;
        this.regNum = regNum;
        this.arrivalDate = arrivalDate;
    }
}
