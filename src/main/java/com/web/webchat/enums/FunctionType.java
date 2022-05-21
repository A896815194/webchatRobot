package com.web.webchat.enums;

import java.util.Objects;

/**
 * 所有功能类型
 **/
public enum FunctionType {

    SendMsgToFriend("私聊"),

    sendMsgToGroup("群聊"),

    TextReview("文本文档内容审核"),

    TuLingRobot("图灵机器人聊天"),

    ASR("语音识别"),

    SignIn("群签到"),

    MagicShop("魔法商城"),

    MagicWeather("魔法天气");

    private String text;

    FunctionType(String text) {
        this.text = text;
    }


    public String getText() {
        return text;
    }


    public static boolean isFuncationValue(String ml) {
        for (FunctionType value : values()) {
            if (Objects.equals(ml, value.getText())) {
                return true;
            }
        }
        return false;
    }

    public static String getFunctionTypeByMl(String ml) {
        for (FunctionType value : values()) {
            if (Objects.equals(ml, value.getText())) {
                return value.name();
            }
        }
        return "";
    }

}
