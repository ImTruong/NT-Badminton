package com.dev.NT_Badminton.entities.contacts;

import com.dev.NT_Badminton.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "contacts")
public class Contact extends BaseEntity {
    String firstName;

    String lastName;

    String phone;

    String email;

    String streetAddress;

    String note;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    ContactType type;

    @Column(name = "user_id", columnDefinition = "int")
    Integer userId;

    @Column(name = "city_id", columnDefinition = "int")
    Integer cityId;

    @Column(name = "district_id", columnDefinition = "int")
    Integer districtId;

    @Column(name = "ward_id", columnDefinition = "int")
    Integer wardId;

    @ManyToOne
    @JoinColumn(name = "city_id", insertable = false, updatable = false)
    City city;

    @ManyToOne
    @JoinColumn(name = "district_id", insertable = false, updatable = false)
    District district;

    @ManyToOne
    @JoinColumn(name = "ward_id", insertable = false, updatable = false)
    Ward ward;
}
