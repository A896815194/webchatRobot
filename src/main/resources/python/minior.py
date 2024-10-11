#!/usr/bin/python
# coding:utf-8

# @FileName:    main.py
# @Time:        2024/1/2 22:27
# @Author:      bubu
# @Project:     douyinLiveWebFetcher

from liveManWeb import DouyinLiveWebFetcher
import sys
if __name__ == '__main__':
    # 直播间id
    live_id = sys.argv[1]
    # 管理id集合
    manager_user_string = sys.argv[2]
    manager_user = manager_user_string.split(',')
    # 通知url
    notify_url = sys.argv[3]
    # 签名js路径
    signjs_url = sys.argv[4]
    # danmulog路径
    cast_log_path = sys.argv[5]
    DouyinLiveWebFetcher(live_id,manager_user,notify_url,signjs_url,cast_log_path).start()
