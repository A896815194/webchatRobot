package com.web.webchat.strategy;

import com.google.gson.Gson;
import com.web.webchat.config.PropertiesEntity;
import com.web.webchat.dto.RequestDto;
import com.web.webchat.dto.tulingrobot.TuLingRobotRequestDto;
import com.web.webchat.dto.tulingrobot.TuLingRobotResponseDto;
import com.web.webchat.enums.FunctionType;
import com.web.webchat.factory.StrategyFactory;
import com.web.webchat.inteface.Handler;
import com.web.webchat.util.RestTemplateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.util.CollectionUtils.isEmpty;

@Component
public class TuLingRobotMsg implements Handler {
    private static final Logger logger = LogManager.getLogger(TuLingRobotMsg.class.getName());

    @Autowired
    private PropertiesEntity properties;

    @Override
    public void afterPropertiesSet() throws Exception {
        StrategyFactory.register(FunctionType.TuLingRobot.name(), this);
    }

    @Override
    public List<String> createMessage(RequestDto request, PropertiesEntity propertiesEntity) {
        this.properties = propertiesEntity;
        return getTuLingResponseMsg(request.getMsg());
    }

    public List<String> getTuLingResponseMsg(String msg) {
        TuLingRobotRequestDto request = getTuLingRobotRequestDto(msg);
        TuLingRobotResponseDto responseDto = RestTemplateUtil.sendMsgToTuLingRobot(request,properties.getTulingApi());
        logger.info(new Gson().toJson(responseDto));
        List<String> result = new ArrayList<>();
        if (!isEmpty(responseDto.getResults())) {
            if (responseDto.getResults().stream().anyMatch(item -> "image".equals(item.getResultType()))) {
                responseDto.getResults().forEach(item -> {
                    if ("image".equals(item.getResultType())) {
                        result.add("@_@" + item.getValues().get("image"));
                    } else {
                        result.add(String.join("\n", item.getValues().values()));
                    }
                });
            } else {
                responseDto.getResults().forEach(item -> {
                    result.add(String.join("\n", item.getValues().values()));
                });
            }
        }
        return result;
    }

    private TuLingRobotRequestDto getTuLingRobotRequestDto(String msg) {
        Map<String, String> text = new HashMap<>();
        text.put("text", msg);
        Map<String, String> selfInfo = new HashMap<>();
        selfInfo.put("text", msg);
        text.put("text", msg);
        Map<String, Object> perception = Stream.of(
                new AbstractMap.SimpleEntry<>("inputText", text))
                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
        Map<String, String> userInfo = Stream.of(
                new AbstractMap.SimpleEntry<>("apiKey", properties.getTulingApiKey()),
                new AbstractMap.SimpleEntry<>("userId", properties.getTulingUserId())
        )
                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));

        TuLingRobotRequestDto request = TuLingRobotRequestDto.builder()
                .reqType(0)
                .perception(perception)
                .userInfo(userInfo).build();
        return request;
    }

}
