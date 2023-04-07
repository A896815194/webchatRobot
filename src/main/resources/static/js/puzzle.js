let num = 3//难度 3 * 3 
let count = 0//记录步数
let time = 1 //记录时间
let puzzleImgsSrc = [] //所有拼图地址
let puzzleImgSrc = '' //拼图地址
let setIntervalTime = '' //定时器
let img = document.getElementById("startImg")
let newImg = document.getElementById('newImg') //拼图区域
let a = 0
//存储开始和结束循序，以此来判断是否过关
let gameStart = []//开始循序
let gameOld = []//结束循序

//选择所有拼图添加事件
let puzzleImgs = document.querySelector('.selectImgMsg').children //获取所有拼图

for (let i = 0; i < puzzleImgs.length; i++) {
  const dom = puzzleImgs[i];
  //获取拼图地址
  if (dom.classList.length > 0) {
    puzzleImgsSrc.push(dom.attributes[1].nodeValue)
  }
  else {
    puzzleImgsSrc.push(dom.attributes[0].nodeValue)
  }

  //为每一个拼图添加事件选择事件
  dom.onclick = function () {
    //替换拼图
    puzzleImgSrc = puzzleImgsSrc[i]
    document.getElementById('startImg').src = puzzleImgSrc //原图替换
    document.getElementById('passImg').src = puzzleImgSrc //过关图片替换

    // 判断是否已开始游戏 如果已经开始，游戏复位
    let dom = document.getElementById('startGame')
    if (dom.disabled) {
      //关闭定时器
      closeTimer()
      a = 0
      time = 1
      document.getElementById('count').innerHTML = "步数: " + 0
      document.getElementById('time').innerHTML = "用时: 00:00"

      //重新选择拼图，页面重绘
      newImg.innerHTML = ''
      let img = document.createElement('img')
      img.setAttribute("id", "startImg")
      img.setAttribute("src", puzzleImgSrc)
      newImg.appendChild(img)
      dom.className = "finger"
      dom.removeAttribute('disabled') 
      document.getElementById('resetGame').classList.add("disable")
      document.getElementById('suspendGame').classList.add("disable")
    }
  }
}
//随机产生的拼图地址
puzzleImgSrc = puzzleImgsSrc[Math.floor(Math.random()*7)];
document.getElementById('startImg').src = puzzleImgSrc //原图替换
    document.getElementById('passImg').src = puzzleImgSrc //过关图片替换

//初始化
function InitPuzzle() {
  let w = img.width / num //计算每一个切图大小 宽
  let h = img.height / num//计算每一个切图大小 高

  let imgNewW = newImg.clientWidth / num - 5 //保证所有切图可以在盒子中放下

  for (let i = 0; i < num; i++) {
    for (let j = 0; j < num; j++) {
      //绘制切图，并用div包裹
      let div = document.createElement('div')
      div.setAttribute('id', 'div' + a) //动态为div添加id
      newImg.appendChild(div)

      document.getElementById('div' + a).style.width = imgNewW + 'px' //设置 宽度
      document.getElementById('div' + a).style.height = imgNewW + 'px'//设置 高度
      document.getElementById('div' + a).style.margin = 1 + 'px' //设置 切图间隙
      //添加 canvas 元素用于绘制切图
      let canvas = document.createElement('canvas')
      canvas.setAttribute('id', 'canvas' + a) //动态为canvas添加id
      document.getElementById('div' + a).appendChild(canvas)

      //canvas 的大小必须用属性width，height，通过style添加的无效
      document.getElementById('canvas' + a).setAttribute('width', imgNewW)
      document.getElementById('canvas' + a).setAttribute('height', imgNewW)

      // //动态为canvas添加id
      // document.getElementsByTagName('canvas')[a].id = 'canvas' + a

      //创建点击事件用于移动
      document.getElementById('div' + a).onclick = function () {
        //判断是否可以移动，并移动
        Blank(this.offsetLeft, this.offsetTop, this)
      }

      if (!(j == num - 1 && i == num - 1)) {
        //绘制图片 最后一张留白
        document.getElementById('canvas' + a).getContext("2d").drawImage(img, w * j, h * i, w, h, 0, 0, imgNewW, imgNewW)
      }
      a++;//用于设置id
    }
  }
}

/**
 * 空白区域位置与滑动区域比较判断是否可以移动
 * 
 *  width:切图与左边框的距离
 *  height:切图与上边框的距离
 *  clickImg: 切图dom
 *  isStart: 是否为点击开始按钮时调用的，默认 false
 **/
function Blank(width, height, clickImg, isStart = false) {
  //留白切图dom
  let blank = document.getElementById('div' + (num * num - 1))

  //判断与留白切图 x轴或y轴是否有相同(相同才有可能移动)
  if (width == blank.offsetLeft || height == blank.offsetTop) {
    if (width == blank.offsetLeft) {//当x轴相同时 进一步判断是否是相邻切图（相邻才能移动）
      if ((blank.offsetTop - height) / clickImg.clientWidth < 2) {

        moveDiv(clickImg.id, blank.id)
        if (!isStart) {//不是点击开始按钮时调用
          count++ //步数加一
          document.getElementById('count').innerHTML = "步数: " + count
          successGame()//判断是否过关
        }
      }
    }
    else {//当y轴相同时 进一步判断是否是相邻切图（相邻才能移动）
      if ((blank.offsetLeft - width) / clickImg.clientWidth < 2) {
        moveDiv(clickImg.id, blank.id)//调用移动函数
        if (!isStart) {//不是点击开始按钮时调用
          count++ //步数加一
          document.getElementById('count').innerHTML = "步数: " + count
          successGame()//判断是否过关
        }
      }
    }
  }
  else {
    return false
  }
}

