function sendMsg(){
 let msg = $("#msg").val();
 if(msg===''){
     return;
 }
 let sendMsg={
   "msg":msg,
   "type":"2",
   "action":"broadcast"
 }
 ws.send(JSON.stringify( sendMsg ));
 $("#msgBox").append("<p class='lt'><span class='rm'>我 ：</span><span class='nr'>"+msg+"</span></p>");
    $("#msg").val("");
    $("#msgBox")[0].scrollTop = $("#msgBox")[0].scrollHeight;
}

$("#msg").keydown(function (event) {
    if (event.keyCode == 13) {
        sendMsg();
    }
});

document.onkeydown = function (e) { // 回车提交表单
// 兼容FF和IE和Opera
    var theEvent = window.event || e;
    var code = theEvent.keyCode || theEvent.which || theEvent.charCode;
    if (code == 13) {
        sendMsg();
    }
}