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
@Table(name = "sys_job")
public class SysJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * bean名称
     */
    @Column(name = "bean_name")
    private String beanName;
    /**
     * 方法名称
     */
    @Column(name = "method_name")
    private String methodName;
    /**
     * 方法参数
     */
    @Column(name = "method_params")
    private String methodParams;
    /**
     * cron表达式
     */
    @Column(name = "cron_expression")
    private String cronExpression;
    /**
     * 状态（1正常 0暂停）
     */
    @Column(name = "job_status")
    private Integer jobStatus;
    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;
    /**
     * 创建时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss ", timezone = "GMT+8")
    @Column(name = "create_time")
    private Date createTime;
    /**
     * 更新时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss ", timezone = "GMT+8")
    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "function_type")
    private String functionType;

    @Column(name = "chatroom_id")
    private String chatroomId;

    @Column(name = "wxid")
    private String wxid;
}
