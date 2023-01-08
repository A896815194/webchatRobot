package com.web.webchat.enums;

/**
 * 微信api的类型
 *
 * **/
public enum ApiType {

    SendTextMsg("K10033","发送文本消息"),

    SendImageMsg("K10034","发送图片消息"),

    SendVideoMsg("K10033","发送视频消息"),

    SendFileMsg("K10035","发送文件消息"),

    SendCardMsg("K10038","发送名片消息"),

    SendGroupMsgAndAt("K10037","发送群消息并艾特");

//    SendEmojiMsg("K10036","发送动态表情"),
//
//    SendLinkMsg("K10039","发送分享链接"),
//
//    SendMusicMsg("K10033","发送音乐分享"),
//
//    GetRobotName("K10033","取登录账号昵称"),
//
//    GetRobotHeadimgurl("K10033","取登录账号头像"),
//
//    GetLoggedAccountList("K10033","取登录账号列表"),
//
//    GetFriendList("K10033","取好友列表"),
//
//    GetGroupList("K10033","取群聊列表"),
//
//    GetGroupMemberDetailInfo("K10033","取群成员详细"),
//
//    GetGroupMemberList("K10033","取群成员列表"),
//
//    AcceptTransfer("K10033","接收好友转账"),
//
//    AgreeGroupInvite("K10033","同意群聊邀请"),
//
//    AgreeFriendVerify("K10033","同意好友请求"),
//
//    ModifyFriendNote("K10033","修改好友备注"),
//
//    DeleteFriend("K10033","删除好友"),
//
//    GetAppDirectory("K10033","取应用目录"),
//
//    AppendLogs("K10033","添加日志"),
//
//    RemoveGroupMember("K10033","踢出群成员"),
//
//    ModifyGroupName("K10033","修改群名称"),
//
//    ModifyGroupNotice("K10033","修改群公告"),
//
//    BuildingGroup("K10033","建立新群"),
//
//    QuitGroup("K10033","退出群聊"),
//
//    InviteInGroup("K10033","邀请加入群聊");

    private String name;

    private String text;

    ApiType(String name, String text) {
        this.name = name;
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    ApiType(String text) {
        this.text = text;
    }


    public String getText() {
        return text;
    }


    public String getValue() {
        return String.valueOf(this.ordinal());
    }
}
