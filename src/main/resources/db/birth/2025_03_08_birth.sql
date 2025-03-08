
CREATE TABLE webchat.birth_card (
                                    `id` int(11) NOT NULL AUTO_INCREMENT,
                                    `name` varchar(100) DEFAULT NULL COMMENT '名字',
                                    `type` varchar(100) DEFAULT NULL COMMENT '紫卡金卡',
                                    `picUrl` varchar(100) DEFAULT NULL COMMENT '卡片正面图片',
                                    `send` varchar(100) DEFAULT NULL COMMENT '是否抽中过',
                                    `headUrl` varchar(100) DEFAULT NULL COMMENT '头像地址',
                                    `cardType` varchar(100) DEFAULT NULL COMMENT '1:礼物，2：祝福',
                                    `content` varchar(100) DEFAULT NULL COMMENT '祝福描述',
                                    PRIMARY KEY (`id`)
);