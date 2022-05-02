package com.web.webchat.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "chatroom_member_sign")
public class ChatroomMemberSign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chatroom_id")
    private String chatroomId;

    @Column(name = "wxid_id")
    private String wxidId;

    @Column(name = "wxid_name")
    private String wxidName;

    @Column(name = "rank_day")
    private Integer rankDay;

    @Column(name = "sign_time")
    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss ", timezone = "GMT+8")
    private Date signTime;

}
