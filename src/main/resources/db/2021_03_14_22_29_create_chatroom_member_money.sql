CREATE TABLE IF NOT EXISTS `chatroom_member_money` (
    `id` int(10) NOT NULL AUTO_INCREMENT,
    `wxid_id` VARCHAR(50),
    `money` bigint NOT NULL,
    PRIMARY KEY (`id`)
    );