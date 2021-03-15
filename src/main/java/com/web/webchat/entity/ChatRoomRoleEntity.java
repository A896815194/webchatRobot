package com.web.webchat.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "chatroom_role")
public class ChatRoomRoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chatroom_name")
    private String chatRoomName;

    @Column(name = "chatroom_id")
    private String chatRoomId;

    @Column(name = "is_open")
    private Integer isOpen;
}
