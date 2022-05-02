package com.web.webchat.api;

import com.google.gson.Gson;
import com.web.webchat.abstractclass.ChatBase;
import com.web.webchat.dto.RequestDto;
import com.web.webchat.dto.ResponseDto;
import com.web.webchat.util.ServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;


@RestController
@RequestMapping("webChat")
public class WebChatController {

    @Autowired
    private ServiceFactory serviceFactory;

    @PostMapping("/api")
    public ResponseDto webChat(@RequestBody RequestDto request) {
        System.out.println(request);
        ResponseDto response = null;
//        //群聊私聊开关
//        SwitchControl chatControl = new SwitchControl();
//        chatControl.setFunctionSwitch(switchService);
//        if(chatControl.openFunction(request)){
//            return response;
//        };
//        if(chatControl.closeFunction(request)){
//            return response;
//        };
        ChatBase stragey = serviceFactory.getInvokeStrategy(request.getEvent());
        if (Objects.isNull(stragey)) {
            System.out.println("没有这个策略的逻辑:" + stragey);
            return new ResponseDto();
        }
        stragey.sendToWechat(request);
        return response;
    }

    @PostMapping("new/api")
    public ResponseDto newWebChat(@RequestBody String requestDto) {
        System.out.println(requestDto);
        ResponseDto response = null;
        RequestDto request = new Gson().fromJson(requestDto, RequestDto.class);
        ChatBase stragey = serviceFactory.getInvokeStrategy(request.getEvent());
        if (Objects.isNull(stragey)) {
            System.out.println("没有这个策略的逻辑:" + stragey);
            return new ResponseDto();
        }
        if (Objects.equals(request.getRobot_wxid(), request.getFrom_wxid())) {
            return response;
        }
        stragey.sendToWechat(request);
        return response;
    }
}
