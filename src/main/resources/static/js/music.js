$(function () {
    const ap = new APlayer({
        container: document.getElementById('aplayer'),
        listFolded: true,
        // autoplay:true,
        theme:'purple',
        preload:true,
        //fixed:true,
        audio: [
            {
                name: '想见你想见你想见你',
                artist: '张杰',
                url: '../music/x.mp3',
                cover: '../img/qin.png'
            },
            {
                name: '故事细腻',
                artist: '林俊杰',
                url: '../music/l.mp3',
                cover: '../img/baotou.png'
            },
            {
                name: '陪你度过漫长岁月',
                artist: '陈奕迅',
                url: '../music/c.mp3',
                cover: '../img/qinqin.png'
            },
            {
                name: '唯一',
                artist: '告五人',
                url: '../music/w.mp3',
                cover: '../img/bixin.png'
            }
        ]
    });
});