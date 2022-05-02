package com.web.webchat.util;

import com.web.webchat.abstractclass.ChatBase;
import com.web.webchat.enums.PushEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Configurable
@Component
public class ServiceFactory {

    @Autowired
    private Map<String, ChatBase> serviceStrategyMap = new HashMap<>();

    public ChatBase getInvokeStrategy(PushEvent eventType) {
        switch (eventType) {
            case EventFriendMsg:
                return serviceStrategyMap.get(PushEvent.EventFriendMsg.name());
            case EventGroupMsg:
                return serviceStrategyMap.get(PushEvent.EventGroupMsg.name());

            default:
                System.out.println("啥也没有");
        }

        return null;
    }


}
