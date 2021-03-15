package com.web.webchat.dto.tulingrobot;

import lombok.*;

import java.util.Map;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TuLingRobotRequestDto {
   //0-文本(默认)、1-图片、2-音频
    private int reqType;
    //"inputText": {
    //            "text": "附近的酒店"
    //        },
    // "inputImage": {
    //            "url": "imageUrl"
    //        },
    private Map<String, Object> perception;

    private Map<String,String> userInfo;




}
