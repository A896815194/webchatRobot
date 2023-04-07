CREATE TABLE `blindDate` (
     `id` bigint(50) NOT NULL AUTO_INCREMENT,
     `chatroom_id` VARCHAR(50),
     `robot_id` VARCHAR(50),
     `content` VARCHAR(50),
     `pic_url` VARCHAR(50),
     `send_time` TIMESTAMP,
     PRIMARY KEY (`id`)
);