package com.web.webchat.util;

import com.google.gson.Gson;
import com.web.webchat.config.PropertiesEntity;
import com.web.webchat.dto.KunPengRequestDto;
import com.web.webchat.dto.RequestDto;
import com.web.webchat.dto.ResponseDto;
import com.web.webchat.enums.ApiType;
import com.web.webchat.enums.Message;
import com.web.webchat.enums.PushEvent;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class KunpengToLoveCatUtil {
    // "event_type": 1010,加群，"event_type": 1009,离开群里 "event_type": 1100,"同步消息",
    // 信息类型 1 文本 34 语音  3 图片 43视频 10000加群
    // 1/文本消息 3/图片消息 34/语音消息  42/名片消息  43/视频 47/动态表情 48/地理位置  49/分享链接  2001/红包  2002/小程序  2003/群邀请


    public static RequestDto kpConverCat(KunPengRequestDto kpDto, PropertiesEntity propertiesEntity) {
        RequestDto dto = new RequestDto();
        // 同步消息
        if (Objects.equals(kpDto.getEvent_type(), "1100")) {
            KunPengRequestDto.Data data = kpDto.getData();
            if (Objects.equals(data.getFrom_wxid(), data.getFinal_from_wxid())) {
                if(Objects.equals(10000,data.getMsgtype())){
                    dto.setEvent(PushEvent.NoHandle);
                    return dto;
                }
                dto.setEvent(PushEvent.EventFriendMsg);
            } else {
                dto.setEvent(PushEvent.EventGroupMsg);
                dto.setAtuserlists(data.getAtuserlists());
                String jsonString = new Gson().toJson(data.getAtuserlists());
                dto.setAtUserString(jsonString);
            }
            fillMainData(kpDto, dto, data);
            //加群
        } else if (Objects.equals(kpDto.getEvent_type(), "1010")) {
            KunPengRequestDto.Data data = kpDto.getData();
            if (CollectionUtils.isEmpty(data.getMemberlist())) {
                dto.setEvent(PushEvent.NoHandle);
                return dto;
            }
            dto.setEvent(PushEvent.EventGroupMsg);
            dto.setAtuserlists(data.getAtuserlists());
            String jsonString = new Gson().toJson(data.getAtuserlists());
            dto.setAtUserString(jsonString);
            String roomName = data.getRoomname();
            List<String> roomNames = data.getMemberlist().stream().map(KunPengRequestDto.MemberDto::getNickName).collect(Collectors.toList());
            String names = StringUtils.join(roomNames, "、");
            List<String> invates = data.getMemberlist().stream().map(KunPengRequestDto.MemberDto::getInviterNickName).collect(Collectors.toList());
            String invateNames = StringUtils.join(invates, "、");
            String message = String.format(Message.ADD_MEMBER, names, invateNames, roomName);
            dto.setMsg(message);
            dto.setType(1);
            dto.setFrom_wxid(data.getRoomid());
            dto.setFinal_from_name(data.getFrom_name());
            dto.setFinal_from_wxid(data.getFinal_from_wxid());
            dto.setFrom_name(data.getFrom_name());
            dto.setTo_wxid(data.getTo_wxid());
            dto.setRobot_wxid(kpDto.getAccount_wxid());
            dto.setFilepath(data.getFilepath());
            RestTemplateUtil.sendMsgToWeChatSync(WeChatUtil.handleResponse(dto, ApiType.SendTextMsg), propertiesEntity.getWechatUrl());
            dto.setEvent(PushEvent.NoHandle);
            return dto;
        }else if (Objects.equals(kpDto.getEvent_type(), "1009")) {
            KunPengRequestDto.Data data = kpDto.getData();
            if (CollectionUtils.isEmpty(data.getMemberlist())) {
                dto.setEvent(PushEvent.NoHandle);
                return dto;
            }
            dto.setEvent(PushEvent.EventGroupMsg);
            dto.setAtuserlists(data.getAtuserlists());
            String jsonString = new Gson().toJson(data.getAtuserlists());
            dto.setAtUserString(jsonString);
            List<String> roomNames = data.getMemberlist().stream().map(KunPengRequestDto.MemberDto::getNickname).collect(Collectors.toList());
            String names = StringUtils.join(roomNames, "、");
            String message = String.format(Message.DEADD_MEMBER, names);
            dto.setMsg(message);
            dto.setType(1);
            dto.setFrom_wxid(data.getRoomid());
            dto.setFinal_from_name(data.getFrom_name());
            dto.setFinal_from_wxid(data.getFinal_from_wxid());
            dto.setFrom_name(data.getFrom_name());
            dto.setTo_wxid(data.getTo_wxid());
            dto.setRobot_wxid(kpDto.getAccount_wxid());
            dto.setFilepath(data.getFilepath());
            RestTemplateUtil.sendMsgToWeChatSync(WeChatUtil.handleResponse(dto, ApiType.SendTextMsg), propertiesEntity.getWechatUrl());
            dto.setEvent(PushEvent.NoHandle);
            return dto;
        } else {
            dto.setEvent(PushEvent.NoHandle);
        }
        return dto;
    }

    private static void fillMainData(KunPengRequestDto kpDto, RequestDto dto, KunPengRequestDto.Data data) {
        dto.setType(data.getMsgtype());
        dto.setMsg(data.getContent());
        dto.setFrom_wxid(data.getFrom_wxid());
        dto.setFinal_from_name(data.getFrom_name());
        dto.setFinal_from_wxid(data.getFinal_from_wxid());
        dto.setFrom_name(data.getFrom_name());
        dto.setTo_wxid(data.getTo_wxid());
        dto.setRobot_wxid(kpDto.getAccount_wxid());
        dto.setFilepath(data.getFilepath());
    }
}
