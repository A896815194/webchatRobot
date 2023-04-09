package com.web.webchat.dto.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    // 类型   1 个人   2广播
    private String type;

    private String action;
    // 人名
    private String userName;
    // 人id
    private String userId;

    private String onlineCount;

    private String msg;

    private List<Map<String,Object>> members;

}
