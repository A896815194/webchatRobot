CREATE TABLE IF NOT EXISTS `thing` (
    id INT(10) NOT NULL AUTO_INCREMENT,
    thing_name VARCHAR(50),
    thing_type VARCHAR(50),
    thing_desc VARCHAR(50),
    thing_class VARCHAR(50),
    thing_method VARCHAR(50),
    thing_template VARCHAR(50),
    start_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    end_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    duration bigint,
    auto_use INT,
    PRIMARY KEY (id)
    );