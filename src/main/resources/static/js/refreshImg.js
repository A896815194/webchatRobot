function imgLoad(img,callback) {
    var timer = setInterval(function() {
        if (img.complete) {
            callback(img)
            clearInterval(timer)
        }
    }, 50)
}

function imgsLoad(imgs,callback) {
    let imgCount = imgs.length;
    let loadedCount = 0;
    for (var i = 0; i < imgs.length; i++) {
        imgs[i].onload = function() {
            loadedCount++;
            if (loadedCount === imgCount) {
                callback(imgs);
            }
        }
    }
}
