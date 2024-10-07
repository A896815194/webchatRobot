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
@Table(name = "sing_daily_gzh")
public class SingDailyGzhEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "song_name")
    private String songName;

    @Column(name = "years")
    private Integer year;

    @Column(name = "months")
    private Integer month;

    @Column(name = "days")
    private Integer day;

    // 上午   下午    晚上
    @Column(name = "time_type")
    private String timeType;
    // 2024-10-7
    @Column(name = "time_day")
    private String timeDay;

    @Column(name = "create_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss ", timezone = "GMT+8")
    private Date createTime;

    @Column(name = "update_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss ", timezone = "GMT+8")
    private Date updateTime;


}
