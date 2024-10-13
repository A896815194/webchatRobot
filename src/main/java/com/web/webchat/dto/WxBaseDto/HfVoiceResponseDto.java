package com.web.webchat.dto.WxBaseDto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@XStreamAlias("xml")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class HfVoiceResponseDto extends WxBaseResonseDto {

    @XStreamAlias("Voice")
    private Voice voice;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Voice {

        @XStreamAlias("MediaId")
        private String mediaId;

    }

}
