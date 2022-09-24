package com.web.webchat.dto.WxBaseDto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@XStreamAlias("xml")
@NoArgsConstructor
@AllArgsConstructor

public class HfMusicResponseDto extends WxBaseResonseDto {

    @XStreamAlias("Music")
    private Music music;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Music {

        @XStreamAlias("MusicUrl")
        private String musicUrl;

        @XStreamAlias("HQMusicUrl")
        private String hqMusicUrl;

        @XStreamAlias("ThumbMediaId")
        private String thumbMediaId;

        @XStreamAlias("Title")
        private String title;

        @XStreamAlias("Description")
        private String description;

    }

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }
}
