ALTER TABLE function_role ADD COLUMN function_name
    VARCHAR(50) DEFAULT NULL COMMENT '方法名字' ;
ALTER TABLE function_role ADD COLUMN command
    VARCHAR(50) DEFAULT NULL COMMENT '方法命令' ;