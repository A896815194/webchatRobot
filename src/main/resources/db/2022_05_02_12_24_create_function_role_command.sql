create table if not exists `function_role_command`
(
    `id`  bigint(50) NOT NULL auto_increment,
    `function_type`    varchar(50) DEFAULT NULL,
    `command`  varchar(50) NOT NULL,
    `class_name`  varchar(50) NOT NULL,
    `class_method`  varchar(50) NOT NULL,
    PRIMARY KEY (`id`)
    )
    