#!/usr/bin/python
# coding:utf-8

# @FileName:    main.py
# @Time:        2024/1/2 22:27
# @Author:      bubu
# @Project:     douyinLiveWebFetcher

from liveMan import DouyinLiveWebFetcher

if __name__ == '__main__':
    # 直播间id
    live_id = '125853211144'
    # 用户集合
    # 2428182699715406 雪
    # 109318880836 狐狸
    # 62474263189 加
    # 76089173365 bo
    # 58688615767 三
    # 71091000991 偏执
    # 98481654073 剪辑君
    manager_user = [2428182699715406, 109318880836, 62474263189, 76089173365,58688615767,71091000991,98481654073]
    notify_url = 'https://www.jsdream.club/webChat/gzh/notify';
    signjs_url = 'M:\爬抖音\DouyinLiveWebFetcher-main\DouyinLiveWebFetcher-main\sign.js';
    DouyinLiveWebFetcher(live_id,manager_user,notify_url,signjs_url).start()
