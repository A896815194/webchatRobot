create table if not exists `chatroom_role`
(
    `id`                       bigint(50) NOT NULL auto_increment,
    `chatroom_name`            varchar(50) DEFAULT NULL,
    `chatroom_id`              varchar(50) DEFAULT NULL,
    `is_open`                  tinyint(1) NOT NULL,

    PRIMARY KEY (`id`)

    )