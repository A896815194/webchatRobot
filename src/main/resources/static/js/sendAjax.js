const JS = {};
JS.post = function (url, param, callback, errorCallback) {
    console.log(JSON.stringify(param));
    $.ajax({
        type: 'POST',
        url: url,
        async: false,
        dataType: 'json',
        crossDomain: true,
        data: JSON.stringify(param),
        xhrFields: {
            withCredentials: true
        },
        contentType : 'application/json;charset=utf-8',
        success: function (result) {
            callback(result);
        },
        error: function (xhr, status, error) {
            if (errorCallback !== undefined) {
                errorCallback(xhr, status, error);
            }
        }
    });
};
JS.jumpIndex = function(url,type){
    let form = document.createElement("form");
    $("body").append(form);
    form.id = "jumpForm";
    form.method = type;
    form.action = url;
    form.submit();
}
JS.get = function (url, param, callback, errorCallback) {
    console.log($.param(param));
    $.ajax({
        type: 'GET',
        url: url,
        crossDomain: true,
        xhrFields: {
            withCredentials: true
        },
        async: false,
        dataType: 'json',
        data: $.param(param),
        success: function (result) {
            callback(result);
        },
        error: function (xhr, status, error) {
            if (errorCallback !== undefined) {
                errorCallback(xhr, status, error);
            }
        }
    });
};
JS.playAudio = function(musicUrl,duration){
    soundUrls.push(musicUrl);
    if(duration!==undefined){
        soundDurations =[];
        soundDurations.push(duration);
    }
}


// 定义存放声音文件url的数组
const soundUrls = [];
// 定义每段音乐播放的时长
var soundDurations = [2000];

// 定义播放声音函数
function playSound(url, duration) {
    const audio = new Audio(url);
    audio.play();
    setTimeout(() => {
        audio.pause();
        audio.currentTime = 0;
        // 播放结束后移除该声音的url
        soundUrls.shift();
        // 如果还有下一个声音，则继续播放
        if (soundUrls.length) {
            playSound(soundUrls[0], soundDurations[0]);
        }
        if (soundUrls.length == 0) {
            isPlaying = false;
        }
    }, duration);
}
// 监听声音url的变化
let isPlaying = false; // 是否正在播放声音
Object.defineProperty(soundUrls, 'push', {
    configurable: false,
    enumerable: false,
    writable: false,
    value: function () {
        Array.prototype.push.apply(this, arguments);
        // 如果当前没有正在播放的声音，则播放第一个声音
        if (!isPlaying && soundUrls.length) {
            isPlaying = true;
            playSound(soundUrls[0], soundDurations[0]);
        }
    }
});