ALTER TABLE function_role ADD COLUMN chatroom_name
    VARCHAR(50) DEFAULT NULL COMMENT '群名称' ;
update function_role set chatroom_name = '?大神殿堂?灌篮' where chatroom_id = '18311306574@chatroom';
update function_role set chatroom_name = '干饭人' where chatroom_id = '21124084724@chatroom';
update function_role set chatroom_name = 'Robot' where chatroom_id = '18955225703@chatroom';
update function_role set chatroom_name = '穷鬼变富研究所' where chatroom_id = '18428420942@chatroom';
update function_role set chatroom_name = '70周年庆' where chatroom_id = '11117186907@chatroom';
update function_role set chatroom_name = '伙食改善进步小组' where chatroom_id = '25700089386@chatroom';
update function_role set chatroom_name = '木有云朵鸭~?' where chatroom_id = '18059175619@chatroom';
update function_role set chatroom_name = '中东富豪群' where chatroom_id = '4484999592@chatroom';