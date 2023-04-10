let num = 3;//难度 3 * 3
let step = 0;//记录步数
let time = 1; //记录时间
let setIntervalTime = ''; //定时器
let puzzleImgsSrc = ["/img/puzzle/1.png", "/img/puzzle/2.png", "/img/puzzle/3.png", "/img/puzzle/4.png", "/img/puzzle/5.png", "/img/puzzle/6.png", "/img/puzzle/7.png", "/img/puzzle/9.jpg", "/img/puzzle/10.png", "/img/puzzle/11.png", "/img/puzzle/12.png"] //所有拼图地址
let puzzleImgsNode = [];
let puzzleImgSrc = '';//拼图地址
let ptImg = {};
let newImg = {} //拼图区域
// 二维数组
let ewsz = [];
let spcanvas = [];
// 游戏难度
let level = 0;

//存储开始和结束循序，以此来判断是否过关
let gameStart = [];//开始循序
// 所有拼图
let puzzleImgs = [];
// 是否开始游戏
let istart = 0;
// 空白格子最后一个
var emptyRow = num - 1, emptyCol = num - 1;
let mewImgWidth = 0;
// 通关url
let successUrl = "";
let timeHours = "";
$(function () {
    newImg = $("#newImg")[0];
    let selectImgDiv = $(".selectImg")[0];
    $(selectImgDiv).append("<div class=\"selectImgMsg\"></div>");
    let selectImgMsg = $(".selectImgMsg")[0];
    for (let i = 0; i < puzzleImgsSrc.length; i++) {
        if (i === 0) {
            successUrl = puzzleImgsSrc[i];
            $("#passImg").attr("src", successUrl);
        }
        let img = "<img class=\"puzzleImg\" src=" + puzzleImgsSrc[i] + " alt=\"\">";
        $(selectImgMsg).append(img);
    }
    let newImg1 = document.createElement('img')
    newImg1.setAttribute("id", "startImg")
    newImg1.setAttribute("src", puzzleImgsSrc[0]);
    $("#newImg").append(newImg1);
    ptImg = $("#startImg")[0];
    let imgs = $(".selectImgMsg img");
    for (let i = 0; i < imgs.length; i++) {
        $(imgs[i]).bind("click touchstart", function () {
            if (istart === 1) {
                alert("正在游戏中");
                return;
            }
            puzzleImgSrc = this.src;
            successUrl = puzzleImgSrc;
            $("#passImg").attr("src", successUrl);
            $("#startImg").attr("src", puzzleImgSrc);
            $("#passImg").attr("src", puzzleImgSrc);
            ptImg = $("#startImg")[0];
        })
        puzzleImgsNode.push(imgs[i]);
    }
    $("#speedSuccess").bind("click touchstart", function () {
        if(istart === 1) {
            speedPass();
        }
    });
    imgsLoad(puzzleImgsNode, function () {
        $(".loading").hide();

    });
});


//计时
function timeGame() {
    //对时间格式进行设置
    time++
    if (time < 10) {
        return '00:0' + time
    } else if (60 > time >= 10) {
        return '00:' + time
    } else {
        let fen = Math.floor(time / 60)
        let miao = time % 60
        if (fen < 10 && miao < 10) {
            return '0' + fen + ':0' + miao
        } else if (fen >= 10 && miao < 10) {
            return fen + ':0' + miao
        } else if (fen >= 10 && miao >= 10) {
            return fen + ':' + miao
        } else if (fen < 10 && miao >= 10) {
            return '0' + fen + ':' + miao
        }
    }
}


//开启定时器
function openTimer() {
    setIntervalTime = setInterval(() => {
        document.getElementById('time').innerHTML = "用时: " + timeGame()
    }, 1000)
}

//关闭定时器
function closeTimer() {
    if (setIntervalTime != '') {
        clearInterval(setIntervalTime)
    }
}

var seconds = 0;
var minutes = 0;
var hours = 0;

