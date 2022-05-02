package com.web.webchat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.web.webchat.enums.PushEvent;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {

    @JsonProperty
    private String api;

    @JsonProperty
    private String robot_wxid;

    @JsonProperty
    private String from_wxid;

    @JsonProperty
    private String from_name;

    @JsonProperty
    private String to_wxid;

    @JsonProperty
    private String msg;

    //1/文本消息 3/图片消息 34/语音消息  42/名片消息  43/视频 47/动态表情 48/地理位置  49/分享链接  2001/红包  2002/小程序  2003/群邀请
    private int type;
    @JsonProperty
    private String final_from_wxid;

    @JsonProperty
    private String final_from_name;

    @JsonProperty
    private PushEvent Event;

    @Builder.Default
    private Long timeStamp = System.currentTimeMillis();

    @Override
    public String toString() {
        return "RequestDto{" +
                "api='" + api + '\'' +
                ", robot_wxid='" + robot_wxid + '\'' +
                ", from_wxid='" + from_wxid + '\'' +
                ", to_wxid='" + to_wxid + '\'' +
                ", msg='" + msg + '\'' +
                ", Event='" + Event + '\'' +
                '}';
    }
}
