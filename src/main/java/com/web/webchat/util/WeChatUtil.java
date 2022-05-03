package com.web.webchat.util;

import com.google.gson.Gson;
import com.web.webchat.dto.RequestDto;
import com.web.webchat.dto.ResponseDto;
import com.web.webchat.enums.ApiType;
import com.web.webchat.function.Shop;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WeChatUtil {
    private static final Logger logger = LogManager.getLogger(WeChatUtil.class.getName());

    public static ResponseDto handleResponse(RequestDto req, String apiType) {
        ResponseDto response = ResponseDto.builder()
                .api(apiType)
                .msg(req.getMsg())
                .robot_wxid(req.getRobot_wxid())
                .to_wxid(req.getFrom_wxid())
                .build();
        if (req.getMsg().startsWith("@_@")) {
            if (req.getMsg().split("@_@")[1].startsWith("http")) {
                response.setApi(ApiType.SendImageMsg.name());
                response.setPath(req.getMsg().split("@_@")[1]);
            }
            response.setMsg(req.getMsg().split("@_@")[1]);
        }

        logger.info("webchatUtil 转换后的实体,req:{}",new Gson().toJson(req));
        return response;


    }
}
