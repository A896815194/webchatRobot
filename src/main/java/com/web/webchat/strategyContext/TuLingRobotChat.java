package com.web.webchat.strategyContext;

import com.web.webchat.abstractclass.WechatProduct;
import com.web.webchat.config.PropertiesEntity;
import com.web.webchat.dto.RequestDto;
import com.web.webchat.enums.ApiType;
import com.web.webchat.init.SystemInit;
import com.web.webchat.util.*;

import java.util.List;

public class TuLingRobotChat extends WechatProduct {

    public TuLingRobotChat(String functionType) {
        handler = StrategyFactory.getInvokeStrategy(functionType);
    }

    @Override
    public Integer chat(RequestDto request,PropertiesEntity propertiesEntity) {
        if(34 == request.getType()){
            String pcmUrl = VoiceDecoderUtil.silkToPcm(request.getMsg(),propertiesEntity.getSourceVoiceRootPath()+"/"+request.getFrom_wxid()+"/",String.valueOf(System.currentTimeMillis()),propertiesEntity.getSilkV3Path());
            String msg = BaiduAsrMainUtil.getMsgFromPcm(pcmUrl);
            request.setMsg(msg);
        }
       List<String> message = handler.createMessage(request.getMsg(), propertiesEntity);

        message.forEach(item->{
            if(SystemInit.flag && "tiaotiaoxiaoshuai".equals(request.getRobot_wxid())){
                request.setMsg(message+"\n" +
                        "(主人不在,现在由机器人晓晓回复)");
            }else {
                request.setMsg(item);
            }RestTemplateUtil.sendMsgToWeChat(WeChatUtil.handleResponse(request, ApiType.SendTextMsg.name()),propertiesEntity.getWechatUrl());

        });
       return 1;
    }


}
