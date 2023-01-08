package com.web.webchat.dto;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KunPengRequestDto {
    // "event_type": 1010,加群，"event_type": 1009,离开群里 "event_type": 1100,"同步消息",
    private String event_type;
    private String event_desc;
    private String account_wxid;
    private Data data;

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data {
        private String SvrId;
        private Long LocalId;
        private String PrefixId;
        private Long createtime;
        // 信息类型 1 文本 34 语音  3 图片 43视频 10000加群
        private Integer msgtype;
        private Integer subtype;
        private Integer isSender;
        // 内容
        private String content;
        // 来自微信/群
        private String from_wxid;
        // 来自微信名称
        private String from_name;

        private String final_from_wxid;

        private String final_from_name;
        //显示名称
        private String final_displayname;
        // 发给谁/发给群
        private String to_wxid;

        private String to_name;

        private String filepath;

        private List<AtUser> atuserlists;

        private Object atuserlist;

        private String roomid;

        private String roomname;

        private Integer membercount;

        private List<MemberDto> memberlist;

    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberDto {
        // 加入群里时微信id
        private String userName;
        //加入
        private String nickName;
        // 离开
        private String nickname;
        // 离开群时
        private String wxid;


        private String bigHeadImgUrl;

        private String smallHeadImgUrl;

        private Integer chatroomMemberFlag;
        // 邀请wxid
        private String inviterUserName;
        // 邀请name
        private String inviterNickName;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getWxid() {
            return wxid;
        }

        public void setWxid(String wxid) {
            this.wxid = wxid;
        }

        public String getBigHeadImgUrl() {
            return bigHeadImgUrl;
        }

        public void setBigHeadImgUrl(String bigHeadImgUrl) {
            this.bigHeadImgUrl = bigHeadImgUrl;
        }

        public String getSmallHeadImgUrl() {
            return smallHeadImgUrl;
        }

        public void setSmallHeadImgUrl(String smallHeadImgUrl) {
            this.smallHeadImgUrl = smallHeadImgUrl;
        }

        public Integer getChatroomMemberFlag() {
            return chatroomMemberFlag;
        }

        public void setChatroomMemberFlag(Integer chatroomMemberFlag) {
            this.chatroomMemberFlag = chatroomMemberFlag;
        }

        public String getInviterUserName() {
            return inviterUserName;
        }

        public void setInviterUserName(String inviterUserName) {
            this.inviterUserName = inviterUserName;
        }

        public String getInviterNickName() {
            return inviterNickName;
        }

        public void setInviterNickName(String inviterNickName) {
            this.inviterNickName = inviterNickName;
        }
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AtUser {
        // notify@all 所有人
        private String wxid;
        private String nickname;
    }

    public static KunPengRequestDto build(KunPengRequestDto dto) {
        if (dto.getData() != null && !(dto.getData().getAtuserlist() instanceof String)) {
            String jsonString = new Gson().toJson(dto.getData().getAtuserlist());
            Type listType = new TypeToken<ArrayList<AtUser>>(){}.getType();
            dto.getData().setAtuserlists(new Gson().fromJson(jsonString, listType));
        }
        return dto;
    }
}
