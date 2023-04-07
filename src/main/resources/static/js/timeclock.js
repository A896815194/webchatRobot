$(function(){
    // 计算时间差
    var targetDate = new Date("2023/04/03  00:00:00");
    var daysElem = $("#days");
    var hoursElem = $("#hours");
    var minutesElem = $("#minutes");
    var secondsElem = $("#seconds");

    function updateCountdown() {
        var now = new Date();
        var diff = now.getTime()-targetDate.getTime();

        var days = Math.floor(diff / (1000 * 60 * 60 * 24));
        var hours = Math.floor((diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        var minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));
        var seconds = Math.floor((diff % (1000 * 60)) / 1000);

        daysElem.text(days);
        hoursElem.text(hours);
        minutesElem.text(minutes);
        secondsElem.text(seconds);
    }

    // 更新计时器
    setInterval(updateCountdown, 1000);
});