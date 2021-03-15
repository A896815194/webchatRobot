package com.web.webchat.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "customer_info")
public class Demo {
    @Id
    private String customerId;

    private String name;

    private String grade;

    @Column(name = "interested_vehicle")
    private String interestedVehicle;

    private String dealerId;

    private Integer birthdayYear;

    private String birthday;

    private String birthdayDate;

    private String city;

    private String district;

    private String ucid;

    private Integer leadCount;

    private String familyMemberCount;

    private Integer salesVehicleCount;

    @Column(name = "as_vehicle_count", updatable = false, insertable = false)
    private Integer asVehicleCount;

    @Column(name = "vehicle_count", updatable = false, insertable = false)
    private Integer vehicleCount;

    private String mobile;
}
