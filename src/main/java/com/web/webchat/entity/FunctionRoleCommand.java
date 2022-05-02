package com.web.webchat.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "function_role_command")
public class FunctionRoleCommand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "function_type")
    private String functionType;

    @Column(name = "command")
    private String command;

    @Column(name = "class_name")
    private String className;

    @Column(name = "class_method")
    private String classMethod;
}
