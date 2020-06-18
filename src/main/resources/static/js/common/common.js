$(function() {
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

		// BackSpaceキーの制御
		if (keyCode == 8 && (obj.getAttribute("type") != "text"
							&& obj.getAttribute("") != "search"
							&& obj.getAttribute("") != "tel"
							&& obj.getAttribute("") != "url"
							&& obj.getAttribute("") != "email"
							&& obj.getAttribute("type") != "password"
							&& obj.getAttribute("") != "datetime"
							&& obj.getAttribute("") != "number"
							&& obj.tagName != "TEXTAREA")) {
			return false;
		}

		if (keyCode == 32 && (obj.getAttribute("type") != "text"
							&& obj.getAttribute("") != "search"
							&& obj.getAttribute("") != "tel"
							&& obj.getAttribute("") != "url"
							&& obj.getAttribute("") != "email"
							&& obj.getAttribute("type") != "password"
							&& obj.getAttribute("type") != "checkbox"
							&& obj.getAttribute("") != "datetime"
							&& obj.getAttribute("") != "number"
							&& obj.tagName != "TEXTAREA"
							&& obj.getAttribute("type") != "button"
							&& obj.getAttribute("type") != "submit"
							&& obj.getAttribute("type") != "reset")) {
			return false;
		}

		if (keyCode == 13 && !(obj.getAttribute("type") == "button"
							|| obj.getAttribute("type") == "submit"
							|| obj.getAttribute("type") == "reset"
							|| obj.tagName != "a")) {
			return false;
		}

		// バックスペースキーを制御する
		if(keyCode == 8){
			// テキストボックス／テキストエリアを制御する
			if((obj.tagName == "INPUT" && (obj.type == "text" || obj.type == "TEXT" || obj.type == "password" || obj.type == "PASSWORD"))
					|| obj.tagName == "TEXTAREA"){
				// 入力できる場合は制御しない
				if(!obj.readOnly && !obj.disabled){
					return true;
				}
			}
			return false;
		}

		// deleteキーを制御する
		if(keyCode == 46){
			// マルチセレクトボックスを制御する
			if(obj.tagName == "SELECT" && obj.type == "select-multiple"){
				// セレクトボックスまたは子画面表示ボタンが活性の場合は選択された項目を削除
				var $parent = $(obj).closest("td");
				var $button = $parent.find("input[type='button']");
				if(!obj.readOnly && !obj.disabled && !$button.prop("disabled")){
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
	selectorEscape = function(val) {
		return val.replace(/[ !"#$%&'()*+,.\/:;<=>?@\[\\\]^`{|}~]/g, '\\$&');
	}

	$('input[type="submit"]').on('click', function(e) {
		var form = $(this).closest("form");
		var submitName = e.target.name;
		form.attr("name", submitName);
		return;
	});

	$('input[type="button"]').on('click', function(e) {
		var form = $(this).closest("form");
		var submitName = e.target.name;
		form.attr("name", submitName);
		return;
	});

	$("form").on('submit', function(e) {
		var submitName = e.target.name;
		if (null == submitName.match(/nonBlock/)) {
			blockUI();
		}

		var $form = $(this);

		$("input.dateFormat", this).each(function() {
			var value = $.calendar.formatYyyymmdd($(this).val());
			if(String.isNotEmpty(value)) {
				$(this).val(value);
			}
		});
		$("input.yearMonth", this).each(function() {
			var value = $.yearMonth.formatYyyymm($(this).val());
			if(String.isNotEmpty(value)) {
				$(this).val(value);
			}
		});
		$("input[class*='Comma']", this).each(function() {
			var value =$.currency.formatNoComma($(this).val());
			if(String.isNotEmpty(value)) {
				$(this).val(value);
			}
		});

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
			var selectedVal = $("option:selected", $this).val();
			$("option", $this).each(function(){
				$that = $(this);
				$that.attr("selected", $that.val() == selectedVal);
			});
		});

		$(":input:disabled", this).clone(false).attr('disabled', false).removeAttr('id class').hide().appendTo($hiddenArea);
		$multiSelectBoxList.each(function() {
			$select = $(this);
			$("option:selected", this).each(function(idx){
				$option = $(this);
				var name = $select.attr('name').replace('KbnList', 'List'); //XxxCdList=>XxxListで受取(命名規約)
				$("<input></input>").attr({'type':'hidden', 'name': name + '[' + idx + '].kbn'  }).val($option.val() ).appendTo($hiddenArea);
				$("<input></input>").attr({'type':'hidden', 'name': name + '[' + idx + '].name'}).val($option.text()).appendTo($hiddenArea);
			});
		});
		$hiddenArea.appendTo(this);
	});
});
