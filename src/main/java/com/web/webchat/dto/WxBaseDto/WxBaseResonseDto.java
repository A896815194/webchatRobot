package com.web.webchat.dto.WxBaseDto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@XStreamAlias("xml")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WxBaseResonseDto {

    @XStreamAlias("ToUserName")
    private String toUserName;

    @XStreamAlias("FromUserName")
    private String fromUserName;

    @XStreamAlias("CreateTime")
    private String createTime = String.valueOf(System.currentTimeMillis());

    @XStreamAlias("MsgType")
    private String msgType;

}
