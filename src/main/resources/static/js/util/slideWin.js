
$(document).ready(function () {
    var x = 2;
    const appDiv = document.querySelector('#app');
    const bDiv = document.querySelector('.menu-body');
// 获取app容器的左边界位置
    const appLeft = appDiv.offsetLeft;
    const appHeight = appDiv.clientHeight
// 计算b元素应该在左边界中间的位置
    const bTop = appHeight / 3;
// 设置b元素的left属性
    bDiv.style.left = appLeft + 'px';
    bDiv.style.top = bTop+ 'px';
    $(".menu-btn").click(function () {
        x += 1;
        if (x % 2 == 0) {
            $(".sidemenu").css("display","none");
            $(".sidemenu span").css({
                "opacity":"0",
                "display":"none",
            });

            $(".sidemenu,.slidecontent").css({
                "width": "0px",
                "opacity":"0",
            });
            // $(".menu-body").animate({
            //     "width": "50px",
            // }, 250, function () {
            //     $(".menu-body").css("height","100px");
            // });

            $(".menu-body").css({
                "background-color": "rgba(46, 136, 186, 0)",
                "width":0
            });
        }
        else {

            // test
            $(".sidemenu span").css({
                "display":"initial",
            });
            $(".sidemenu,.slidecontent").css({
                "width": "140",
                "opacity": "1",
            },250);
            $(".sidemenu span").css({
                "opacity":"1",
            });
            $(".menu-body").css({
                "width": "200px",
                // "height":"460px",
            },500);
            $(".sidemenu").css("display","block");
            $(".menu-body").css({
                "background-color":"rgba(46, 136, 186, 0.51)",
            }, 250);
        }

    });

});