package com.web.webchat.enums;


/**
 * 微信接收时间的类型
 *
 * **/
public enum PushEvent {
    EventLogin("新的账号登录成功/下线"),

    EventGroupMsg("群消息事件"),

    EventFriendMsg("私聊消息事件"),

    EventReceivedTransfer("收到转账事件"),

    EventScanCashMoney("面对面收款（二维码收款时，运行这里）"),

    EventFriendVerify("好友请求事件"),

    EventGroupMemberAdd("群成员增加事件"),

    EventGroupMemberDecrease("群成员减少事件"),

    EventSysMsg("用户设置或调试消息"),

    EventModify("用户设置或调试消息"),

    EventContactsChange("朋友变动事件"),

    ;

    private String text;

    PushEvent(String text) {
        this.text = text;
    }


    public String getText() {
        return text;
    }


    public String getValue() {
        return String.valueOf(this.ordinal());
    }
}