function startTimer() {
    setInterval(function () {
        seconds++;
        if (seconds == 60) {
            seconds = 0;
            minutes++;
            if (minutes == 60) {
                minutes = 0;
                hours++;
            }
        }
        timeHours = (hours ? (hours > 9 ? hours : "0" + hours) : "00") + ":" + (minutes ? (minutes > 9 ? minutes : "0" + minutes) : "00") + ":" + (seconds > 9 ? seconds : "0" + seconds);
        var timerDisplay = document.getElementById("time");
        timerDisplay.innerHTML = "用时:" + timeHours;// 更新显示时间
    }, 1000);
}


/**
 * 开始游戏
 **/
function startGame() {
    istart = 1;
    $('#time').html("用时: 00:00");

    openTimer()//开启定时器

    $('#startImg').css("display", 'none'); //原图隐藏
    $("#startGame").attr('disabled', "disabled")//开始按钮禁用
    $("#startGame").attr('class', 'disable');//开启禁用标志
    if ($("#speedSuccess")) {
        document.getElementById("speedSuccess").removeAttribute('disabled')//移除禁用标志
        document.getElementById("speedSuccess").className = 'finger'//添加手指标志
    }
    // 重置，暂停按钮开启
    document.getElementById("resetGame").removeAttribute('disabled')//移除禁用标志
    document.getElementById("resetGame").className = 'finger'//添加手指标志
    document.getElementById("suspendGame").removeAttribute('disabled')//移除禁用标志
    document.getElementById("suspendGame").className = 'finger'//添加手指标志
    $("#set").attr('disabled', "disabled")//开始按钮禁用
    $("#set").attr('class', 'disable');//开启禁用标志
    InitPuzzle()//初始化

}

// 打乱拼图数组
function shuffle() {
    for (var i = 0; i < num * num * 10 * (level + 1); i++) { // 进行10*size*size次交换
        var dir = Math.floor(Math.random() * 4); // 随机选择方向
        switch (dir) {
            case 0: // 上
                if (emptyRow > 0) {
                    swap(emptyRow, emptyCol, emptyRow - 1, emptyCol);
                    emptyRow--;
                }
                break;
            case 1: // 下
                if (emptyRow < num - 1) {
                    swap(emptyRow, emptyCol, emptyRow + 1, emptyCol);
                    emptyRow++;
                }
                break;
            case 2: // 左
                if (emptyCol > 0) {
                    swap(emptyRow, emptyCol, emptyRow, emptyCol - 1);
                    emptyCol--;
                }
                break;
            case 3: // 右
                if (emptyCol < num - 1) {
                    swap(emptyRow, emptyCol, emptyRow, emptyCol + 1);
                    emptyCol++;
                }
                break;
        }
    }
}


function InitPuzzle() {
    imgLoad(ptImg, function () {
        let w = ptImg.width / num //计算每一个切图大小 宽
        let h = ptImg.height / num//计算每一个切图大小 高
        mewImgWidth = newImg.clientWidth / num - 5 //保证所有切图可以在盒子中放下
        let divid = 0;
        ewsz = new Array();
        for (let i = 0; i < num; i++) {
            ewsz[i] = new Array();
            for (let j = 0; j < num; j++) {
                //绘制切图，并用div包裹
                $(newImg).append("<div id='div" + divid + "'></div>")
                $("#div" + divid).css("width", mewImgWidth + "px");
                $("#div" + divid).css("height", mewImgWidth + "px");
                $("#div" + divid).css("margin", "1px");  //设置 切图间隙
                //添加 canvas 元素用于绘制切图
                let canvas = document.createElement('canvas')
                canvas.setAttribute('id', 'canvas' + divid) //动态为canvas添加id
                document.getElementById('div' + divid).appendChild(canvas)

                //canvas 的大小必须用属性width，height，通过style添加的无效
                document.getElementById('canvas' + divid).setAttribute('width', mewImgWidth)
                document.getElementById('canvas' + divid).setAttribute('height', mewImgWidth)

                if (!(j == num - 1 && i == num - 1)) {
                    //绘制图片 最后一张留白
                    document.getElementById('canvas' + divid).getContext("2d").drawImage(ptImg, w * j, h * i, w, h, 0, 0, mewImgWidth, mewImgWidth)

                    ewsz[i][j] = $("#canvas" + divid);

                }
                if (j == num - 1 && i == num - 1) {
                    ewsz[i][j] = $("#canvas" + divid);
                }

                let div = document.getElementById("canvas" + divid);
                spcanvas.push(div);
                divid++;//用于设置id
            }
        }
        let ids = $(newImg).find("div");
        if (gameStart.length == 0) {
            for (let index = 0; index < ids.length; index++) {
                gameStart.push(ids[index].id);
                puzzleImgs.push($("#div" + index));
                $("#div" + index).remove();
            }
        }
        shuffle();
        update();
    });

}

