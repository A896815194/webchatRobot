package com.web.webchat.service;

import com.web.webchat.abstractclass.ChatBase;
import com.web.webchat.config.PropertiesEntity;
import com.web.webchat.dto.RequestDto;
import com.web.webchat.enums.FunctionType;
import com.web.webchat.init.SystemInit;
import com.web.webchat.inteface.Handler;
import com.web.webchat.repository.FunctionRoleRepository;
import com.web.webchat.strategyContext.TuLingRobotChat;
import com.web.webchat.verifiaction.EventFriendMsgVerification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

import static java.util.Objects.isNull;


@Service("EventFriendMsg")
public class EventFriendMsgService extends ChatBase {

    @Autowired
    private PropertiesEntity propertiesEntity;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private FunctionRoleRepository functionRoleRepository;

    public void sendMessageToWechat(RequestDto request) {
        if (new EventFriendMsgVerification().hasOpen(request, FunctionType.TuLingRobot.name(), 1)) {
            if ("tiaotiaoxiaoshuai".equals(request.getRobot_wxid())) {
                if (isAutoSend(request.getMsg()) && isAdmin(request.getTo_wxid(), request.getRobot_wxid())) {
                    SystemInit.flag = true;
                }
                if (isCancelAuto(request.getMsg()) && isAdmin(request.getTo_wxid(), request.getRobot_wxid())) {
                    SystemInit.flag = false;
                }
                if (SystemInit.flag) {
                    new TuLingRobotChat(FunctionType.TuLingRobot.name()).chat(request, propertiesEntity);
                }

            } else {
                new TuLingRobotChat(FunctionType.TuLingRobot.name()).chat(request, propertiesEntity);
            }
        }
    }

    @Override
    public boolean beforeSendMessageToWechat(RequestDto request, Handler handler) {
        long currentTime = System.currentTimeMillis();
        Long interVal = propertiesEntity.getReplyInterval();
        if (isNull(SystemInit.lastRequestMap.get(request.getRobot_wxid()))) {
            return true;
        }
        RequestDto lastRequest = SystemInit.lastRequestMap.get(request.getRobot_wxid());
        if (Objects.equals(request.getFrom_wxid(), lastRequest.getFrom_wxid()) &&
                currentTime - lastRequest.getTimeStamp() < interVal
        ) {
            return false;
        }
        return true;
    }

    @Override
    public void afterSendMessageToWechat(RequestDto request) {
        SystemInit.lastRequestMap.put(request.getRobot_wxid(), request);
    }

    private boolean isAdmin(String fromWixd, String adminWixd) {
        return Objects.equals(fromWixd, adminWixd);
    }

    private boolean isAutoSend(String msg) {
        return Objects.equals(msg, "托管");
    }

    private boolean isCancelAuto(String msg) {
        return Objects.equals(msg, "取消托管");
    }

}
