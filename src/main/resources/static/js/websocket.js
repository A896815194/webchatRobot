//var ws = null;
$(function(){
    if ("WebSocket" in window){
        createWebSocket(wsUrl);
    }
});


var ws;//websocket实例
var lockReconnect = false;//避免重复连接
var wsUrl = 'ws://127.0.0.1:8081/websocket/';

function createWebSocket(url) {
    try {
        ws = new WebSocket(url);
        initEventHandle();
    } catch (e) {
        reconnect(url);
    }
}

function initEventHandle() {
    ws.onclose = function () {
        console.log("连接已关闭...");
        reconnect(wsUrl);
    };
    ws.onerror = function () {
        console.log("出现异常已关闭...");
        reconnect(wsUrl);
    };
    ws.onopen = function () {
        //心跳检测重置
        heartCheck.reset().start();
    };
    ws.onmessage = function (event) {
        console.log(event);
        var received_msg = event.data;
        let msg = $.parseJSON(received_msg)
        if(msg.action=="heartBeat"){
            heartCheck.reset().start();
            $("#human").html(msg.onlineCount);
        }
        if(msg.action =="welcome"){
            $("#msgBox").append("<p class='xt'>欢迎 <span class='rm'>"+msg.userName+"</span> 加入游戏</p>");
            $("#human").html(msg.onlineCount);
        }
        if(msg.action =="broadcast")
        {
            $("#msgBox").append("<p class='lt'><span class='rm'>"+msg.userName+" ：</span><span class='nr'>"+msg.msg+"</span></p>");
            $("#human").html(msg.onlineCount);
        }
        if(msg.action =="baby"){
            $("#msgBox").append("<p class='xt'><span class='rm'>"+msg.userName+"</span> 离开了游戏</p>");
            $("#human").html(msg.onlineCount);
        }
    }
}

function reconnect(url) {
    if(lockReconnect) return;
    lockReconnect = true;
    //没连接上会一直重连，设置延迟避免请求过多
    setTimeout(function () {
        console.log("尝试重连...");
        createWebSocket(url);
        lockReconnect = false;
    }, 2000);
}


//心跳检测
var heartCheck = {
    timeout: 60000,//60秒
    timeoutObj: null,
    reset: function(){
        clearTimeout(this.timeoutObj);
        return this;
    },
    start: function(){
        this.timeoutObj = setTimeout(function(){
            //这里发送一个心跳，后端收到后，返回一个心跳消息，
            //onmessage拿到返回的心跳就说明连接正常
            let sendMsg={
                "action":"heartBeat"
            }
            ws.send(JSON.stringify( sendMsg ));
        }, this.timeout)
    }
}


