package com.web.webchat.service;

import com.web.webchat.abstractclass.ChatBase;
import com.web.webchat.config.PropertiesEntity;
import com.web.webchat.dto.KunPengRequestDto;
import com.web.webchat.dto.RequestDto;
import com.web.webchat.enums.ApiType;
import com.web.webchat.enums.FunctionType;
import com.web.webchat.init.SystemInit;
import com.web.webchat.inteface.Handler;
import com.web.webchat.repository.FunctionRoleRepository;
import com.web.webchat.strategyContext.TuLingRobotChat;
import com.web.webchat.util.BaiduAsrMainUtil;
import com.web.webchat.util.RestTemplateUtil;
import com.web.webchat.util.VoiceDecoderUtil;
import com.web.webchat.util.WeChatUtil;
import com.web.webchat.verifiaction.EventGroupMsgVerification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

import static java.util.Objects.isNull;


@Service("EventGroupMsg")
public class EventGroupMsgService extends ChatBase {

    @Autowired
    private PropertiesEntity propertiesEntity;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private FunctionRoleRepository functionRoleRepository;
    // RequestDto{api='null', robot_wxid='wxid_i5vabkq7vwb222', from_wxid='18955225703@chatroom', to_wxid='wxid_i5vabkq7vwb222', msg='6', Event='EventGroupMsg'}

    public void sendMessageToWechat(RequestDto request) {
        if (new EventGroupMsgVerification().hasOpen(request, FunctionType.TuLingRobot.name(), 1)) {
            if (request.getMsg().contains(".silk")) {
                if (34 == request.getType()) {
                    String pcmUrl = VoiceDecoderUtil.silkToPcm(request.getMsg(), propertiesEntity.getSourceVoiceRootPath() + "/" + request.getFrom_wxid() + "/", String.valueOf(System.currentTimeMillis()), propertiesEntity.getSilkV3Path());
                    String msg = BaiduAsrMainUtil.getMsgFromPcm(pcmUrl);
                    request.setMsg("语音翻译:" + msg);
                    RestTemplateUtil.sendMsgToWeChatSync(WeChatUtil.handleResponse(request, ApiType.SendTextMsg), propertiesEntity.getWechatUrl());
                    return;
                }
            }
            if (isOneAtRobot(request)) {
                request.setMsg(sendMsg(request.getMsg(), request.getFrom_name()));
                new TuLingRobotChat(FunctionType.TuLingRobot.name()).chat(request, propertiesEntity);
            }
        }
    }

    @Override
    public boolean beforeSendMessageToWechat(RequestDto request, Handler handler) {
        if (isHighFrequency(request)) {
            return false;
        }
        if (34 == request.getType()) {
            return true;
        }
        return true;
    }


    @Override
    public void afterSendMessageToWechat(RequestDto request) {
        SystemInit.lastRequestMap.put(request.getRobot_wxid(), request);
    }


    private boolean isAtRobot(RequestDto request) {
        if (!CollectionUtils.isEmpty(request.getAtuserlists())) {
            List<KunPengRequestDto.AtUser> atUsers = request.getAtuserlists();
            if (atUsers.stream().anyMatch(item -> Objects.equals(item.getWxid(), request.getRobot_wxid()))) {
                return true;
            }
        }
        return false;
    }

//    private boolean isAtRobotOld(String msg, String robotId) {
//        return msg.startsWith("[@at,nickname=Robot,wxid=" + robotId + "]") &&
//                isOneAtRobot(msg);
//    }

    private boolean isOneAtRobot(RequestDto request) {
        if (!CollectionUtils.isEmpty(request.getAtuserlists())) {
            List<KunPengRequestDto.AtUser> atUsers = request.getAtuserlists();
            return atUsers.stream().allMatch(item -> Objects.equals(item.getWxid(), request.getRobot_wxid()));
        }
        return false;
    }

    private boolean isOneAtRobotOld(String msg) {
        int index = msg.indexOf("]");
        String msg1 = msg.substring(index);
        return !msg1.contains("@at,nickname=Robot,wxid=");
    }

    private String sendMsg(String msg, String robotId) {
        return msg.split(robotId)[1].split("]")[1].trim();
    }


    private boolean isHighFrequency(RequestDto request) {
        long currentTime = System.currentTimeMillis();
        Long interVal = propertiesEntity.getReplyInterval();
        if (isNull(SystemInit.lastRequestMap.get(request.getRobot_wxid()))) {
            return false;
        }
        RequestDto lastRequest = SystemInit.lastRequestMap.get(request.getRobot_wxid());
        if (isOneAtRobot(request) &&
                Objects.equals(request.getFrom_wxid(), lastRequest.getFrom_wxid()) &&
                currentTime - lastRequest.getTimeStamp() < interVal
        ) {
            return true;
        }
        return false;
    }

}
