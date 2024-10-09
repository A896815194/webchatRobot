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

    @Value("${api.tuling-api:null}")
    public String tulingApi;

    @Value("${api.tuling-api-key:null}")
    public String tulingApiKey;

    @Value("${api.tuling-user-id:null}")
    public String tulingUserId;

    @Value("${api.wechat-url:null}")
    public String wechatUrl;

    @Value("${api.Baidu-text-review:null}")
    public String BaiduTextReviewUrl;

    @Value("${api.source-voice-root-path:null}")
    public String sourceVoiceRootPath;

    @Value("${api.silk-v3-decoder-path:null}")
    public String silkV3Path;

    @Value("${api.ffmpeg-path:null}")
    public String ffmpegPath;

    @Value("${api.reply-interval:null}")
    public Long replyInterval;

    @Value("${api.robotId:null}")
    public String robotId;

    @Value("${api.robotName:null}")
    public String robotName;

    public String chatroomId;

    public String wxid;

    @Value("${api.appFilePath:null}")
    public String appFilePath;

    @Value("${api.minior.liveId:null}")
    public String liveId;

    @Value("${api.minior.zbId:null}")
    public String zbId;

    @Value("${api.minior.danmuOpen:false}")
    public Boolean dmOpen;

    @Value("${api.minior.pythonFilePath}")
    public String pythonScriptPath;

    @Value("${api.minior.managerDouYId}")
    public String managerDouYIds;

    @Value("${api.minior.notifyUrl}")
    public String notifyUrl;

    @Value("${api.minior.signJsPath}")
    public String signJsPath;

    @Value("${api.minior.nodeModulesPath}")
    public String nodeModulesPath;

    @Value("${api.minior.castLogPath}")
    public String castLogPath;
}
