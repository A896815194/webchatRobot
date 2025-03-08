package com.web.webchat.entity.birth;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "birth_card")
public class BirthCardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "pic_url")
    private String picUrl;

    @Column(name = "picf_url")
    private String picfUrl;
    // 是否抽到
    @Column(name = "send")
    private String send;

    @Column(name = "head_url")
    private String headUrl;
    //'1:礼物，2：祝福',
    @Column(name = "card_type")
    private String cardType;

    @Column(name = "zf_type")
    private String zfType;

    @Column(name = "content")
    private String content;

    @Column(name = "sfsl")
    private String sfsl;

}
