var JsCookie = {}
//设置cookie
JsCookie.setCookie = function (c_name, value, expiredays) {
    var exdate = new Date()
    exdate.setDate(exdate.getDate() + expiredays)
    document.cookie = c_name + "=" + escape(value) +
        ((expiredays == null) ? "" : ";expires=" + exdate.toGMTString())
}

//取回cookie
JsCookie.getCookie = function (c_name) {
    var d;
    var b = document.cookie;
    // console.log(b)
    var c = b.split(";");
    for (let e = 0; e < c.length; e++) {
        var f = c[e].split("=");
        if (c_name== f[0].toString().trim()) {
            d = f[1];
            break;
        }
    } if (void 0 == d || null == d) {
        return "";
    }
    else {
        var g = unescape(d.trim());
        return g;
    }
}

JsCookie.delCookie = function (c_name) {
    var b = new Date(0).toGMTString();
    document.cookie = c_name + "=;expires=" + b + ";path=/";
    }