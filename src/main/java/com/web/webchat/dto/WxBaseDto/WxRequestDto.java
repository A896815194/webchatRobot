package com.web.webchat.dto.WxBaseDto;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WxRequestDto {

    @SerializedName("tousername")
    private String toUserName;
    @SerializedName("fromusername")
    private String fromUserName;
    @SerializedName("createtime")
    private Long createTime;
    @SerializedName("msgtype")
    private String msgType;
    @SerializedName("content")
    private String content;
    @SerializedName("msgid")
    private String msgId;
    @SerializedName("msgdataid")
    private String msgData;
    @SerializedName("idx")
    private String idx;
    //subscribe 订阅   unsubscribe 取消订阅
    @SerializedName("event")
    private String event;
}
