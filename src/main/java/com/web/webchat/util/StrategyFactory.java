package com.web.webchat.util;


import com.web.webchat.inteface.Handler;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class StrategyFactory {

    private static Map<String, Handler> strategyMap = new HashMap<>();

    public static Handler getInvokeStrategy(String name) {
        return strategyMap.get(name);
    }

    public static void register(String name, Handler handler) {
        if (StringUtils.isEmpty(name) || null == handler) {
            return;
        }
        strategyMap.put(name, handler);
    }
}
