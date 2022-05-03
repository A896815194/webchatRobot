package com.web.webchat.abstractclass;


import com.google.gson.Gson;
import com.web.webchat.config.PropertiesEntity;
import com.web.webchat.dto.RequestDto;
import com.web.webchat.entity.FunctionRoleCommand;
import com.web.webchat.entity.FunctionRoleEntity;
import com.web.webchat.enums.ApiType;
import com.web.webchat.enums.FunctionType;
import com.web.webchat.enums.Message;
import com.web.webchat.init.SystemInit;
import com.web.webchat.inteface.Handler;
import com.web.webchat.repository.FunctionRoleRepository;
import com.web.webchat.util.*;
import com.web.webchat.verifiaction.EventFriendMsgVerification;
import com.web.webchat.verifiaction.EventGroupMsgVerification;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static com.web.webchat.init.SystemInit.functionRoleRole;
import static java.util.Objects.isNull;

@Component
public abstract class ChatBase {

    @Autowired
    private PropertiesEntity propertiesEntity;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private FunctionRoleRepository functionRoleRepository;
    @Autowired
    private ReflectionService reflectionService;

    // RequestDto{api='null', robot_wxid='wxid_i5vabkq7vwb222', from_wxid='18955225703@chatroom', to_wxid='wxid_i5vabkq7vwb222', msg='6', Event='EventGroupMsg'}
    public boolean getVerificationByType(String eventMsg, String functionType, RequestDto request, int open) {
        if (Objects.equals("EventGroupMsg", eventMsg)) {
            return new EventGroupMsgVerification().hasOpen(request, functionType, open);
        }
        if (Objects.equals("EventFriendMsg", eventMsg)) {
            return new EventFriendMsgVerification().hasOpen(request, functionType, open);
        }
        return false;
    }

    public boolean open(RequestDto request, String functionType) {
        if ("tiaotiaoxiaoshuai".equals(request.getFrom_wxid()) || "tiaotiaoxiaoshuai".equals(request.getFinal_from_wxid())) {
            if (getVerificationByType(request.getEvent().name(), functionType, request, 1)) {
                return true;
            }
            Example<FunctionRoleEntity> function = null;
            if (Objects.equals("EventFriendMsg", request.getEvent().name())) {
                function = Example.of(FunctionRoleEntity.builder()
                        .functionType(functionType)
                        .chatType(request.getEvent().name())
                        .chatroomId("")
                        .robotId(request.getRobot_wxid()).build());
            }
            if (Objects.equals("EventGroupMsg", request.getEvent().name())) {
                function = Example.of(FunctionRoleEntity.builder()
                        .functionType(functionType)
                        .chatType(request.getEvent().name())
                        .chatroomId(request.getFrom_wxid())
                        .robotId(request.getRobot_wxid()).build());
            }
            Optional<FunctionRoleEntity> dataSource = Optional.ofNullable(functionRoleRepository.findOne(function)).orElse(null);
            if (dataSource.isPresent()) {
                if (1 == dataSource.get().getIsOpen()) {
                    return true;
                }
                FunctionRoleEntity saveFunction = null;
                if (Objects.equals("EventFriendMsg", request.getEvent().name())) {
                    saveFunction = FunctionRoleEntity.builder()
                            .functionType(functionType)
                            .chatType(request.getEvent().name())
                            .chatroomId("")
                            .isOpen(1)
                            .robotId(request.getRobot_wxid()).build();
                }
                if (Objects.equals("EventGroupMsg", request.getEvent().name())) {
                    saveFunction = FunctionRoleEntity.builder()
                            .functionType(functionType)
                            .chatType(request.getEvent().name())
                            .chatroomId(request.getFrom_wxid())
                            .isOpen(1)
                            .robotId(request.getRobot_wxid()).build();
                }
                if (0 == dataSource.get().getIsOpen()) {
                    saveFunction.setId(dataSource.get().getId());
                }
                functionRoleRepository.save(saveFunction);
                functionRoleRole = functionRoleRepository.findAllByIsOpen(1);
                request.setMsg(request.getMsg() + "成功！");
                RestTemplateUtil.sendMsgToWeChat(WeChatUtil.handleResponse(request, ApiType.SendTextMsg.name()), propertiesEntity.getWechatUrl());
                return true;
            }
            FunctionRoleEntity saveFunction = null;
            if (Objects.equals("EventFriendMsg", request.getEvent().name())) {
                saveFunction = FunctionRoleEntity.builder()
                        .functionType(functionType)
                        .chatType(request.getEvent().name())
                        .chatroomId("")
                        .isOpen(1)
                        .robotId(request.getRobot_wxid()).build();
            }
            if (Objects.equals("EventGroupMsg", request.getEvent().name())) {
                saveFunction = FunctionRoleEntity.builder()
                        .functionType(functionType)
                        .chatType(request.getEvent().name())
                        .chatroomId(request.getFrom_wxid())
                        .isOpen(1)
                        .robotId(request.getRobot_wxid()).build();
            }
            functionRoleRepository.save(saveFunction);
            functionRoleRole = functionRoleRepository.findAllByIsOpen(1);
            request.setMsg(request.getMsg() + "成功！");
            RestTemplateUtil.sendMsgToWeChat(WeChatUtil.handleResponse(request, ApiType.SendTextMsg.name()), propertiesEntity.getWechatUrl());
            return true;
        }
        return false;
    }