/**
 * 移动，与空白区域互换
 * 
 *  id_1:第一个div id
 *  id_2:第二个div id
 **/
function moveDiv(id_1, id_2) {
  var insert = function (nodeInsert, nodeTo) {
    if (nodeInsert.insertAdjacentElement) {
      nodeTo.insertAdjacentElement('beforeBegin', nodeInsert);
    }
    else {
      nodeTo.parentNode.insertBefore(nodeInsert, nodeTo);
    }
  }
  //通过第三个元素来实现交换
  var obj = document.createElement("a");
  var t1 = document.getElementById(id_1);
  var t2 = document.getElementById(id_2);
  insert(obj, t2);
  insert(t2, t1);
  insert(t1, obj);
  //移除第三个元素
  newImg.removeChild(newImg.getElementsByTagName('a')[0])
}

/**
 * 
 * 判断闯关是否成功
 * 
 * **/
function successGame() {
  let ids = newImg.getElementsByTagName("div")
  gameOld = []//将结束时的顺序清空重新赋值防止数据重复
  for (let index = 0; index < ids.length; index++) {
    gameOld.push(ids[index].id)
  }
  // 判断是否过关，并给出过关提示
  if (gameOld.toString() == gameStart.toString()) {
    closeTimer()//关闭定时器
    //设置过关提示
    document.getElementById("passCount").innerHTML = '总步数: ' + count
    document.getElementById("passTime").innerHTML = '用时: ' + time + '秒'
    document.querySelector(".pass").style.display = 'block'//显示模态框
    
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


/**
* 开始游戏
**/
function startGame() {
  document.getElementById('time').innerHTML = "用时: 00:00"

  openTimer()//开启定时器

  document.getElementById('startImg').style.display = 'none' //原图隐藏
  document.getElementById("startGame").setAttribute('disabled', "disabled")//开始按钮禁用
  document.getElementById("startGame").className = 'disable'//开启禁用标志

  // 重置，暂停按钮开启
  document.getElementById("resetGame").removeAttribute('disabled')//移除禁用标志
  document.getElementById("resetGame").className = 'finger'//添加手指标志
  document.getElementById("suspendGame").removeAttribute('disabled')//移除禁用标志
  document.getElementById("suspendGame").className = 'finger'//添加手指标志


  InitPuzzle()//初始化

  gameOld = []
  let ids = newImg.getElementsByTagName("div")
  if (gameStart.length == 0) {
    for (let index = 0; index < ids.length; index++) {
      gameStart.push(ids[index].id)
    }
  }
  let blank = document.getElementById('div' + (num * num - 1))

  // 生成随机数，打乱顺序
  for (let i = 0; i < Math.random() * 100; i++) {
    for (let j = 0; j < num * num - 1; j++) {
      let div = document.getElementById('div' + j)
      // 在可移动范围打乱顺序防止无法复原问题
      Blank(div.offsetLeft, div.offsetTop, div, true)
    }
  }
  for (let index = 0; index < ids.length; index++) {
    gameOld.push(ids[index].id)

  }
  if (gameOld.toString() == gameStart.toString()) {
    startGame()//防止切图没有打乱
  }
}

//游戏重置
function resetGame() {
  //关闭定时器
  closeTimer()
  a = 0
  time = 1
  document.getElementById('count').innerHTML = "步数: " + 0
  document.getElementById('time').innerHTML = "用时: 00:00"

  newImg.innerHTML = ''
  let img = document.createElement('img')
  img.setAttribute("id", "startImg")
  img.setAttribute("src", puzzleImgSrc)
  newImg.appendChild(img)
  startGame()
}

//计时
function timeGame() {
  //对时间格式进行设置
  time++
  if (time < 10) {
    return '00:0' + time
  }
  else if (60 > time >= 10) {
    return '00:' + time
  }
  else {
    let fen = Math.floor(time / 60)
    let miao = time % 60
    if (fen < 10 && miao < 10) {
      return '0' + fen + ':0' + miao
    }
    else if (fen >= 10 && miao < 10) {
      return fen + ':0' + miao
    }
    else if (fen >= 10 && miao >= 10) {
      return fen + ':' + miao
    }
    else if (fen < 10 && miao >= 10) {
      return '0' + fen + ':' + miao
    }
  }
}

//暂停
function suspendGame(state) {
  //暂停
  if (state) {
    //清除定时器
    closeTimer()
    //开启模态框
    document.querySelector('.suspend').style.display = 'block'
  }
  else {
    //开始
    //关闭模态框
    document.querySelector('.suspend').style.display = 'none'
    // 开启定时器
    openTimer()
  }
}


//游戏设置
function setGame() {
  document.querySelector('.setGame').style.display = 'block'
}


//关闭模态框
function closeModal() {
  document.querySelector('.setGame').style.display = 'none'
  document.querySelector('.pass').style.display = 'none'
}

//保存游戏设置
function saveSet() {
  document.querySelector('.setGame').style.display = 'none'

  let level = 0
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
  if (document.getElementById('startGame').disabled) {
    resetGame()
  }
}








