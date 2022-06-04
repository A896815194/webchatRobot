package com.web.webchat.function;

import com.web.webchat.config.listen.AllEventPubLisher;
import com.web.webchat.dto.RequestDto;
import com.web.webchat.dto.ResponseDto;
import com.web.webchat.entity.FunctionRoleCommand;
import com.web.webchat.entity.ThingEntity;
import com.web.webchat.enums.FunctionType;
import com.web.webchat.init.SystemInit;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component("MagicThing")
public class MagicThing {

    private static final Logger logger = LogManager.getLogger(MagicThing.class.getName());

    @Autowired
    private AllEventPubLisher publisher;

    //魔法使用道具
    public ResponseDto magicUseThing(RequestDto request) {
        logger.info("wxid:{},name:{}使用魔法道具", request.getFinal_from_wxid(), request.getFinal_from_name());
        // 使用 妙手空空
        String msg = request.getMsg();
        List<FunctionRoleCommand> rcs = SystemInit.functionRoleCommands.stream().filter(item -> Objects.equals(item.getFunctionType(), FunctionType.MagicThing.name())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(rcs)) {
            logger.info("没有魔法道具这个命令");
            return null;
        }
        // 使用
        String command = rcs.get(0).getCommand();
        if (command.endsWith("*")) {
            command = StringUtils.substringBefore(command, "*");
        }
        //使用 + 妙手空空  + 可能会艾特人   去掉使用
        String commandString = StringUtils.substringAfter(msg, command);
        if (StringUtils.isBlank(commandString)) {
            logger.info("没有以命令:【{}】开头", command);
            return null;
        }
        AtomicReference<ThingEntity> useThing = new AtomicReference<>();
        SystemInit.nameThingMap.forEach((k, v) -> {
            if (commandString.startsWith(k)) {
                useThing.set(v);
            }
        });
        if (useThing.get() == null) {
            logger.info("命令:【{}】没有找到对应的物品", command);
            return null;
        }
        // 妙手空空对应的物品
        ThingEntity commandThing = useThing.get();
        //msg 换成真实命令
        request.setMsg(commandString);
        if (isPushThingEvent(commandString, commandThing)) {
            publisher.pushUseThingListener(request, commandThing);
            return null;
        }
        logger.info("命令:【{}】不符合命令规则", commandString);
        return null;
    }

    private boolean isPushThingEvent(String commandString, ThingEntity commandThing) {
        // 如果是直接使用的物品就推送
        if (commandThing.getUseType() == null) {
            return false;
        }
        if (commandThing.getUseType() == 1) {
            return true;
        }
        // 2类物品需要艾特人使用
        if (commandThing.getUseType() == 2) {
            // 判断已没有以  物品命令开头  比如以妙手空空开头,有值会去掉妙手空空留下剩余部分
            String commands = StringUtils.substringAfter(commandString, commandThing.getThingName());
            if (StringUtils.isBlank(commands)) {
                return false;
            }
            // 如果艾特人了
            return isAt(commands);
        }
        return false;
    }

    // at开通并且有艾特内容就是true
    private static boolean isAt(String msg) {
        return msg.startsWith("[@at,nickname=") &&
                isHasAt(msg);
    }

    private static boolean isHasAt(String msg) {
        Pattern pattern = Pattern.compile("\\[@at,nickname=");
        Matcher matcher = pattern.matcher(msg);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count > 0;
    }

}
