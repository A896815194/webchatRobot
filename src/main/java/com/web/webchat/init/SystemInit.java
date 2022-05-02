package com.web.webchat.init;

import com.web.webchat.dto.RequestDto;
import com.web.webchat.entity.FunctionRoleCommand;
import com.web.webchat.entity.FunctionRoleEntity;
import com.web.webchat.repository.ChatRoomRoleRepository;
import com.web.webchat.repository.FunctionRoleCommandRepository;
import com.web.webchat.repository.FunctionRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        return commandFunctionType.get(msg);
    }
    public static List<FunctionRoleCommand> getCommandsByMsg(String msg){
       return commandByFunctionType.get(msg);
    }
}
