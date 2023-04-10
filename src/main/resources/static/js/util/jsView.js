/**弹窗**/
function view(options){
    this.isClose = options.isClose || true;
    this.duration = options.duration || null;
    this.imgUrl = options.imgUrl || null;
    this.content = options.content || null;

    var that = this;

    //创建弹窗元素
    var popup = document.createElement('div');
    popup.classList.add('popup');

    //关闭按钮
    if(this.isClose){
        var closeBtn = document.createElement('div');
        closeBtn.classList.add('close-btn');
        closeBtn.innerHTML = '<h>×</h>';
        popup.appendChild(closeBtn);

        closeBtn.addEventListener('click', function(){
            that.close(popup);
        });
    }

    //内容图片
    if(this.imgUrl){
        var img = document.createElement('img');
        img.classList.add('content-img');
        img.src = this.imgUrl;
        popup.appendChild(img);
    }

    //内容文字
    if(this.content){
        var content = document.createElement('p');
        content.classList.add('content-text');
        content.innerHTML = this.content;
        popup.appendChild(content);
    }

    //添加到body中
    document.body.appendChild(popup);

    //自动关闭
    if(this.duration){
        setTimeout(function(){
            that.close(popup);
        }, this.duration);
    }

    //关闭弹窗方法
    this.close = function(popup){
        popup.parentNode.removeChild(popup);
    }
}

// //使用示例
// var view1 = new view({
//     isClose: true,
//     duration: 5000,
//     imgUrl: 'path/to/image.jpg',
//     content: '这是文本内容'
// });
//
// var view2 = new view({
//     isClose: false,
//     duration: null,
//     imgUrl: 'path/to/image2.jpg',
//     content: '这是另一个文本内容'
// });