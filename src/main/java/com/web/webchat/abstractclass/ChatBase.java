package com.web.webchat.abstractclass;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.web.webchat.config.PropertiesEntity;
import com.web.webchat.config.listen.AllEventPubLisher;
import com.web.webchat.dto.KunPengRequestDto;
import com.web.webchat.dto.RequestDto;
import com.web.webchat.entity.FunctionRoleCommand;
import com.web.webchat.entity.FunctionRoleEntity;
import com.web.webchat.enums.ApiType;
import com.web.webchat.enums.FunctionType;
import com.web.webchat.enums.Message;
import com.web.webchat.function.timer.BlindDate;
import com.web.webchat.init.SystemInit;
import com.web.webchat.inteface.Handler;
import com.web.webchat.repository.FunctionRoleRepository;
import com.web.webchat.util.*;
import com.web.webchat.verifiaction.EventFriendMsgVerification;
import com.web.webchat.verifiaction.EventGroupMsgVerification;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static com.web.webchat.init.SystemInit.functionRoleRole;
import static java.util.Objects.isNull;

@Component
public abstract class ChatBase {
    private static final Logger logger = LogManager.getLogger(ChatBase.class.getName());

    @Autowired
    private PropertiesEntity propertiesEntity;
    @Autowired
    private FunctionRoleRepository functionRoleRepository;
    @Autowired
    private ReflectionService reflectionService;
    @Autowired
    private AllEventPubLisher pubLisher;

    // RequestDto{api='null', robot_wxid='wxid_i5vabkq7vwb222', from_wxid='18955225703@chatroom', to_wxid='wxid_i5vabkq7vwb222', msg='6', Event='EventGroupMsg'}
    public static boolean getVerificationByType(String eventMsg, String functionType, RequestDto request, int open) {
        if (Objects.equals("EventGroupMsg", eventMsg)) {
            return new EventGroupMsgVerification().hasOpen(request, functionType, open);
        }
        if (Objects.equals("EventFriendMsg", eventMsg)) {
            return new EventFriendMsgVerification().hasOpen(request, functionType, open);
        }
        return false;
    }