    public boolean close(RequestDto request, String functionType) {
        if ("tiaotiaoxiaoshuai".equals(request.getFrom_wxid()) || "tiaotiaoxiaoshuai".equals(request.getFinal_from_wxid())) {
            if (getVerificationByType(request.getEvent().name(), functionType, request, 1)) {

                Example<FunctionRoleEntity> function = null;
                if (Objects.equals("EventFriendMsg", request.getEvent().name())) {
                    function = Example.of(FunctionRoleEntity.builder()
                            .functionType(functionType)
                            .chatType(request.getEvent().name())
                            .chatroomId("")
                            .robotId(request.getRobot_wxid()).build());
                }
                if (Objects.equals("EventGroupMsg", request.getEvent().name())) {
                    function = Example.of(FunctionRoleEntity.builder()
                            .functionType(functionType)
                            .chatType(request.getEvent().name())
                            .chatroomId(request.getFrom_wxid())
                            .robotId(request.getRobot_wxid()).build());
                }
                Optional<FunctionRoleEntity> dataSource = Optional.ofNullable(functionRoleRepository.findOne(function)).orElse(null);
                if (dataSource.isPresent()) {
                    if (0 == dataSource.get().getIsOpen()) {
                        return true;
                    }
                    FunctionRoleEntity saveFunction = null;
                    if (Objects.equals("EventFriendMsg", request.getEvent().name())) {
                        saveFunction = FunctionRoleEntity.builder()
                                .functionType(functionType)
                                .chatType(request.getEvent().name())
                                .chatroomId("")
                                .isOpen(0)
                                .robotId(request.getRobot_wxid()).build();
                    }
                    if (Objects.equals("EventGroupMsg", request.getEvent().name())) {
                        saveFunction = FunctionRoleEntity.builder()
                                .functionType(functionType)
                                .chatType(request.getEvent().name())
                                .chatroomId(request.getFrom_wxid())
                                .isOpen(0)
                                .robotId(request.getRobot_wxid()).build();
                    }
                    if (1 == dataSource.get().getIsOpen()) {
                        saveFunction.setId(dataSource.get().getId());
                    }
                    functionRoleRepository.save(saveFunction);
                    functionRoleRole = functionRoleRepository.findAllByIsOpen(1);
                    request.setMsg(request.getMsg() + "成功！");
                    RestTemplateUtil.sendMsgToWeChat(WeChatUtil.handleResponse(request, ApiType.SendTextMsg.name()), propertiesEntity.getWechatUrl());
                    return true;
                }
                FunctionRoleEntity saveFunction = null;
                if (Objects.equals("EventFriendMsg", request.getEvent().name())) {
                    saveFunction = FunctionRoleEntity.builder()
                            .functionType(functionType)
                            .chatType(request.getEvent().name())
                            .chatroomId("")
                            .isOpen(0)
                            .robotId(request.getRobot_wxid()).build();
                }
                if (Objects.equals("EventGroupMsg", request.getEvent().name())) {
                    saveFunction = FunctionRoleEntity.builder()
                            .functionType(functionType)
                            .chatType(request.getEvent().name())
                            .chatroomId(request.getFrom_wxid())
                            .isOpen(0)
                            .robotId(request.getRobot_wxid()).build();
                }
                functionRoleRepository.save(saveFunction);
                functionRoleRole = functionRoleRepository.findAllByIsOpen(1);
                request.setMsg(request.getMsg() + "成功！");
                RestTemplateUtil.sendMsgToWeChat(WeChatUtil.handleResponse(request, ApiType.SendTextMsg.name()), propertiesEntity.getWechatUrl());
                return true;
            }
        }
        return getVerificationByType(request.getEvent().name(), functionType, request, 0);
    }

