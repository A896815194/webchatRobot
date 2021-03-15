package com.web.webchat.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto {

    private String api;
    //文本型, , 机器人账号id（就是这条消息是哪个机器人的，因为可能登录多个机器人）
    private String robot_wxid;
    //整数型, , 1/文本消息 3/图片消息 34/语音消息  42/名片消息  43/视频 47/动态表情 48/地理位置  49/分享链接  2001/红包  2002/小程序  2003/群邀请 更多请参考sdk模块常量值
    private Integer type;
     // 文本型, , 来源群id
    private String from_wxid;
    //文本型, , 来源群昵称
    private String from_name;
    // 文本型, , 具体发消息的群成员id
    private String final_from_wxid;
    //文本型, , 具体发消息的群成员昵称
    private String final_from_name;
    // 文本型, , 接收消息的人id，（一般是机器人收到了，也有可能是机器人发出的消息，别人收到了，那就是别人）
    private String to_wxid;
    //文本型, , 消息内容
    private String msg;
    //url
    private String path;

    @Override
    public String toString() {
        return "ResponseDto{" +
                "api='" + api + '\'' +
                ", robot_wxid='" + robot_wxid + '\'' +
                ", type=" + type +
                ", from_wxid='" + from_wxid + '\'' +
                ", from_name='" + from_name + '\'' +
                ", final_from_wxid='" + final_from_wxid + '\'' +
                ", final_from_name='" + final_from_name + '\'' +
                ", to_wxid='" + to_wxid + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
