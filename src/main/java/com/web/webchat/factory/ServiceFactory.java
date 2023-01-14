package com.web.webchat.factory;

import com.web.webchat.abstractclass.ChatBase;
import com.web.webchat.api.WebChatController;
import com.web.webchat.enums.PushEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Configurable
@Component
public class ServiceFactory {

    private static final Logger logger = LogManager.getLogger(ServiceFactory.class.getName());

    @Autowired
    private Map<String, ChatBase> serviceStrategyMap = new HashMap<>();

    public ChatBase getInvokeStrategy(PushEvent eventType) {
        switch (eventType) {
            case EventFriendMsg:
                return serviceStrategyMap.get(PushEvent.EventFriendMsg.name());
            case EventGroupMsg:
                return serviceStrategyMap.get(PushEvent.EventGroupMsg.name());

            default:
                logger.error("啥也没有");
        }

        return null;
    }


}
