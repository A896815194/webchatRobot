package com.web.webchat.entity.video;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "danmaku_list")
public class DanmuEntity {

    //'弹幕池id'
    @Column(name = "id")
    private String id;
    //弹幕id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cid;
    //弹幕内容
    @Column(name = "type")
    private String type;
    //'弹幕颜色
    @Column(name = "text")
    private String text;
    //''弹幕颜色''
    @Column(name = "color")
    private String color;
    //'弹幕大小'
    @Column(name = "size")
    private String size;
    //'时间点'
    @Column(name = "videotime")
    private float videotime;
    //'用户ip'
    @Column(name = "ip")
    private String ip;
    //'发送时间
    @Column(name = "time")
    private Integer time;
}
