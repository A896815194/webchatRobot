package com.web.webchat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class UserThing {
    private Long useBagId;

    private String wxidId;

    private String thingId;

    private String thingName;

    private String thingType;

    private String thingTemplate;

    private String thingClass;

    private String thingMethod;

    private Integer autoUse;

    private Date startTime;

    private Date endTime;

    private Integer useCount;
}
