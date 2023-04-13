var wsUrl = 'ws://192.168.0.5:8081/websocket/';
// var wsUrl = 'ws://124.223.2.76:8081/websocket/';
var ws;//websocket实例
var lockReconnect = false;//避免重复连接
var loginName = '';
// 失败次数
var failcount = 0;
var gameUser = '';
let finshAudio = '/audio/finish.mp3';
let levelDuration = [2000, 4000, 6000];
let jlImgUrl = ['/img/gif/wang.gif', '/img/gif/kiss.gif',
    '/img/gif/wang.gif', '/img/gif/kiss.gif', '/img/gif/wang.gif', '/img/gif/kiss.gif',
    '/img/gif/wang.gif', '/img/gif/kiss.gif',
    '/img/gif/1.gif',
    '/img/gif/3.gif', '/img/gif/4.gif', '/img/gif/kiss.gif',
    '/img/gif/6.gif', '/img/gif/7.gif',
    '/img/gif/8.gif', '/img/gif/9.gif',
    '/img/gif/10.gif', '/img/gif/11.gif',
    '/img/gif/16.gif', '/img/gif/17.gif', '/img/gif/18.gif'];
$(function () {
    let usrId = getQueryString("id");
    loginName = usrId;
    checkLogin(usrId);
    if ("WebSocket" in window) {
        createWebSocket(wsUrl);
    }
});

function getQueryString(name) {
    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
    var r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return unescape(r[2]);
    }
    return null;
}

// function checkLogin(){
//     loginName = JsCookie.getCookie("aliName");
//     if(loginName === null|| loginName===""){
//         alert("没有通过主页访问进来,请回到主页");
//         JS.jumpIndex("/ali/index", "get");
//     }
// }

function checkLogin() {
    let id = getQueryString("id");
    if (id == null) {
        alert("请不要随意修改id!");
    }
    let param = {"id": id};
    JS.get("/api/v1/checkGameById", param, function (result) {
        if (!result.success) {
            alert("游客不能随意访问窝~,带你认识下小栗吱吧");
            JS.jumpIndex("/ali/index", "get");
        }
        $("#username").html(result.username);
        gameUser = result.username;
    });
}

function createWebSocket(url) {
    try {
        ws = new WebSocket(url + loginName);
        initEventHandle();
    } catch (e) {
        reconnect(url);
    }
}

function delCookie(name) {
    let param = {"name": name};
    JS.get("/api/v1/deletCookie", param, function () {
    });
}

function initEventHandle() {
    ws.onclose = function () {
        console.log("连接已关闭...");
        reconnect(wsUrl);
        //delCookie(loginName)
        // JsCookie.delCookie("aliName");
    };
    ws.onerror = function () {
        console.log("出现异常已关闭...");
        reconnect(wsUrl);
        console.log(loginName);
        // delCookie(loginName)
        // JsCookie.delCookie("aliName");
    };
    ws.onopen = function () {
        //心跳检测重置
        heartCheck.reset().start();
    };
    ws.onmessage = function (event) {
        console.log(event);
        var received_msg = event.data;
        let msg = $.parseJSON(received_msg)
        if (msg.action == "gxGame") {
            $("#msgBox").append("<p class='xtts'>" + msg.msg + "</p>");
            $("#human").html(msg.onlineCount);
            JS.playAudio(finshAudio, 4000);
            let gifUrl = jlImgUrl[Math.floor((Math.random() * jlImgUrl.length))];
            var view2 = new view({
                isClose: true,
                duration: levelDuration[level],
                imgUrl: gifUrl,
            });
        }
        if (msg.action == "heartBeat") {
            heartCheck.reset().start();
            $("#human").html(msg.onlineCount);
            refreshUserUl(msg);
        }
        if (msg.action == "welcome") {
            $("#msgBox").append("<p class='xt'>欢迎 <span class='rm'>" + msg.userName + "</span> 加入游戏</p>");
            $("#human").html(msg.onlineCount);
            refreshUserUl(msg);
        }
        if (msg.action == "broadcast") {
            $("#msgBox").append("<p class='lt'><span class='rm'>" + msg.userName + " ：</span><span class='nr'>" + msg.msg + "</span></p>");
            $("#human").html(msg.onlineCount);
            refreshUserUl(msg);
        }
        if (msg.action == "baby") {
            $("#msgBox").append("<p class='xt'><span class='rm'>" + msg.userName + "</span> 离开了游戏</p>");
            $("#human").html(msg.onlineCount);
            refreshUserUl(msg);
        }
    }
}

function refreshUserUl(msg) {
    if (msg.members !== undefined && msg.members.length > 0) {
        $("#nameUl").html("");
        for (let i = 0; i < msg.members.length; i++) {
            let member = msg.members[i];
            $("#nameUl").append("<li id=" + member.id + ">" + member.name + "</li>");
        }
    }
}

function reconnect(url) {
    checkLogin();
    if (lockReconnect) return;
    lockReconnect = true;
    //没连接上会一直重连，设置延迟避免请求过多
    setTimeout(function () {
        console.log("尝试重连..." + failcount + "次");
        createWebSocket(url);
        if (failcount >= 10) {
            alert("重连已经超过10次,回到主页休息一下吧");
            JS.jumpIndex("/ali/index", "get");
        }
        failcount++;
        lockReconnect = false;
    }, 2000);
}


//心跳检测
var heartCheck = {
    timeout: 60000,//60秒
    timeoutObj: null,
    reset: function () {
        clearTimeout(this.timeoutObj);
        return this;
    },
    start: function () {
        this.timeoutObj = setTimeout(function () {
            //这里发送一个心跳，后端收到后，返回一个心跳消息，
            //onmessage拿到返回的心跳就说明连接正常
            let sendMsg = {
                "action": "heartBeat",
                "userId": loginName
            }
            ws.send(JSON.stringify(sendMsg));
            checkLogin();
        }, this.timeout)
    }
}


