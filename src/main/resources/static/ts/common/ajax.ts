import { StringUtils } from "../utils/stringUtils.js";

class Ajax {

	static errorCallBack(data: object): void {
		$('#errorMessage').html("システムに問題が発生しました。管理者へお問い合わせください");
	}

	static doAsyncPost = function (url: string, form: AjaxForm, callBack: (msg: object) => void) {
		form._csrf = StringUtils.toString($('input[name="_csrf"]').val);

		$.ajax({
			method: "POST",
			async: true,
			url: url,
			data: form
		})
			.then(callBack, Ajax.errorCallBack);
	};

	static doAsyncGet = function (url: string, form: AjaxForm, callBack: (msg: object) => void) {
		$.ajax({
			type: 'GET',
			async: true,
			url: url,
			data: form
		})
			.then(callBack, Ajax.errorCallBack);
	};

	static doSyncPost = function (url: string, form: AjaxForm, callBack: (msg: object) => void) {
		form._csrf = StringUtils.toString($('input[name="_csrf"]').val);

		$.ajax({
			type: 'POST',
			async: false,
			url: url,
			data: form
		})
			.then(callBack, Ajax.errorCallBack);
	};

	static doSyncGet = function (url: string, form: AjaxForm, callBack: (msg: object) => void) {
		$.ajax({
			type: 'GET',
			async: false,
			url: url,
			data: form
		})
			.then(callBack, Ajax.errorCallBack);
	};

	static doAsyncPostJson = function (url: string, form: AjaxForm, callBack: (msg: object) => void) {
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

	static doSyncPostJson = function (url: string, form: AjaxForm, callBack: (msg: object) => void) {
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
};

class AjaxObject {
	url: string;
	data: object;
	callBack: Function;

	constructor(url: string, data: object, callBack: Function) {
		this.url = url;
		this.data = data;
		this.callBack = callBack;
	}
};
class AjaxForm {
	_csrf: string = "";

	constructor() {}

	// constructor(_csrf: string) {
	// 	this._csrf = _csrf;
	// }
};

export { Ajax, AjaxForm };