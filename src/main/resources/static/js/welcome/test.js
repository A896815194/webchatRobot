(function () {
    //触屏即加载音乐

    // document.addEventListener('touchstart', function() {undefined
    // document.getElementById('media').play()
    // }

    //进入微信页面即加载
    // document.addEventListener('WeixinJSBridgeReady', function() {undefined
    // document.getElementById('videoPlay').play()
    // })

    var voice = document.getElementById('video');
    if(typeof WeixinJSBrdgeReady=="object" && typeof WeixinJSBridge.invoke == "function"){
        voice.play()
    }else{
        if (document.addEventListener) {
            document.addEventListener("WeixinJSBridgeReady", function () {
                voice.play();
            }, false);
        } else if (document.attachEvent) {
            document.attachEvent("WeixinJSBridgeReady", function () {
                voice.play();
            });
            document.attachEvent("onWeixinJSBridgeReady", function () {
                voice.play();
            });
        }
    }
})