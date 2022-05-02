package com.web.webchat.enums;

/**
 * 微信api的类型
 *
 * **/
public enum ApiType {

    SendTextMsg("发送文本消息"),

    SendImageMsg("发送图片消息"),

    SendVideoMsg("发送视频消息"),

    SendFileMsg("发送文件消息"),

    SendCardMsg("发送名片消息"),

    SendGroupMsgAndAt("发送群消息并艾特"),

    SendEmojiMsg("发送动态表情"),

    SendLinkMsg("发送分享链接"),

    SendMusicMsg("发送音乐分享"),

    GetRobotName("取登录账号昵称"),

    GetRobotHeadimgurl("取登录账号头像"),

    GetLoggedAccountList("取登录账号列表"),

    GetFriendList("取好友列表"),

    GetGroupList("取群聊列表"),

    GetGroupMemberDetailInfo("取群成员详细"),

    GetGroupMemberList("取群成员列表"),

    AcceptTransfer("接收好友转账"),

    AgreeGroupInvite("同意群聊邀请"),

    AgreeFriendVerify("同意好友请求"),

    ModifyFriendNote("修改好友备注"),

    DeleteFriend("删除好友"),

    GetAppDirectory("取应用目录"),

    AppendLogs("添加日志"),

    RemoveGroupMember("踢出群成员"),

    ModifyGroupName("修改群名称"),

    ModifyGroupNotice("修改群公告"),

    BuildingGroup("建立新群"),

    QuitGroup("退出群聊"),

    InviteInGroup("邀请加入群聊");

    private String text;

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
