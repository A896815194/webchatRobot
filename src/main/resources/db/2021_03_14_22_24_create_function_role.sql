create table if not exists `function_role`

(
    `id`  bigint(50) NOT NULL auto_increment,
    `function_type`    varchar(50) DEFAULT NULL,
    `chat_type`  varchar(50) NOT NULL,
    `is_open`  tinyint(1) NOT NULL,
    `chatroom_id`  varchar(50),
    `robot_id`    varchar(50) DEFAULT NULL,
    PRIMARY KEY (`id`)
    )
    