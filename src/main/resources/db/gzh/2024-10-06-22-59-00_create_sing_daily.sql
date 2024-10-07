CREATE TABLE IF NOT EXISTS `sing_daily_gzh` (
    id INT(10) NOT NULL AUTO_INCREMENT,
    song_name VARCHAR(50),
    years int(50),
    months int(50),
    days int(50),
    time_type VARCHAR(50),
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    time_day varchar(100) DEFAULT NULL,
    PRIMARY KEY (id)
    );