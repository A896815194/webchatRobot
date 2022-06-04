package com.web.webchat.config.listen;

import com.web.webchat.dto.RequestDto;
import com.web.webchat.entity.ThingEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class AllEventPubLisher {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void pushAutoThingListener(RequestDto reqeust) {
        applicationEventPublisher.publishEvent(new ThingEvent(this, reqeust));
    }

    public void pushUseThingListener(RequestDto reqeust, ThingEntity thing) {
        applicationEventPublisher.publishEvent(new ThingEvent(this, reqeust, thing));
    }
}
