package com.web.webchat.config;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Component
public class PropertiesEntity {

    @Value("${api.tuling-api}")
    public String tulingApi;

    @Value("${api.tuling-api-key}")
    public String tulingApiKey;

    @Value("${api.tuling-user-id}")
    public String tulingUserId;

    @Value("${api.wechat-url}")
    public String wechatUrl;

    @Value("${api.Baidu-text-review}")
    public String BaiduTextReviewUrl;

    @Value("${api.source-voice-root-path}")
    public String sourceVoiceRootPath;

    @Value("${api.silk-v3-decoder-path}")
    public String silkV3Path;

    @Value("${api.ffmpeg-path}")
    public String ffmpegPath;

    @Value("${api.reply-interval}")
    public Long replyInterval;

    @Value("${api.robotId}")
    public String robotId;

    @Value("${api.robotName}")
    public String robotName;

    public String chatroomId;

    public String wxid;

    @Value("${api.appFilePath}")
    public String appFilePath;
}
