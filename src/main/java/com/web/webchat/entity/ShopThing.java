package com.web.webchat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShopThing {

    private Long thingId;

    private String thingName;

    private Integer thingCount;

    private Long thingPrice;

    private Integer isDelte;

    private Integer isOne;

    private String thingDesc;
}