    public boolean open(RequestDto request, String functionType) {
        logger.info("开启功能命令.");
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
                        .chatroomName(request.getFinal_from_name())
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
                            .chatroomName(request.getFrom_name())
                            .isOpen(1)
                            .robotId(request.getRobot_wxid()).build();
                }
                if (0 == dataSource.get().getIsOpen()) {
                    saveFunction.setId(dataSource.get().getId());
                }
                functionRoleRepository.save(saveFunction);
                functionRoleRole = functionRoleRepository.findAllByIsOpen(1);
                request.setMsg(request.getMsg() + "成功！");
                functionTypeHandle(functionType, request);
                RestTemplateUtil.sendMsgToWeChatSync(WeChatUtil.handleResponse(request, ApiType.SendTextMsg), propertiesEntity.getWechatUrl());
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
                        .chatroomName(request.getFrom_name())
                        .isOpen(1)
                        .robotId(request.getRobot_wxid()).build();
            }
            functionRoleRepository.save(saveFunction);
            functionRoleRole = functionRoleRepository.findAllByIsOpen(1);
            request.setMsg(request.getMsg() + "成功！");
            functionTypeHandle(functionType, request);
            RestTemplateUtil.sendMsgToWeChatSync(WeChatUtil.handleResponse(request, ApiType.SendTextMsg), propertiesEntity.getWechatUrl());
            return true;
        }
        return false;
    }

    private void functionTypeHandle(String functionType, RequestDto request) {
        if (Objects.equals(functionType, FunctionType.BlindDate.name())) {
            logger.info(functionType + ":要创建配置文件");
            try {
                propertiesEntity.setChatroomId(request.getFrom_wxid());
                String pzFilePath = BlindDate.createPzPath(propertiesEntity);
                logger.info(functionType + ":配置文件:url" + pzFilePath);
                String content = createFileContent(functionType, request);
                ReadExcel.outFile(pzFilePath, content);
            } catch (Exception e) {
                logger.error("创建异常", e);
            }
            logger.info(functionType + ":创建完毕");
        }

    }

    private String createFileContent(String functionType, RequestDto request) {
        if (Objects.equals(functionType, FunctionType.BlindDate.name())) {
            StringBuilder sb = new StringBuilder();
            sb.append("不推男=");
            sb.append("\r");
            sb.append("不推女=");
            sb.append("\r");
            sb.append("男=");
            sb.append("\r");
            sb.append("女=");
            sb.append("\r");
            sb.append("内容男=");
            sb.append("\r");
            sb.append("内容女=");
            sb.append("\r");
            return sb.toString();
        }
        return null;
    }


    public boolean close(RequestDto request, String functionType) {
        logger.info("关闭功能命令.");
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
                            .chatroomName(request.getFrom_name())
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
                                .chatroomName(request.getFrom_name())
                                .isOpen(0)
                                .robotId(request.getRobot_wxid()).build();
                    }
                    if (1 == dataSource.get().getIsOpen()) {
                        saveFunction.setId(dataSource.get().getId());
                    }
                    functionRoleRepository.save(saveFunction);
                    functionRoleRole = functionRoleRepository.findAllByIsOpen(1);
                    request.setMsg(request.getMsg() + "成功！");
                    RestTemplateUtil.sendMsgToWeChatSync(WeChatUtil.handleResponse(request, ApiType.SendTextMsg), propertiesEntity.getWechatUrl());
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
                            .chatroomName(request.getFinal_from_name())
                            .chatroomId(request.getFrom_wxid())
                            .isOpen(0)
                            .robotId(request.getRobot_wxid()).build();
                }
                functionRoleRepository.save(saveFunction);
                functionRoleRole = functionRoleRepository.findAllByIsOpen(1);
                request.setMsg(request.getMsg() + "成功！");
                RestTemplateUtil.sendMsgToWeChatSync(WeChatUtil.handleResponse(request, ApiType.SendTextMsg), propertiesEntity.getWechatUrl());
                return true;
            }
        }
        return getVerificationByType(request.getEvent().name(), functionType, request, 0);
    }


    public abstract void sendMessageToWechat(RequestDto request);

    public abstract boolean beforeSendMessageToWechat(RequestDto request, Handler handler);

    public abstract void afterSendMessageToWechat(RequestDto request);

    public boolean sendToWechat(RequestDto request) {
        if (isHighFrequency(request)) {
            return false;
        }
        String msg = request.getMsg();
        // 判断是否是开关命令
        logger.info("发布物品特性事件");
        pubLisher.pushAutoThingListener(request);
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
        String functionType = InitCommonUtil.getFunctionTypeByMsg(request.getMsg());
        if (StringUtils.isNotBlank(functionType)) {
            logger.info("functionType{}:", functionType);
            //判断功能开没开
            if (getVerificationByType(request.getEvent().name(), functionType, request, 1)) {
                //开了执行方法
                List<FunctionRoleCommand> commands = InitCommonUtil.getCommandsByMsg(request.getMsg());
                FunctionRoleCommand command = commands.get(0);
                Map<String, Object> resultMap = convertRequestToMap(request);
                try {
                    reflectionService.invokeService(command.getClassName(), command.getClassMethod(), resultMap);
                } catch (Exception e) {
                    logger.error("反射执行对应的bean方法失败", e);
                    request.setMsg(Message.SYSTEM_ERROR_MSG);
                    RestTemplateUtil.sendMsgToWeChatSync(WeChatUtil.handleResponse(request, ApiType.SendTextMsg), propertiesEntity.getWechatUrl());
                }
            }
            //没开啥也不干
            return false;
        }
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

    public static Map<String, Object> convertRequestToMap(RequestDto request) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        String dtoJson = gson.toJson(request);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap = gson.fromJson(dtoJson, resultMap.getClass());
        return resultMap;
    }

    // 是否超频
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

    //    private boolean isAtRobot(String msg, String robotId) {
//        return msg.startsWith("[@at,nickname=Robot,wxid=" + robotId + "]") &&
//                isOneAtRobot(msg, robotId);
//    }
    private boolean isAtRobot(RequestDto request) {
        if (!CollectionUtils.isEmpty(request.getAtuserlists())) {
            List<KunPengRequestDto.AtUser> atUsers = request.getAtuserlists();
            if (atUsers.stream().anyMatch(item -> Objects.equals(item.getWxid(), request.getRobot_wxid()))) {
                return true;
            }
        }
        return false;
    }

//    private boolean isOneAtRobot(String msg, String robotId) {
//        int index = msg.indexOf("]");
//        String msg1 = msg.substring(index);
//        return !msg1.contains("@at,nickname=Robot,wxid=");
//    }

    private boolean isOneAtRobot(RequestDto request) {
        if (!CollectionUtils.isEmpty(request.getAtuserlists())) {
            List<KunPengRequestDto.AtUser> atUsers = request.getAtuserlists();
            if (atUsers.stream().allMatch(item -> Objects.equals(item.getWxid(), request.getRobot_wxid()))) {
                return true;
            }
        }
        return false;
    }
}
