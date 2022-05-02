package com.web.webchat.service;

import com.web.webchat.abstractclass.ChatBase;
import com.web.webchat.config.PropertiesEntity;
import com.web.webchat.dto.RequestDto;
import com.web.webchat.entity.FunctionRoleEntity;
import com.web.webchat.enums.ApiType;
import com.web.webchat.enums.FunctionType;
import com.web.webchat.init.SystemInit;
import com.web.webchat.inteface.Command;
import com.web.webchat.inteface.Handler;
import com.web.webchat.repository.FunctionRoleRepository;
import com.web.webchat.strategyContext.TuLingRobotChat;
import com.web.webchat.util.RestTemplateUtil;
import com.web.webchat.util.WeChatUtil;
import com.web.webchat.verifiaction.EventFriendMsgVerification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.Optional;

import static com.web.webchat.init.SystemInit.functionRoleRole;
import static java.util.Objects.isNull;


@Service("EventFriendMsg")
public class EventFriendMsgService extends ChatBase implements Command {

    @Autowired
    private PropertiesEntity propertiesEntity;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private FunctionRoleRepository functionRoleRepository;

    public void sendMessageToWechat(RequestDto request) {
        if (new EventFriendMsgVerification().hasOpen(request, FunctionType.TuLingRobot.name(),1)) {
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


    @Override
    public boolean open(RequestDto request) {
        if (("开启" + FunctionType.SendMsgToFriend.getText()).equals(request.getMsg())
                && "tiaotiaoxiaoshuai".equals(request.getFrom_wxid())) {
            if (new EventFriendMsgVerification().hasOpen(request, null,1)) {
                return true;
            }
            Example<FunctionRoleEntity> function = Example.of(FunctionRoleEntity.builder()
                    .functionType("TuLingRobot")
                    .chatType("EventFriendMsg")
                    .chatroomId("")
                    .robotId(request.getRobot_wxid()).build());

            Optional<FunctionRoleEntity> dataSource = Optional.ofNullable(functionRoleRepository.findOne(function)).orElse(null);
            if (dataSource.isPresent()) {
                if (1 == dataSource.get().getIsOpen()) {
                    return true;
                }
                FunctionRoleEntity saveFunction = FunctionRoleEntity.builder()
                        .functionType("TuLingRobot")
                        .chatType("EventFriendMsg")
                        .chatroomId("")
                        .isOpen(1)
                        .robotId(request.getRobot_wxid()).build();
                if (0 == dataSource.get().getIsOpen()) {
                    saveFunction.setId(dataSource.get().getId());
                }
                functionRoleRepository.save(saveFunction);
                functionRoleRole = functionRoleRepository.findAllByIsOpen(1);
                request.setMsg("开启私聊成功！");
                RestTemplateUtil.sendMsgToWeChat(WeChatUtil.handleResponse(request, ApiType.SendTextMsg.name()), propertiesEntity.getWechatUrl());
                return true;
            }
            FunctionRoleEntity saveFunction = functionRoleRepository.save(FunctionRoleEntity.builder()
                    .functionType("TuLingRobot")
                    .chatType("EventFriendMsg")
                    .chatroomId("")
                    .isOpen(1)
                    .robotId(request.getRobot_wxid()).build());
            functionRoleRole = functionRoleRepository.findAllByIsOpen(1);
            request.setMsg("开启私聊成功！");
            RestTemplateUtil.sendMsgToWeChat(WeChatUtil.handleResponse(request, ApiType.SendTextMsg.name()), propertiesEntity.getWechatUrl());
            return true;
        }
        return false;
    }

    @Override
    public boolean close(RequestDto request) {
        if (("关闭" + FunctionType.SendMsgToFriend.getText()).equals(request.getMsg()) && "tiaotiaoxiaoshuai".equals(request.getFrom_wxid())) {
            if (new EventFriendMsgVerification().hasOpen(request, null,1)) {

                Example<FunctionRoleEntity> function = Example.of(FunctionRoleEntity.builder()
                        .functionType("TuLingRobot")
                        .chatType("EventFriendMsg")
                        .chatroomId("")
                        .robotId(request.getRobot_wxid()).build());

                Optional<FunctionRoleEntity> dataSource = Optional.ofNullable(functionRoleRepository.findOne(function)).orElse(null);
                if (dataSource.isPresent()) {
                    if (0 == dataSource.get().getIsOpen()) {
                        return true;
                    }
                    FunctionRoleEntity saveFunction = FunctionRoleEntity.builder()
                            .functionType("TuLingRobot")
                            .chatType("EventFriendMsg")
                            .chatroomId("")
                            .isOpen(0)
                            .robotId(request.getRobot_wxid()).build();
                    if (1 == dataSource.get().getIsOpen()) {
                        saveFunction.setId(dataSource.get().getId());
                    }
                    functionRoleRepository.save(saveFunction);
                    functionRoleRole = functionRoleRepository.findAllByIsOpen(1);
                    request.setMsg("关闭私聊成功！");
                    RestTemplateUtil.sendMsgToWeChat(WeChatUtil.handleResponse(request, ApiType.SendTextMsg.name()), propertiesEntity.getWechatUrl());
                    return true;
                }
                FunctionRoleEntity saveFunction = functionRoleRepository.save(FunctionRoleEntity.builder()
                        .functionType("TuLingRobot")
                        .chatType("EventFriendMsg")
                        .chatroomId("")
                        .isOpen(0)
                        .robotId(request.getRobot_wxid()).build());
                functionRoleRole = functionRoleRepository.findAllByIsOpen(1);
                request.setMsg("关闭私聊成功！");
                RestTemplateUtil.sendMsgToWeChat(WeChatUtil.handleResponse(request, ApiType.SendTextMsg.name()), propertiesEntity.getWechatUrl());
                return true;
            }
        }
        return new EventFriendMsgVerification().hasOpen(request, null,0);
    }
}
