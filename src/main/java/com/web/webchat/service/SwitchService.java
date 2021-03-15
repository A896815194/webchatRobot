package com.web.webchat.service;

import com.web.webchat.config.PropertiesEntity;
import com.web.webchat.dto.RequestDto;
import com.web.webchat.entity.FunctionRoleEntity;
import com.web.webchat.enums.ApiType;
import com.web.webchat.enums.FunctionType;
import com.web.webchat.inteface.Command;
import com.web.webchat.inteface.ServiceInt;
import com.web.webchat.repository.FunctionRoleRepository;
import com.web.webchat.util.RestTemplateUtil;
import com.web.webchat.util.WeChatUtil;
import com.web.webchat.verifiaction.EventFriendMsgVerification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.web.webchat.init.SystemInit.functionRoleRole;

@Service("FunctionSwitch")
public class SwitchService extends ServiceInt implements Command {

    @Autowired
    private PropertiesEntity propertiesEntity;

    @Autowired
    private FunctionRoleRepository functionRoleRepository;

    @Override
    public void sendMessageToWechat(RequestDto request) {
        if (open(request)) {
            return;
        }
        if (close(request)) {
            return;
        }
    }

    @Override
    public boolean beforeSendMessageToWechat(RequestDto request) {
        return true;
    }

    @Override
    public void afterSendMessageToWechat(RequestDto request) {

    }

    @Override
    public boolean open(RequestDto request) {
        if (("开启"+ FunctionType.SendMsgToFriend.name()).equals(request.getMsg()) && "tiaotiaoxiaoshuai".equals(request.getFrom_wxid())) {
            if (EventFriendMsgVerification.hasOpen(request,1)) {
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
        return false;
    }
}
