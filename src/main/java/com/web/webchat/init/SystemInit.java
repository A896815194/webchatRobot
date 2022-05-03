package com.web.webchat.init;

import com.web.webchat.dto.RequestDto;
import com.web.webchat.entity.FunctionRoleCommand;
import com.web.webchat.entity.FunctionRoleEntity;
import com.web.webchat.repository.ChatRoomRoleRepository;
import com.web.webchat.repository.FunctionRoleCommandRepository;
import com.web.webchat.repository.FunctionRoleRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class SystemInit {

    // public static List<ChatRoomRoleEntity> chatRoomRole = new ArrayList<>();

    public static List<FunctionRoleEntity> functionRoleRole = new ArrayList<>();

    //托管开关
    public static boolean flag = false;

    @Autowired
    private ChatRoomRoleRepository chatRoomRoleRepository;

    @Autowired
    private FunctionRoleRepository functionRoleRepository;

    @Autowired
    private FunctionRoleCommandRepository functionRoleCommandRepository;

    public static RequestDto lastRequest = null;

    public static Map<String, RequestDto> lastRequestMap = new HashMap<>();
    // 命令, 方法类型
    public static Map<String, String> commandFunctionType = new HashMap<>();
    // 命令，方法实体
    public static Map<String, List<FunctionRoleCommand>> commandByFunctionType = new HashMap<>();
    public static List<FunctionRoleCommand> functionRoleCommands = new ArrayList<>();

    private static final String CHINES_NUMER_REGE = "[\\u4e00-\\u9fa5].*[0-9].*";

    private static final String CHINES_REGE = "[\\u4e00-\\u9fa5]*";

    private static final String NUMER_REGE = "[0-9]+";

    //获取初始化功能列表
    @PostConstruct
    public void init() {

        //chatRoomRole = chatRoomRoleRepository.findAllByIsOpen(1);
        functionRoleRole = functionRoleRepository.findAllByIsOpen(1);
        functionRoleCommands = functionRoleCommandRepository.findAll();
        if (!CollectionUtils.isEmpty(functionRoleCommands)) {
            commandByFunctionType = functionRoleCommands.stream().collect(Collectors.groupingBy(FunctionRoleCommand::getCommand));
            commandFunctionType = functionRoleCommands.stream().collect(Collectors.toMap(FunctionRoleCommand::getCommand, FunctionRoleCommand::getFunctionType));
        }
    }

    public static String getFunctionTypeByMsg(String msg) {
        //是不是文字+数字
        if (isChineseAndNumber(msg)) {
            String command = getChinesString(msg);
            String number = getMsgNumber(msg);
            if (Strings.isNotBlank(number)) {
                command = command + "%s";
                return commandFunctionType.get(command);
            }
        }
        return commandFunctionType.get(msg);
    }

    private static boolean isChineseAndNumber(String msg) {
        Pattern pattern = Pattern.compile(CHINES_NUMER_REGE);
        Matcher m = pattern.matcher(msg);
        //如果匹配到了
        if (m.find()) {
            return !Strings.isBlank(m.group());
        }
        return false;
    }

    private static String getChinesString(String msg) {
        Pattern pattern = Pattern.compile(CHINES_REGE);
        Matcher m = pattern.matcher(msg);
        if (m.find()) {
            return m.group();
        }
        return "";
    }

    public static String getMsgNumber(String msg) {
        Pattern pattern = Pattern.compile(NUMER_REGE);
        Matcher m = pattern.matcher(msg);
        //如果匹配到了
        if (m.find()) {
            return m.group();
        }
        return "";
    }


    public static List<FunctionRoleCommand> getCommandsByMsg(String msg) {
        if (isChineseAndNumber(msg)) {
            String command = getChinesString(msg);
            String number = getMsgNumber(msg);
            if (Strings.isNotBlank(number)) {
                command = command + "%s";
                return commandByFunctionType.get(command);
            }
        }
        return commandByFunctionType.get(msg);
    }
}
