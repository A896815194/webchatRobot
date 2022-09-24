package com.web.webchat.dto.WxBaseDto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
//回复文本
@XStreamAlias("xml")
@Data
public class HfArticleResponseDto extends WxBaseResonseDto {
    // 回复内容
    @XStreamAlias("ArticleCount")
    private String articleCount = "1";

    // 类型
    @XStreamAlias("Articles")
    private List<Article> articles;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @XStreamAlias("item")
    public static class Article {

        @XStreamAlias("PicUrl")
        private String picUrl;

        @XStreamAlias("Url")
        private String url;

        @XStreamAlias("Title")
        private String title;

        @XStreamAlias("Description")
        private String description;

    }
}
