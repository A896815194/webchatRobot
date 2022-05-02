package com.web.webchat.util;

import com.web.webchat.dto.RequestDto;
import com.web.webchat.dto.ResponseDto;
import com.web.webchat.enums.ApiType;

public class WeChatUtil {

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

        System.out.println(response);
        return response;


    }
}
