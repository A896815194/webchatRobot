package com.web.webchat.factory;


import com.web.webchat.inteface.Handler;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class StrategyFactory {

    // 创建不同类型的创建信息数据
    private static final Map<String, Handler> strategyMap = new HashMap<>();

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
