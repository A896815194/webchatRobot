#!/usr/bin/python
# coding:utf-8

# @FileName:    main.py
# @Time:        2024/1/2 22:27
# @Author:      bubu
# @Project:     douyinLiveWebFetcher
from flask import Flask
app = Flask(__name__)
from liveManWeb import DouyinLiveWebFetcher
import requests
import time
import os
import multiprocessing
import psutil
 
def liveMinior(): 
         # liveCast State url
        live_cast_state_url = 'http://localhost:8081/webChat/fetch/config';
        result = fetch_live_config(live_cast_state_url)
        if result == None:
            start_time = time.localtime()
            if not os.path.exists("logs"):
                os.mkdir("logs")
            if not os.path.exists("logs"):
                os.mkdir("logs")

            start_time_str = time.strftime('%Y%m%d_%H%M%S', start_time)
            filename = f"logs/error.txt"
            # 写入文件头部数据
            with open(filename, 'w', encoding='UTF-8') as file:
                file.write(f"【{start_time_str}】访问接口失败了,认为处理一下\n")
        else:        
            # 直播间id
            #live_id = '125853211144';
            #zb_id = 'Sy980317';
            #live_id = result
            #zb_id = '92553935098'
            # 管理id集合
            #manager_user = [2428182699715406, 109318880836, 62474263189, 76089173365,58688615767,71091000991,98481654073]
            # 通知url
            #notify_url = 'http://localhost:8081/webChat/gzh/notify';
            # 签名js路径
            #signjs_url = 'M:\爬抖音\DouyinLiveWebFetcher-main\DouyinLiveWebFetcher-main\sign.js';
            print(f"{result}")
            live_id = result.get('live_id')
            zb_id = result.get('zb_id')
            # 管理id集合
            manager_user = result.get('manager_user')
            # 通知url
            notify_url = result.get('notify_url')
            # 签名js路径
            signjs_url = result.get('signjs_url')
            DouyinLiveWebFetcher(live_id,manager_user,notify_url,signjs_url).start()
        
@app.route('/miniorLive')
def miniorLive(): 
    # 获取当前系统中的进程信息
    processes = psutil.process_iter()
    # 统计Python进程的数量
    python_processes = [p for p in processes if 'python' in p.name()]
    if len(python_processes) < 2:
        print("【执行监听方法】")
        task_id = multiprocessing.Process(target=liveMinior, args=())
        task_id.start()
    else:
        print("【有监听方法执行,所以不执行】")   
    return '1'    
            

def fetch_live_config(live_cast_state_url):
    try:
        response = requests.get(live_cast_state_url)
        response.raise_for_status()
        response_json = response.json()
        return response_json      
    except requests.exceptions.RequestException as e:
        print(f"Error during API call: {e}")
        return None
    
if __name__ == '__main__':
    app.run()