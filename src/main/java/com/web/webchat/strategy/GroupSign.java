package com.web.webchat.strategy;

import com.web.webchat.config.PropertiesEntity;
import com.web.webchat.dto.RequestDto;
import com.web.webchat.entity.FunctionRoleEntity;
import com.web.webchat.enums.ApiType;
import com.web.webchat.enums.FunctionType;
import com.web.webchat.inteface.Command;
import com.web.webchat.inteface.Handler;
import com.web.webchat.repository.FunctionRoleRepository;
import com.web.webchat.util.RestTemplateUtil;
import com.web.webchat.util.StrategyFactory;
import com.web.webchat.util.WeChatUtil;
import com.web.webchat.verifiaction.EventGroupMsgVerification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.web.webchat.init.SystemInit.functionRoleRole;

@Component
public class GroupSign implements Handler, Command {

    @Autowired
    private PropertiesEntity properties;

    @Autowired
    private FunctionRoleRepository functionRoleRepository;

    @Override
    public List<String> createMessage(RequestDto request, PropertiesEntity propertiesEntity) {
        if (open(request)) {
            return null;
        }
        if (close(request)) {
            return null;
        }
        List<String> result = new ArrayList<>();
        result.add("test");
        return result;
    }

    @Override
    public void afterPropertiesSet() {
        StrategyFactory.register(FunctionType.SignIn.name(), this);
    }


    @Override
    public boolean open(RequestDto request) {
        if ("开启签到".equals(request.getMsg()) && "tiaotiaoxiaoshuai".equals(request.getFinal_from_wxid())) {
            if (new EventGroupMsgVerification().hasOpen(request, FunctionType.SignIn.name(), 1)) {
                return true;
            }
            Example<FunctionRoleEntity> function = Example.of(FunctionRoleEntity.builder()
                    .functionType(FunctionType.SignIn.name())
                    .functionName(FunctionType.SignIn.getText())
                    .chatType("EventGroupMsg")
                    .chatroomId(request.getFrom_wxid())
                    .robotId(request.getRobot_wxid()).build());

            Optional<FunctionRoleEntity> dataSource = Optional.of(functionRoleRepository.findOne(function)).orElse(null);
            if (dataSource.isPresent()) {
                if (1 == dataSource.get().getIsOpen()) {
                    return true;
                }
                FunctionRoleEntity saveFunction = FunctionRoleEntity.builder()
                        .functionType(FunctionType.SignIn.name())
                        .functionName(FunctionType.SignIn.getText())
                        .chatType("EventGroupMsg")
                        .chatroomId(request.getFrom_wxid())
                        .isOpen(1)
                        .robotId(request.getRobot_wxid()).build();
                if (0 == dataSource.get().getIsOpen()) {
                    saveFunction.setId(dataSource.get().getId());
                }
                functionRoleRepository.save(saveFunction);
                functionRoleRole = functionRoleRepository.findAllByIsOpen(1);
                request.setMsg("开启签到成功！");
                RestTemplateUtil.sendMsgToWeChat(WeChatUtil.handleResponse(request, ApiType.SendTextMsg.name()), properties.getWechatUrl());
                return true;
            }
            FunctionRoleEntity saveFunction = FunctionRoleEntity.builder()
                    .functionType(FunctionType.SignIn.name())
                    .functionName(FunctionType.SignIn.getText())
                    .chatType("EventGroupMsg")
                    .chatroomId(request.getFrom_wxid())
                    .isOpen(1)
                    .robotId(request.getRobot_wxid()).build();
            functionRoleRepository.save(saveFunction);
            functionRoleRole = functionRoleRepository.findAllByIsOpen(1);
            request.setMsg("开启签到成功！");
            RestTemplateUtil.sendMsgToWeChat(WeChatUtil.handleResponse(request, ApiType.SendTextMsg.name()), properties.getWechatUrl());
            return true;
        }
        return false;
    }

    @Override
    public boolean close(RequestDto request) {
        if ("关闭签到".equals(request.getMsg()) && "tiaotiaoxiaoshuai".equals(request.getFinal_from_wxid())) {
            if (new EventGroupMsgVerification().hasOpen(request, FunctionType.SignIn.name(), 1)) {

                Example<FunctionRoleEntity> function = Example.of(FunctionRoleEntity.builder()
                        .functionType(FunctionType.SignIn.name())
                        .functionName(FunctionType.SignIn.getText())
                        .chatType("EventGroupMsg")
                        .chatroomId(request.getFrom_wxid())
                        .robotId(request.getRobot_wxid()).build());

                Optional<FunctionRoleEntity> dataSource = Optional.of(functionRoleRepository.findOne(function)).orElse(null);
                if (dataSource.isPresent()) {
                    if (0 == dataSource.get().getIsOpen()) {
                        return true;
                    }
                    FunctionRoleEntity saveFunction = FunctionRoleEntity.builder()
                            .functionType(FunctionType.SignIn.name())
                            .functionName(FunctionType.SignIn.getText())
                            .chatType("EventGroupMsg")
                            .chatroomId(request.getFrom_wxid())
                            .isOpen(0)
                            .robotId(request.getRobot_wxid()).build();
                    if (1 == dataSource.get().getIsOpen()) {
                        saveFunction.setId(dataSource.get().getId());
                    }
                    functionRoleRepository.save(saveFunction);
                    functionRoleRole = functionRoleRepository.findAllByIsOpen(1);
                    request.setMsg("关闭签到成功！");
                    RestTemplateUtil.sendMsgToWeChat(WeChatUtil.handleResponse(request, ApiType.SendTextMsg.name()), properties.getWechatUrl());
                    return true;
                }
                FunctionRoleEntity saveFunction = FunctionRoleEntity.builder()
                        .functionType(FunctionType.SignIn.name())
                        .functionName(FunctionType.SignIn.getText())
                        .chatType("EventGroupMsg")
                        .chatroomId(request.getFrom_wxid())
                        .isOpen(0)
                        .robotId(request.getRobot_wxid()).build();
                functionRoleRepository.save(saveFunction);
                functionRoleRole = functionRoleRepository.findAllByIsOpen(1);
                request.setMsg("关闭签到成功！");
                RestTemplateUtil.sendMsgToWeChat(WeChatUtil.handleResponse(request, ApiType.SendTextMsg.name()), properties.getWechatUrl());
                return true;
            }
        }
        return new EventGroupMsgVerification().hasOpen(request, FunctionType.SignIn.name(), 0);
    }
}
