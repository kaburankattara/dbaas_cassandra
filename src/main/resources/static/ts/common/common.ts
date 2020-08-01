import * as ns from "jquery"
import { StringUtils } from "../utils/stringUtils";
import { BlockUI } from "../common/blockUi";

$(function () {

	// 右クリックの禁止
	$(document).on("contextmenu", function(e) {
		return false;
	});
	// ------------------------
	// jQueryキー制御
	// backSpace、Alt+←→、F5を制限
	// ------------------------
	$(document).keydown(function(event){
		// クリックされたキーのコード
		var keyCode = event.keyCode;
		// Ctrlキーがクリックされたか (true or false)
		var ctrlClick = event.ctrlKey;
		// Altキーがクリックされたか (true or false)
		var altClick = event.altKey;
		// キーイベントが発生した対象のオブジェクト
		var obj = event.target;

		// ファンクションキーを制御する
		// 制限を掛けたくない場合は対象から外す
		if(keyCode == 116 // F5キーの制御
//				|| keyCode == 112 // F1キーの制御
//				|| keyCode == 113 // F2キーの制御
//				|| keyCode == 114 // F3キーの制御
//				|| keyCode == 115 // F4キーの制御
//				|| keyCode == 116 // F5キーの制御
//				|| keyCode == 117 // F6キーの制御
//				|| keyCode == 122 // F11キーの制御
//				|| keyCode == 123 // F12キーの制御
		) {
			return false;
		}

		let tagName = obj.documentElement.tagName;
		let type = obj.documentElement.attributes.getNamedItem("type")?.value;
		let isReadOnly = obj.documentElement.attributes.getNamedItem("readOnly")?.value;
		let isDisabled = obj.documentElement.attributes.getNamedItem("disabled")?.value;
		// BackSpaceキーの制御
		if (keyCode == 8 && (type != "text"
							// && type != "search"
							// && type != "tel"
							// && type != "url"
							// && type != "email"
							&& type != "password"
							// && type != "datetime"
							// && type != "number"
							&& tagName != "TEXTAREA")) {
			return false;
		}
		// スペースキーの制御
		if (keyCode == 32 && (type != "text"
							// && obj.getAttribute("") != "search"
							// && obj.getAttribute("") != "tel"
							// && obj.getAttribute("") != "url"
							// && obj.getAttribute("") != "email"
							&& type != "password"
							&& type != "checkbox"
							// && obj.getAttribute("") != "datetime"
							// && obj.getAttribute("") != "number"
							&& tagName != "TEXTAREA"
							&& type != "button"
							&& type != "submit"
							&& type != "reset")) {
			return false;
		}
		// エンターキーの制御
		if (keyCode == 13 && !(type == "button"
							|| type == "submit"
							|| type == "reset"
							|| tagName != "a")) {
			return false;
		}

		// バックスペースキーを制御する
		if(keyCode == 8){
			// テキストボックス／テキストエリアを制御する
			if((tagName == "INPUT" && (type == "text" || type == "TEXT" || type == "password" || type == "PASSWORD"))
					|| tagName == "TEXTAREA"){
				// 入力できる場合は制御しない
				if(!isReadOnly && !isDisabled){
					return true;
				}
			}
			return false;
		}

		// deleteキーを制御する
		if(keyCode == 46){
			// マルチセレクトボックスを制御する
			if(tagName == "SELECT" && type == "select-multiple"){
				// セレクトボックスまたは子画面表示ボタンが活性の場合は選択された項目を削除
				var $parent = $(obj).closest("td");
				var $button = $parent.find("input[type='button']");
				if(!isReadOnly && !isDisabled && !$button.prop("disabled")){
					$(obj).children("option:selected").each(function() {
						$(this).remove();
					});
					return false;
				}
			}
		}

		// Alt + ←→を制御する
		if(altClick && (keyCode == 37 || keyCode == 39)){
			return false;
		}

//		// Ctrl + Nを制御する
//		if(ctrlClick && keyCode == 78){
//			return false;
//		}
	});

	/** セレクタ用エスケープ */
	const selectorEscape = function(val: string) {
		return val.replace(/[ !"#$%&'()*+,.\/:;<=>?@\[\\\]^`{|}~]/g, '\\$&');
	}

	$('input[type="submit"]').on('click', function(e) {
		var form = $(this).closest("form");
		var submitName:string = StringUtils.toString(e.target.getAttributeNames);
		form.attr("name", submitName);
		return;
	});

	$('input[type="button"]').on('click', function(e) {
		var form = $(this).closest("form");
		var submitName = StringUtils.toString(e.target.getAttributeNames);
		form.attr("name", submitName);
		return;
	});

	$("form").on('submit', function(e) {
		var submitName = StringUtils.toString(e.target.getAttributeNames);
		if (null == submitName.match(/nonBlock/)) {
			let blockUI = new BlockUI();
			blockUI.blockUI;
		}

		var $form = $(this);

		// $("input.dateFormat", this).each(function() {
		// 	var value = $.calendar.formatYyyymmdd($(this).val());
		// 	if(stringUtils.isNotEmpty(value)) {
		// 		$(this).val(value);
		// 	}
		// });
		// $("input.yearMonth", this).each(function() {
		// 	var value = $.yearMonth.formatYyyymm($(this).val());
		// 	if(stringUtils.isNotEmpty(value)) {
		// 		$(this).val(value);
		// 	}
		// });
		// $("input[class*='Comma']", this).each(function() {
		// 	var value =$.currency.formatNoComma($(this).val());
		// 	if(stringUtils.isNotEmpty(value)) {
		// 		$(this).val(value);
		// 	}
		// });

		var $multiSelectBoxList = $('select[multiple="multiple"]');
		$multiSelectBoxList.each(function() {
			$(this).find('option').each(function() {
				this.selected = true;
			});
		});

		$("div#hiddenArea", this).remove();
		var $hiddenArea = $("<div></div>").attr("id", "hiddenArea");

		$("select:disabled").each(function(){
			var $this = $(this);
			var selectedVal = $("option:selected", $this).val;
			$("option", $this).each(function(){
				var $that = $(this);
				var isSelected: string = $that.val == selectedVal ? "true": "";
				$that.attr("selected", isSelected);
			});
		});

		$(":input:disabled", this).clone(false).attr('disabled', StringUtils.false).removeAttr('id class').hide().appendTo($hiddenArea);
		$multiSelectBoxList.each(function() {
			var $select = $(this);
			$("option:selected", this).each(function (idx) {
				var $option = $(this);
				var name = $select.attr('name')!.replace('KbnList', 'List'); //XxxCdList=>XxxListで受取(命名規約)
				$("<input></input>").attr({'type':'hidden', 'name': name + '[' + idx + '].kbn'  }).val(StringUtils.toString($option.val)).appendTo($hiddenArea);
				$("<input></input>").attr({'type':'hidden', 'name': name + '[' + idx + '].name'}).val($option.text()).appendTo($hiddenArea);
			});
		});
		$hiddenArea.appendTo(this);
	});
});
