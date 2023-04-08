var navigation = document.querySelector(".navigation");
var video;
var btns;
var slides;
var contents;
var musicBtn;
// 提交按钮
var goBtn;
// 主页视频url
const indexVieoUrl = "/video/keai.mp4";
var contentBox;
var input;
const text = "她-拥有一双明亮的大眼睛和一头栗色短发，气质阳光，笑容甜美，总是能够给人带来积极的能量。她-性格开朗、可爱、搞怪，总是能够把气氛带动起来，让人忘记烦恼。她-每天都会积极地给大家带来好看的节目，不管跳舞、唱歌、talk还是打游戏，总能够让人们在她的直播间里度过一段愉快的时光。她-有时也是一个柔弱、爱哭、笨笨的女孩，总会出乎意料的搞砸计划，但这样的她更加真实、可爱。她-希望能够通过自己的直播，为更多的人带来快乐和正能量。同时也期待大家多多支持，一起见证她的成长和进步，守护最好的栗吱。==小栗吱的全体观众=="; // 需要打字的文本
let i = 0;
let speed = 60;
let pause = false;
var player;
var videoPlay;
$(function () {
        initIndex();
        if ("WebSocket" in window) {
        } else {
            alert("你的设备不支持在线聊天功能,所以无法进入到下一页");
            $("#go").attr('disabled', "disabled")//开始按钮禁用
            $("#go").attr('class', 'disable');//开启禁用标志
        }
    }
)

function initIndex() {
    video = document.getElementById('video');
    musicBtn = document.getElementById("musicBtn");
    btns = document.querySelectorAll(".nav-btn");
    slides = document.querySelectorAll(".video-slide")
    contents = document.querySelectorAll(".content");
    contentBox = $(".content p")[0];
    input = $("#username");
    goBtn = document.getElementById('go');
    videoPlay = document.getElementById('videoPlay');
    video.src = indexVieoUrl;
    video.addEventListener("canplaythrough", function() {
        video.play();
    });
    goBtn.addEventListener("click", function () {
        checkUser();
    });

    musicBtn.addEventListener("click", function () {
        if (video.muted) {
            video.muted = false;
            musicBtn.setAttribute("class", "fas fa-volume-mute");
        } else {
            video.muted = true;
            musicBtn.setAttribute("class", "fas fa-volume-up");
        }
    });
    videoPlay.addEventListener("click", function () {
        video.play();
    });
    typeWriter();
}


// 动态文字
function typeWriter() {
    if (i < text.length && !pause) {
        contentBox.innerHTML += text.charAt(i);
        i++;
        if (text.charAt(i - 1) === '。') {
            contentBox.innerHTML += "<div></div>";
        }
        if (text.charAt(i - 1) === '。') {
            setTimeout(() => {
                typeWriter();
            }, 1500);
        } else {
            setTimeout(() => {
                typeWriter();
            }, speed);
        }
    }
}

function checkUser() {
    let name = $("#username").val();
    if ($("#username").val().trim() == '') {
        alert("大家还不知道你是谁窝");
        return;
    }
    let param = {"name": name};
    JS.get("/api/v1/checkGame", param, function (result) {
        if (result.success) {
            if (result.isJoin) {
                alert("已经有人正在游戏,请更换其他昵称");
                return;
            }
            let form = document.getElementById('loginForm');
            form.method = "post";
            form.action = '/api/v1/loginform';
            form.submit();
        } else {
            alert("出现了小状况,请稍后再试");
        }

    });
}