// 交换两个元素
function swap(r1, c1, r2, c2) {
    var temp = ewsz[r1][c1];
    ewsz[r1][c1] = ewsz[r2][c2];
    ewsz[r2][c2] = temp;
}

// 检查是否完成拼图
function check() {
    let imgjh = $("#newImg canvas");
    let imgArr = [];
    let spcanArr = [];
    if (imgjh.length == spcanvas.length) {
        for (let i = 0; i < imgjh.length; i++) {
            imgArr.push(imgjh[i].id);
            spcanArr.push(spcanvas[i].id);
        }
    }
    if (imgArr.toString() == spcanArr.toString()) {
        istart = 0;
        return true;

    } else {
        false;
    }


}


function gameSuccessEvent() {
    successUrl = $("#startImg").src;
    $("#passImg").attr("src", successUrl);
    document.querySelector(".pass").style.display = 'block'//显示模态框
    $("#passCount").html("总步数:" + step);
    $("#passTime").html($("#time").html());
    let gxMsg = createTGMsg();
    let sendMsg = {
        "msg": gxMsg,
        "type": "2",
        "action": "gxGame"
    }
    ws.send(JSON.stringify(sendMsg));
}

//移动元素
function move(row, col) {
    if ((row === emptyRow && Math.abs(col - emptyCol) === 1) ||
        (col === emptyCol && Math.abs(row - emptyRow) === 1)) {
        swap(row, col, emptyRow, emptyCol);
        emptyRow = row;
        emptyCol = col;
        update();
        if (istart == 1) {//游戏开始就记录状态
            step++ //步数加一
            document.getElementById('count').innerHTML = "步数: " + step
        }
        if (check()) {
            //设置过关提示
            gameSuccessEvent();

            resetGame();
        }
    }
}

function createTGMsg() {
    return "恭喜【" + gameUser + "】在" + getLevelString() + "难度下使用" + step + "步完成了游戏," + document.getElementById('time').innerHTML;
}

function getLevelString() {
    if (level == 0) {
        return "简单";
    }
    if (level == 1) {
        return "中等";
    }
    if (level == 2) {
        return "困难";
    }

}

// 更新拼图界面
function update() {
    let ids = $("#newImg").find("div");
    for (let index = 0; index < ids.length; index++) {
        $("#div" + index).remove();
    }
    let divid = 0;
    for (let i = 0; i < num; i++) {
        for (let j = 0; j < num; j++) {
            if (divid > (num * num)) {
                break;
            }
            $(newImg).append("<div id='div" + divid + "'></div>")
            $("#div" + divid).css("width", mewImgWidth + "px");
            $("#div" + divid).css("height", mewImgWidth + "px");
            $("#div" + divid).css("margin", "1px");  //设置 切图间隙
            $("#div" + divid).append($(ewsz[i][j]));
            // var div = document.getElementById("div" + divid);
            $("#div" + divid).on("click  touchstart", (function (row, col) {
                return function () {
                    move(row, col);
                };
            })(i, j));
            // div.onclick = (function (row, col) {
            //     return function () {
            //         move(row, col);
            //     };
            // })(i, j);
            divid++;
        }

    }
}


function speedPass() {
    clearImgDiv();
    refillImg();
    alert("爱栗表示很急！！");
    gameSuccessEvent();

}

