package com.web.webchat.function.gzh;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component("singDailyZbj")
public class SingDailyZbj {

    private static final Logger logger = LogManager.getLogger(SingDailyZbj.class.getName());

    private static final Map<String, Boolean> chatroomStateMap = new ConcurrentHashMap<>();

    //监控开启
    public String miniorOpen(String content) {
        logger.info("抽卡记录:content{}", content);
        return "";
    }


}
