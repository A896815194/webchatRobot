package com.web.webchat.strategyContext;

import com.web.webchat.abstractclass.WechatProduct;
import com.web.webchat.config.PropertiesEntity;
import com.web.webchat.dto.RequestDto;
import com.web.webchat.enums.ApiType;
import com.web.webchat.init.SystemInit;
import com.web.webchat.util.RestTemplateUtil;
import com.web.webchat.util.StrategyFactory;
import com.web.webchat.util.WeChatUtil;

import java.util.List;

import static java.util.Objects.nonNull;
import static org.springframework.util.CollectionUtils.isEmpty;

public class BaiduTextReviewChat extends WechatProduct {
    public BaiduTextReviewChat(String functionType) {
        handler = StrategyFactory.getInvokeStrategy(functionType);
    }
    @Override
    public Integer chat(RequestDto request, PropertiesEntity propertiesEntity) {
        List<String> message = handler.createMessage(request, propertiesEntity);
        if(!isEmpty(message)) {
            request.setMsg(message.get(0));
            if (SystemInit.flag && "tiaotiaoxiaoshuai".equals(request.getRobot_wxid())) {
                request.setMsg(message + "\n" +
                        "(主人不在,现在由机器人晓晓回复)");
            }
            RestTemplateUtil.sendMsgToWeChat(WeChatUtil.handleResponse(request, ApiType.SendTextMsg.name()), propertiesEntity.getWechatUrl());
            return 1;
        }
        return null;
    }
}
