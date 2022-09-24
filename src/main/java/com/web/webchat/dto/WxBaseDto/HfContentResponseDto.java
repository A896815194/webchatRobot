package com.web.webchat.dto.WxBaseDto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
//回复文本
@XStreamAlias("xml")
@Data
public class HfContentResponseDto extends WxBaseResonseDto {
    // 回复内容
    @XStreamAlias("Content")
    private String content;

    // 类型
    @XStreamAlias("MsgType")
    private String msgType = "text";

}
