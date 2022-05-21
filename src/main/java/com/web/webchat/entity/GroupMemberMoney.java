package com.web.webchat.entity;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupMemberMoney {

    private Integer memberMoneyId;

    private String chatroomId;

    private String wxid;

    private String wxidName;

    private String money;

}
