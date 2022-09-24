package com.web.webchat.dto.WxBaseDto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@XStreamAlias("xml")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class HfVideoResponseDto extends WxBaseResonseDto {

    @XStreamAlias("Video")
    private Video video;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Video {

        @XStreamAlias("MediaId")
        private String mediaId;

        @XStreamAlias("Title")
        private String title;

        @XStreamAlias("Description")
        private String description;

    }

}
