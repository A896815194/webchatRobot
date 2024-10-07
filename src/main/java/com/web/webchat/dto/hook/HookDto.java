package com.web.webchat.dto.hook;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HookDto {

    private String content;

    private String createTime;

    private String fromUserName;

    private String msgId;

    private String msgType;

    private String toUserName;
}
