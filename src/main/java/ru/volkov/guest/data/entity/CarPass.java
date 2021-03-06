package ru.volkov.guest.data.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
public class CarPass extends AbstractEntity implements Serializable {

    private String regNum;

    private LocalDate arrivalDate;

    private LocalDateTime regDataTime;

    private boolean passed;

//    private LocalDateTime passedDataTime;

    private String companyName;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "company_id", nullable = false)
//    private Company company;

    public CarPass(Integer id, String regNum, LocalDate arrivalDate, boolean passed, String companyName) {
        setId(id);
        this.regNum = regNum;
        this.arrivalDate = arrivalDate;
        this.passed = passed;
        this.companyName = companyName;
    }
}
