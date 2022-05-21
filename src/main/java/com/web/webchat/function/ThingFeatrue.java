package com.web.webchat.function;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.web.webchat.config.PropertiesEntity;
import com.web.webchat.dto.RequestDto;
import com.web.webchat.dto.ResponseDto;
import com.web.webchat.entity.UserThing;
import com.web.webchat.enums.ApiType;
import com.web.webchat.util.RestTemplateUtil;
import com.web.webchat.util.WeChatUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("ThingFeatrue")
public class ThingFeatrue {
    private static final Logger logger = LogManager.getLogger(SignIn.class.getName());

    @Autowired
    private PropertiesEntity propertiesEntity;

    //广播
    public ResponseDto guangbo(RequestDto request) {
        logger.info("执行wxid:{},name:{}的物品效果", request.getFinal_from_wxid(), request.getFinal_from_name());
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        UserThing userThing = gson.fromJson(gson.toJson(request.getObject()), UserThing.class);
        String msg = String.format(userThing.getThingTemplate(), request.getFinal_from_name());
        request.setMsg(msg);
        RestTemplateUtil.sendMsgToWeChatSync(WeChatUtil.handleResponse(request, ApiType.SendTextMsg.name()), propertiesEntity.getWechatUrl());
        return null;
    }


}