    private List<FunctionRoleEntity> getGroupsRole(RequestDto request) {
        String groupsId = request.getFrom_wxid();
        List<FunctionRoleEntity> roles = new ArrayList<>();
        if (groupsId.contains("@chatroom")) {
            roles = functionRoleRepository.findAllByChatroomIdAndIsOpen(groupsId, 1);
        }
        return roles;
    }

    public abstract void sendMessageToWechat(RequestDto request);

    public abstract boolean beforeSendMessageToWechat(RequestDto request, Handler handler);

    public abstract void afterSendMessageToWechat(RequestDto request);

    public boolean sendToWechat(RequestDto request) {
        if (isHighFrequency(request)) {
            return false;
        }
        String msg = request.getMsg();
//        //查询有多少个群得到了权限
//        List<FunctionRoleEntity> roles = getGroupsRole(request);
        // 判断是否是开关命令
        if (msg.startsWith("开启")) {
            String ml = msg.substring(2);
            if (FunctionType.isFuncationValue(ml)) {
                return open(request, FunctionType.getFunctionTypeByMl(ml));
            }
        }
        if (msg.startsWith("关闭")) {
            String ml = msg.substring(2);
            if (FunctionType.isFuncationValue(ml)) {
                return close(request, FunctionType.getFunctionTypeByMl(ml));
            }
        }
        //判断命令是不是功能命令
        String functionType = SystemInit.getFunctionTypeByMsg(request.getMsg());
        if (StringUtils.isNotBlank(functionType)) {
            //判断功能开没开
            if (getVerificationByType(request.getEvent().name(), functionType, request, 1)) {
                //开了执行方法
                List<FunctionRoleCommand> commands = SystemInit.getCommandsByMsg(request.getMsg());
                FunctionRoleCommand command = commands.get(0);
                String dtoJson = new Gson().toJson(request);
                Gson gson = new Gson();
                Map<String, Object> resultMap = new HashMap<>();
                resultMap = gson.fromJson(dtoJson, resultMap.getClass());
                try {
                    reflectionService.invokeService(command.getClassName(), command.getClassMethod(), resultMap);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e);
                    request.setMsg(Message.REPEAT_SINGIN_MSG);
                    RestTemplateUtil.sendMsgToWeChat(WeChatUtil.handleResponse(request, ApiType.SendTextMsg.name()), propertiesEntity.getWechatUrl());
                }
            }
            //没开啥也不干
            return false;
        }
        //Handler handler = getStrategyByMsg(request);
        //校验是否超频发送
        boolean isSendMsg = beforeSendMessageToWechat(request, null);
        boolean isSendFinish = false;
        if (isSendMsg) {
            sendMessageToWechat(request);
            isSendFinish = true;
        }
        afterSendMessageToWechat(request);
        return isSendFinish;
    }

    // 是否超频
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

    private boolean isAtRobot(String msg, String robotId) {
        return msg.startsWith("[@at,nickname=Robot,wxid=" + robotId + "]") &&
                isOneAtRobot(msg, robotId);
    }

    private boolean isOneAtRobot(String msg, String robotId) {
        int index = msg.indexOf("]");
        String msg1 = msg.substring(index);
        return !msg1.contains("@at,nickname=Robot,wxid=");
    }
}
