package com.web.webchat.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "function_role")
public class FunctionRoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "function_type")
    private String functionType;

    @Column(name = "chat_type")
    private String chatType;

    @Column(name = "chatroom_id")
    private String chatroomId;

    @Column(name = "robot_id")
    private String robotId;

    @Column(name = "is_open")
    private Integer isOpen;
}
