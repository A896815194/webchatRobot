package com.web.webchat.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.web.webchat.dto.KunPengRequestDto;
import com.web.webchat.dto.RequestDto;
import com.web.webchat.enums.PushEvent;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

public class KunpengToLoveCatUtil {
    // "event_type": 1010,加群，"event_type": 1009,离开群里 "event_type": 1100,"同步消息",
    // 信息类型 1 文本 34 语音  3 图片 43视频 10000加群
    // 1/文本消息 3/图片消息 34/语音消息  42/名片消息  43/视频 47/动态表情 48/地理位置  49/分享链接  2001/红包  2002/小程序  2003/群邀请


    public static RequestDto kpConverCat(KunPengRequestDto kpDto) {
        RequestDto dto = new RequestDto();
        // 同步消息
        if(Objects.equals(kpDto.getEvent_type(),"1100")){
           KunPengRequestDto.Data data = kpDto.getData();
           if(Objects.equals(data.getFrom_wxid(),data.getFinal_from_wxid())){
               dto.setEvent(PushEvent.EventFriendMsg);
           }else{
               dto.setEvent(PushEvent.EventGroupMsg);
               dto.setAtuserlists(data.getAtuserlists());
               String jsonString = new Gson().toJson(data.getAtuserlists());
               dto.setAtUserString(jsonString);
           }
            fillMainData(kpDto, dto, data);
        }else{
            dto.setEvent(PushEvent.NoHandle);
        }
        return dto;
    }

    private static void fillMainData(KunPengRequestDto kpDto, RequestDto dto, KunPengRequestDto.Data data) {
        dto.setType(data.getMsgtype());
        dto.setMsg(data.getContent());
        dto.setFrom_wxid(data.getFrom_wxid());
        dto.setFinal_from_name(data.getFinal_displayname());
        dto.setFinal_from_wxid(data.getFinal_from_wxid());
        dto.setFrom_name(data.getFrom_name());
        dto.setTo_wxid(data.getTo_wxid());
        dto.setRobot_wxid(kpDto.getAccount_wxid());
        dto.setFilepath(data.getFilepath());
    }
}
