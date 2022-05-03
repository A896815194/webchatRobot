package com.web.webchat.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "thing")
public class ThingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "thing_name")
    private String thingName;

    @Column(name = "thing_type")
    private String thingType;

    @Column(name = "thing_desc")
    private String thingDesc;

    @Column(name = "thing_class")
    private String thingClass;

    @Column(name = "thing_method")
    private String thingMethod;

    @Column(name = "thing_template")
    private String thingTemplate;

    @Column(name = "start_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss ", timezone = "GMT+8")
    private Date startTime;

    @Column(name = "end_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss ", timezone = "GMT+8")
    private Date endTime;
    //传入的是秒
    @Column(name = "duration")
    private Long duration;

    @Column(name = "auto_use")
    private Integer autoUse;

}
