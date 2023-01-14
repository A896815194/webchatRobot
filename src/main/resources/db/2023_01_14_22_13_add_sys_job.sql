CREATE TABLE
    sys_job
(
    id bigint(50) NOT NULL AUTO_INCREMENT,
    bean_name VARCHAR(50),
    method_name VARCHAR(50),
    method_params VARCHAR(50),
    cron_expression VARCHAR(50),
    job_status int(2),
    remark VARCHAR(50),
    function_type VARCHAR(50),
    chatroom_id VARCHAR(50),
    wxid VARCHAR(50),
    create_time TIMESTAMP NULL,
    update_time TIMESTAMP NULL,
    PRIMARY KEY (id)
)
    ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;