package com.web.webchat.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "blindDate")
public class BindDateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chatroom_id")
    private String chatroomId;

    @Column(name = "robot_id")
    private String robotId;

    @Column(name = "content")
    private Integer content;

    @Column(name = "pic_url")
    private Long picUrl;

    @Column(name = "send_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss ", timezone = "GMT+8")
    private Date sendTime;


}
