package com.web.webchat.api;

import com.web.webchat.dto.RequestDto;
import com.web.webchat.dto.ResponseDto;
import com.web.webchat.util.ServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("webChat")
public class WebChatController {

    @Autowired
    private ServiceFactory serviceFactory;

    @Autowired
    private com.web.webchat.service.SwitchService switchService;

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
        serviceFactory.getInvokeStrategy(request.getEvent()).sendToWechat(request);
        return response;
    }

}
