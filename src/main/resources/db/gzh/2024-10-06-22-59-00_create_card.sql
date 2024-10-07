CREATE TABLE IF NOT EXISTS `card_user_gzh` (
    id INT(10) NOT NULL AUTO_INCREMENT,
    card_name VARCHAR(50),
    user_id int(50),
    user_name VARCHAR(50),
    card_type int(50),
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    expire_time TIMESTAMP NOT null default CURRENT_TIMESTAMP,
    update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_use int,
    is_delete int,
    PRIMARY KEY (id)
    );