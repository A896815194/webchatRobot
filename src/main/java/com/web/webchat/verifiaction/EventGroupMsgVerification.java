package com.web.webchat.verifiaction;

import com.web.webchat.dto.RequestDto;
import com.web.webchat.enums.PushEvent;
import com.web.webchat.inteface.Verification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

import static com.web.webchat.init.SystemInit.functionRoleRole;

public class EventGroupMsgVerification extends Verification {
    private static final Logger logger = LogManager.getLogger(EventGroupMsgVerification.class.getName());

    public boolean hasOpen(RequestDto request, String functionType, int open) {
        logger.info("event:EventGroupMsg;chatroom:{},functionType:{},open:{}", request.getFrom_wxid(), functionType, open);
        if (Objects.equals(request.getEvent().name(), PushEvent.EventGroupMsg.name())) {
            return functionRoleRole.stream().anyMatch(item ->
                    Objects.equals(item.getChatType(), PushEvent.EventGroupMsg.name()) &&
                            Objects.equals(item.getChatType(), request.getEvent().name()) &&
                            Objects.equals(item.getFunctionType(), functionType) &&
                            Objects.equals(open, item.getIsOpen()) &&
                            Objects.equals(item.getRobotId(), request.getRobot_wxid()) &&
                            Objects.equals(item.getChatroomId(), request.getFrom_wxid()));
        }
        return false;
    }


}
