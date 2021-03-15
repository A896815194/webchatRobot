package com.web.webchat.abstractclass;

import com.web.webchat.config.PropertiesEntity;
import com.web.webchat.dto.RequestDto;
import com.web.webchat.inteface.Handler;

public abstract class WechatProduct {

    protected Handler handler;

    public abstract Integer chat(RequestDto request, PropertiesEntity propertiesEntity);


}
