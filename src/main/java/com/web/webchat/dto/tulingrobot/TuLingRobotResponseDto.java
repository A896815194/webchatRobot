package com.web.webchat.dto.tulingrobot;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.web.webchat.vo.Intent;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TuLingRobotResponseDto {
    // "intent": {
    //        "code": 10005,
    //        "intentName": "",
    //        "actionName": "",
    //        "parameters": {
    //            "nearby_place": "酒店"
    //        }
    @JsonProperty("intent")
    private Intent intent;
    //"results": [
    //        {
    //         	"groupType": 1,
    //            "resultType": "url",
    //            "values": {
    //                "url": "http://m.elong.com/hotel/0101/nlist/#indate=2016-12-10&outdate=2016-12-11&keywords=%E4%BF%A1%E6%81%AF%E8%B7%AF"
    //            }
    //        },
    //        {
    //         	"groupType": 1,
    //            "resultType": "text",
    //            "values": {
    //                "text": "亲，已帮你找到相关酒店信息"
    //            }
    //        }
    //    ]
    @JsonProperty("results")
    private List<RobotResult> results;

}
