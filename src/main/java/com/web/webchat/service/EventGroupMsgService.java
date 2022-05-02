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
import com.web.webchat.util.BaiduAsrMainUtil;
import com.web.webchat.util.RestTemplateUtil;
import com.web.webchat.util.VoiceDecoderUtil;
import com.web.webchat.util.WeChatUtil;
import com.web.webchat.verifiaction.EventGroupMsgVerification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.Optional;

import static com.web.webchat.init.SystemInit.functionRoleRole;
import static java.util.Objects.isNull;


@Service("EventGroupMsg")
public class EventGroupMsgService extends ChatBase implements Command {

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
                    RestTemplateUtil.sendMsgToWeChat(WeChatUtil.handleResponse(request, ApiType.SendTextMsg.name()), propertiesEntity.getWechatUrl());
                    return;
                }
            }
            if (isAtRobot(request.getMsg(), request.getRobot_wxid())) {
                request.setMsg(sendMsg(request.getMsg(), request.getRobot_wxid()));
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


    private boolean isAtRobot(String msg, String robotId) {
        return msg.startsWith("[@at,nickname=Robot,wxid=" + robotId + "]") &&
                isOneAtRobot(msg, robotId);
    }

    private boolean isOneAtRobot(String msg, String robotId) {
        int index = msg.indexOf("]");
        String msg1 = msg.substring(index);
        return !msg1.contains("@at,nickname=Robot,wxid=");
    }

    private String sendMsg(String msg, String robotId) {
        return msg.split(robotId)[1].split("]")[1].trim();
    }

    @Override
    public boolean open(RequestDto request) {
        if ("开启群聊".equals(request.getMsg()) && "tiaotiaoxiaoshuai".equals(request.getFinal_from_wxid())) {
            if (new EventGroupMsgVerification().hasOpen(request, FunctionType.TuLingRobot.name(), 1)) {
                return true;
            }
            Example<FunctionRoleEntity> function = Example.of(FunctionRoleEntity.builder()
                    .functionType("TuLingRobot")
                    .chatType("EventGroupMsg")
                    .chatroomId(request.getFrom_wxid())
                    .robotId(request.getRobot_wxid()).build());

            Optional<FunctionRoleEntity> dataSource = Optional.ofNullable(functionRoleRepository.findOne(function)).orElse(null);
            if (dataSource.isPresent()) {
                if (1 == dataSource.get().getIsOpen()) {
                    return true;
                }
                FunctionRoleEntity saveFunction = FunctionRoleEntity.builder()
                        .functionType("TuLingRobot")
                        .chatType("EventGroupMsg")
                        .chatroomId(request.getFrom_wxid())
                        .isOpen(1)
                        .robotId(request.getRobot_wxid()).build();
                if (0 == dataSource.get().getIsOpen()) {
                    saveFunction.setId(dataSource.get().getId());
                }
                functionRoleRepository.save(saveFunction);
                functionRoleRole = functionRoleRepository.findAllByIsOpen(1);
                request.setMsg("开启群聊成功！");
                RestTemplateUtil.sendMsgToWeChat(WeChatUtil.handleResponse(request, ApiType.SendTextMsg.name()), propertiesEntity.getWechatUrl());
                return true;
            }
            FunctionRoleEntity saveFunction = FunctionRoleEntity.builder()
                    .functionType("TuLingRobot")
                    .chatType("EventGroupMsg")
                    .chatroomId(request.getFrom_wxid())
                    .isOpen(1)
                    .robotId(request.getRobot_wxid()).build();
            functionRoleRepository.save(saveFunction);
            functionRoleRole = functionRoleRepository.findAllByIsOpen(1);
            request.setMsg("开启群聊成功！");
            RestTemplateUtil.sendMsgToWeChat(WeChatUtil.handleResponse(request, ApiType.SendTextMsg.name()), propertiesEntity.getWechatUrl());
            return true;
        }
        return false;
    }

    @Override
    public boolean close(RequestDto request) {
        if ("关闭群聊".equals(request.getMsg()) && "tiaotiaoxiaoshuai".equals(request.getFinal_from_wxid())) {
            if (new EventGroupMsgVerification().hasOpen(request, FunctionType.TuLingRobot.name(), 1)) {

                Example<FunctionRoleEntity> function = Example.of(FunctionRoleEntity.builder()
                        .functionType("TuLingRobot")
                        .chatType("EventGroupMsg")
                        .chatroomId(request.getFrom_wxid())
                        .robotId(request.getRobot_wxid()).build());

                Optional<FunctionRoleEntity> dataSource = Optional.ofNullable(functionRoleRepository.findOne(function)).orElse(null);
                if (dataSource.isPresent()) {
                    if (0 == dataSource.get().getIsOpen()) {
                        return true;
                    }
                    FunctionRoleEntity saveFunction = FunctionRoleEntity.builder()
                            .functionType("TuLingRobot")
                            .chatType("EventGroupMsg")
                            .chatroomId(request.getFrom_wxid())
                            .isOpen(0)
                            .robotId(request.getRobot_wxid()).build();
                    if (1 == dataSource.get().getIsOpen()) {
                        saveFunction.setId(dataSource.get().getId());
                    }
                    functionRoleRepository.save(saveFunction);
                    functionRoleRole = functionRoleRepository.findAllByIsOpen(1);
                    request.setMsg("关闭群聊成功！");
                    RestTemplateUtil.sendMsgToWeChat(WeChatUtil.handleResponse(request, ApiType.SendTextMsg.name()), propertiesEntity.getWechatUrl());
                    return true;
                }
                FunctionRoleEntity saveFunction = FunctionRoleEntity.builder()
                        .functionType("TuLingRobot")
                        .chatType("EventGroupMsg")
                        .chatroomId(request.getFrom_wxid())
                        .isOpen(0)
                        .robotId(request.getRobot_wxid()).build();
                functionRoleRepository.save(saveFunction);
                functionRoleRole = functionRoleRepository.findAllByIsOpen(1);
                request.setMsg("关闭群聊成功！");
                RestTemplateUtil.sendMsgToWeChat(WeChatUtil.handleResponse(request, ApiType.SendTextMsg.name()), propertiesEntity.getWechatUrl());
                return true;
            }
        }
        return new EventGroupMsgVerification().hasOpen(request, FunctionType.TuLingRobot.name(), 0);
    }

    private boolean isHighFrequency(RequestDto request) {
        long currentTime = System.currentTimeMillis();
        Long interVal = propertiesEntity.getReplyInterval();
        if (isNull(SystemInit.lastRequestMap.get(request.getRobot_wxid()))) {
            return false;
        }
        RequestDto lastRequest = SystemInit.lastRequestMap.get(request.getRobot_wxid());
        if (isAtRobot(request.getMsg(), request.getRobot_wxid()) &&
                Objects.equals(request.getFrom_wxid(), lastRequest.getFrom_wxid()) &&
                currentTime - lastRequest.getTimeStamp() < interVal
        ) {
            return true;
        }
        return false;
    }

}
