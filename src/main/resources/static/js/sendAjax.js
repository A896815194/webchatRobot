const JS = {};
JS.post = function (url, param, callback, errorCallback) {
    console.log(JSON.stringify(param));
    $.ajax({
        type: 'POST',
        url: url,
        async: false,
        dataType: 'json',
        crossDomain: true,
        data: JSON.stringify(param),
        xhrFields: {
            withCredentials: true
        },
        contentType : 'application/json;charset=utf-8',
        success: function (result) {
            callback(result);
        },
        error: function (xhr, status, error) {
            if (errorCallback !== undefined) {
                errorCallback(xhr, status, error);
            }
        }
    });
};
JS.jumpIndex = function(url,type){
    let form = document.createElement("form");
    $("body").append(form);
    form.id = "jumpForm";
    form.method = type;
    form.action = url;
    form.submit();
}
JS.get = function (url, param, callback, errorCallback) {
    console.log($.param(param));
    $.ajax({
        type: 'GET',
        url: url,
        crossDomain: true,
        xhrFields: {
            withCredentials: true
        },
        async: false,
        dataType: 'json',
        data: $.param(param),
        success: function (result) {
            callback(result);
        },
        error: function (xhr, status, error) {
            if (errorCallback !== undefined) {
                errorCallback(xhr, status, error);
            }
        }
    });
};