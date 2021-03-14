package ru.volkov.guest.data.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;
import ru.volkov.guest.data.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
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

    @Formula("(SELECT u.full_name FROM User u WHERE u.id = root_id)")
    private String rootName;

    public CarPass(String regNum, LocalDate arrivalDate) {
        this.regNum = regNum;
        this.arrivalDate = arrivalDate;
    }
}
