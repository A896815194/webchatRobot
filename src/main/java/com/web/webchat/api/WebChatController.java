package com.web.webchat.api;

import com.google.gson.Gson;
import com.web.webchat.abstractclass.ChatBase;
import com.web.webchat.dto.RequestDto;
import com.web.webchat.dto.ResponseDto;
import com.web.webchat.util.ServiceFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("webChat")
@Slf4j
public class WebChatController {

    private static final Logger logger = LogManager.getLogger(WebChatController.class.getName());
    @Autowired
    private ServiceFactory serviceFactory;

    @PostMapping("/api")
    public ResponseDto webChat(@RequestBody RequestDto request) {
        ResponseDto response = null;
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
        logger.info("请求数据{}", requestDto);
        ResponseDto response = null;
        RequestDto request = new Gson().fromJson(requestDto, RequestDto.class);
        ChatBase stragey = serviceFactory.getInvokeStrategy(request.getEvent());
        if (Objects.isNull(stragey)) {
            logger.error("没有这个策略的逻辑:{}", request.getEvent());
            return new ResponseDto();
        }
        if (Objects.equals(request.getRobot_wxid(), request.getFrom_wxid())) {
            return response;
        }
        stragey.sendToWechat(request);
        return response;
    }
}
