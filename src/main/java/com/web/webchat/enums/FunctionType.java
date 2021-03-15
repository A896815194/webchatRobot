package com.web.webchat.enums;

public enum FunctionType {

    SendMsgToFriend("私聊"),

    sendMsgToGroup("群聊"),

    TextReview("文本文档内容审核"),

    TuLingRobot("图灵机器人聊天"),

    ASR("语音识别"),

    SINGNIN("群签到");

    private String text;

    FunctionType(String text) {
        this.text = text;
    }


    public String getText() {
        return text;
    }


    public String getValue() {
        return String.valueOf(this.ordinal());
    }
}
