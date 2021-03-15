CREATE TABLE IF NOT EXISTS `chatroom_member_sign` (
    `id` int(10) NOT NULL AUTO_INCREMENT,
    `chatroom_id` VARCHAR(50),
    `wxid_id` VARCHAR(50),
    `wxid_name` VARCHAR(50),
    `rank_day` int(10) NOT NULL,
    `sign_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
    );