package com.web.webchat.dto.WxBaseDto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@XStreamAlias("xml")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class HfImageResponseDto extends WxBaseResonseDto {

    @XStreamAlias("Image")
    private Image image;

    @XStreamAlias("Image")
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Image {

        @XStreamAlias("MediaId")
        private String mediaId;

    }


}
