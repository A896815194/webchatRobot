package com.web.webchat.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "chatroom_member_money")
public class ChatroomMemberMoney {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "wxid_id")
    private String wxidId;

    @Column(name = "MONEY")
    private Long money;

}
