package com.web.webchat.config.listen;

import com.web.webchat.dto.RequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class AllEventPubLisher {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void pushListener(RequestDto reqeust) {
        applicationEventPublisher.publishEvent(new ThingEvent(this, reqeust));
    }
}
