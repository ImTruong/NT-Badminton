package com.dev.NT_Badminton.entities.discounts;

import com.dev.NT_Badminton.dto.constant.ActiveStatus;
import com.dev.NT_Badminton.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "discounts")
public class Discount extends BaseEntity {
    int discountPercentages;

    String description;

    @Temporal(TemporalType.TIMESTAMP)
    Date timeStarted;

    @Temporal(TemporalType.TIMESTAMP)
    Date timeEnded;

    @Column(name = "status", columnDefinition = "tinyint")
    ActiveStatus status;

    Integer productId;
}
