package com.web.webchat.function;

import com.google.gson.Gson;
import com.web.webchat.config.PropertiesEntity;
import com.web.webchat.dto.RequestDto;
import com.web.webchat.dto.ResponseDto;
import com.web.webchat.entity.UserThing;
import com.web.webchat.enums.ApiType;
import com.web.webchat.repository.ChatroomMemberMoneyRepository;
import com.web.webchat.repository.ChatroomMemberSignRepository;
import com.web.webchat.repository.UserBagRepository;
import com.web.webchat.util.RestTemplateUtil;
import com.web.webchat.util.WeChatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Component("ThingFeatrue")
public class ThingFeatrue {
    @Autowired
    private PropertiesEntity propertiesEntity;

    //广播
    public ResponseDto guangbo(RequestDto request) {
        UserThing userThing = new Gson().fromJson(request.getObject().toString(), UserThing.class);
        String msg = String.format(userThing.getThingTemplate(), request.getFinal_from_name());
        request.setMsg(msg);
        RestTemplateUtil.sendMsgToWeChat(WeChatUtil.handleResponse(request, ApiType.SendTextMsg.name()), propertiesEntity.getWechatUrl());
        return null;
    }


}
