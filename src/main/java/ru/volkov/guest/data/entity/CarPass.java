package ru.volkov.guest.data.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Formula;
import ru.volkov.guest.data.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class CarPass extends AbstractEntity implements Serializable {

    private String regNum;
    private Integer rootId;
    private LocalDate arrivalDate;
    private LocalDateTime regDataTime = LocalDateTime.now();

    private boolean passed = false;
    private LocalDateTime passedDataTime;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

//    private boolean enabled = true;
//    @Formula("(SELECT u.full_name FROM User u WHERE u.id = root_id)")
//    private String fullName;

    @Formula("(SELECT u.full_name FROM User u WHERE u.id = user_id)")
    private String creatorName;

    @Formula("(SELECT u.full_name FROM User u WHERE u.id = root_id)")
    private String rootName;

    public CarPass(Integer rootId, String regNum, LocalDate arrivalDate) {
        this.rootId = rootId;
        this.regNum = regNum;
        this.arrivalDate = arrivalDate;
    }
}