function refillImg() {
    for (let index = 0; index < spcanvas.length; index++) {
        for (let j = 0; j < num; j++) {
            $("#newImg").append("<div id='div" + index + "'></div>")
            $("#div" + index).css("width", mewImgWidth + "px");
            $("#div" + index).css("height", mewImgWidth + "px");
            $("#div" + index).css("margin", "1px");  //设置 切图间隙
            $("#div" + index).append(spcanvas[index]);
            $("#div" + index).bind("click  touchstart", (function (row, col) {
                return function () {
                    move(row, col);
                };
            })(index, j));
        }
    }
}

function clearImgDiv() {
    let ids = $("#newImg").find("div");
    for (let index = 0; index < ids.length; index++) {
        $("#div" + index).remove();
    }
    $("#startImg").css("display", "none");

}


//游戏设置
function setGame() {
    document.querySelector('.setGame').style.display = 'block'
}


//关闭模态框
function closeModal() {
    document.querySelector('.setGame').style.display = 'none';
    document.querySelector('.pass').style.display = 'none';
    $("#speedSuccess").attr('disabled', "disabled")//开始按钮禁用
    $("#speedSuccess").attr('class', 'disable');//开启禁用标志
    resetGame();

}

//保存游戏设置
function saveSet() {
    let mj = $("#miji").val();
    if (mj === '9435222') {
        $("#speedSuccess").css("display", "block");
        $("#speedSuccess").attr('disabled', "disabled")//开始按钮禁用
        $("#speedSuccess").attr('class', 'disable');//开启禁用标志
    }
    document.querySelector('.setGame').style.display = 'none'

    level = 0
    //获取级别设置结果
    let radios = document.getElementsByName('level')
    for (let i = 0; i < radios.length; i++) {
        //判断选中的级别
        if (radios[i].checked) {
            level = parseInt(radios[i].value)
        }

    }
    //设置级别
    switch (level) {
        case 0:
            num = 3
            break;
        case 1:
            num = 4
            break;
        case 2:
            num = 5
            break;
        default:
            break;
    }
    //游戏已经开始时
    if (istart === 1) {
        resetGame();
    } else {
        resetGame(true);
    }
}


//游戏重置
function resetGame(currentFlag) {
    //关闭定时器
    closeTimer();
    time = 0
    if (currentFlag != undefined) {
        puzzleImgSrc = successUrl;
    } else {
        let element;
        let currentImg = puzzleImgSrc;
        while (true) {
            element = puzzleImgsSrc[Math.floor((Math.random() * puzzleImgsSrc.length))];
            if (currentImg !== element) {
                break;
            }
        }
        puzzleImgSrc = element;
    }
    successUrl = puzzleImgSrc;
    spcanvas = [];
    emptyRow = num - 1, emptyCol = num - 1;
    istart = 0;
    step = 0;
    ewsz = [];
    let ids = $("#newImg").find("div");
    for (let index = 0; index < ids.length; index++) {
        $("#div" + index).remove();
    }
    $("#startImg").css("display", "block");
    $("#startImg").src = successUrl;
    $("#count").html("步数: " + step);
    $("#time").html("用时: 00:00");
    $("#passCount").html("总步数:" + step);
    $("#passTime").html($("#time").html());
    //jstodo
    ptImg = $("#startImg")[0];
    document.getElementById("startGame").removeAttribute('disabled')//移除禁用标志
    document.getElementById("startGame").className = 'finger'//添加手指标志
    $("#suspendGame").attr('disabled', "disabled")//开始按钮禁用
    $("#suspendGame").attr('class', 'disable');//开启禁用标志
    $("#resetGame").attr('disabled', "disabled")//开始按钮禁用
    $("#resetGame").attr('class', 'disable');//开启禁用标志
    document.getElementById("set").removeAttribute('disabled')//移除禁用标志
    document.getElementById("set").className = 'finger'//添加手指标志

}


//暂停
function suspendGame(state) {
    //暂停
    if (state) {
        //清除定时器
        closeTimer()
        //开启模态框
        document.querySelector('.suspend').style.display = 'block'
    } else {
        //开始
        //关闭模态框
        document.querySelector('.suspend').style.display = 'none'
        // 开启定时器
        openTimer()
    }
}