import { StringUtils } from "../utils/stringUtils.js";
var Ajax = /** @class */ (function () {
    function Ajax() {
    }
    Ajax.errorCallBack = function (data) {
        $('#errorMessage').html("システムに問題が発生しました。管理者へお問い合わせください");
    };
    Ajax.doAsyncPost = function (url, form, callBack) {
        form._csrf = StringUtils.toString($('input[name="_csrf"]').val);
        $.ajax({
            method: "POST",
            async: true,
            url: url,
            data: form
        })
            .then(callBack, Ajax.errorCallBack);
    };
    Ajax.doAsyncGet = function (url, form, callBack) {
        $.ajax({
            type: 'GET',
            async: true,
            url: url,
            data: form
        })
            .then(callBack, Ajax.errorCallBack);
    };
    Ajax.doSyncPost = function (url, form, callBack) {
        form._csrf = StringUtils.toString($('input[name="_csrf"]').val);
        $.ajax({
            type: 'POST',
            async: false,
            url: url,
            data: form
        })
            .then(callBack, Ajax.errorCallBack);
    };
    Ajax.doSyncGet = function (url, form, callBack) {
        $.ajax({
            type: 'GET',
            async: false,
            url: url,
            data: form
        })
            .then(callBack, Ajax.errorCallBack);
    };
    Ajax.doAsyncPostJson = function (url, form, callBack) {
        $.ajax({
            type: "POST",
            async: true,
            url: url,
            data: JSON.stringify(form),
            contentType: 'application/json',
            dataType: "json",
            headers: {
                'X-CSRF-Token': StringUtils.toString($('input[name="_csrf"]').val)
            }
        })
            .then(callBack, Ajax.errorCallBack);
    };
    Ajax.doSyncPostJson = function (url, form, callBack) {
        $.ajax({
            type: "POST",
            async: false,
            url: url,
            data: JSON.stringify(form),
            contentType: 'application/json',
            dataType: "json",
            headers: {
                'X-CSRF-Token': StringUtils.toString($('input[name="_csrf"]').val)
            }
        })
            .then(callBack, Ajax.errorCallBack);
    };
    return Ajax;
}());
;
var AjaxObject = /** @class */ (function () {
    function AjaxObject(url, data, callBack) {
        this.url = url;
        this.data = data;
        this.callBack = callBack;
    }
    return AjaxObject;
}());
;
var AjaxForm = /** @class */ (function () {
    function AjaxForm() {
        this._csrf = "";
    }
    return AjaxForm;
}());
;
export { Ajax, AjaxForm };
