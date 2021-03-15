package com.web.webchat.init;

import com.web.webchat.dto.RequestDto;
import com.web.webchat.entity.ChatRoomRoleEntity;
import com.web.webchat.entity.FunctionRoleEntity;
import com.web.webchat.repository.ChatRoomRoleRepository;
import com.web.webchat.repository.FunctionRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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

    public static RequestDto lastRequest = null;

    public static Map<String,RequestDto> lastRequestMap= new HashMap<>();
    @PostConstruct
    public void init() {

        //chatRoomRole = chatRoomRoleRepository.findAllByIsOpen(1);
        functionRoleRole = functionRoleRepository.findAllByIsOpen(1);
    }

}
