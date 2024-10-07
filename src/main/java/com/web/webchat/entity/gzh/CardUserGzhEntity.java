package com.web.webchat.entity.gzh;

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
@Table(name = "card_user_gzh")
public class CardUserGzhEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "card_name")
    private String cardName;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_name")
    private String userName;
    // 1 暴击  2迷雾 3借力
    @Column(name = "card_type")
    private Integer cardType;

    @Column(name = "create_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss ", timezone = "GMT+8")
    private Date createTime;

    @Column(name = "expire_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss ", timezone = "GMT+8")
    private Date expireTime;

    @Column(name = "update_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss ", timezone = "GMT+8")
    private Date updateTime;

    @Column(name = "is_delete")
    private Integer isDelete;

    @Column(name = "is_use")
    private Integer isUse;
}
