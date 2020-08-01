class Request {
	getSubmitByNewForm = function (url: string, $inputTags: JQuery) {
		$('<form/>', { action: url, method: 'get' })
			.append($("<input type='hidden' id='_csrf' name='_csrf' value='" + $('input[name="_csrf"]').val() + "'>"))
			.append($inputTags.html())
			.appendTo('body')
			.submit();
	};
	getSubmit = function (url: string) {
		$('<form/>', { action: url, method: 'get' })
			.appendTo('body')
			.submit();
	};

	getSubmitAddInputs = function (url: string, $inputTags: JQuery) {
		$('<form/>', { action: url, method: 'get' })
			.appendTo('body')
			.submit();
	};

	/** サブミット（post） */
	postSubmit = function (url: string) {
		$('#form').attr('action', url);
		$('#form').attr('method', 'post');
		$('#form').submit();
	}

	postSubmitAddInputs = function (url: string, $inputTags: JQuery) {
		$('#form')
			.attr('action', url)
			.attr('method', 'post')
			.append($inputTags.html())
			.submit();
	};
}

$(function () {
	/** サブミット時の制御 **/
	$("form").on('submit', function (e) {
		var submitName = e.target.attributes.getNamedItem('name');
		var $form = $(this);

		//		$("input.dateFormat", this).each(function() {
		//			var value = $.calendar.formatYyyymmdd($(this).val());
		//			if(String.isNotEmpty(value)) {
		//				$(this).val(value);
		//			}
		//		});

		//		$("input.yearMonth", this).each(function() {
		//			var value = $.yearMonth.formatYyyymm($(this).val());
		//			if(String.isNotEmpty(value)) {
		//				$(this).val(value);
		//			}
		//		});

		//		$("input[class*='Comma']", this).each(function() {
		//			var value =$.currency.formatNoComma($(this).val());
		//			if(String.isNotEmpty(value)) {
		//				$(this).val(value);
		//			}
		//		});

		//		var $multiSelectBoxList = $('select[multiple="multiple"]');
		//		$multiSelectBoxList.each(function() {
		//			$(this).find('option').each(function() {
		//				this.selected = true;
		//			});
		//		});

		$("div#hiddenArea", this).remove();
		var $hiddenArea = $("<div></div>").attr("id", "hiddenArea");

		//		$("select:disabled").each(function(){
		//			var $this = $(this);
		//			var selectedVal = $("option:selected", $this).val();
		//			$("option", $this).each(function(){
		//				$that = $(this);
		//				$that.attr("selected", $that.val() == selectedVal);
		//			});
		//		});

		$(":input:disabled", this).clone(false).removeAttr('disabled').removeAttr('id class').hide().appendTo($hiddenArea);
		//		$multiSelectBoxList.each(function() {
		//			$select = $(this);
		//			$("option:selected", this).each(function(idx){
		//				$option = $(this);
		//				var name = $select.attr('name').replace('KbnList', 'List'); //XxxCdList=>XxxListで受取(命名規約)
		//				$("<input></input>").attr({'type':'hidden', 'name': name + '[' + idx + '].kbn'  }).val($option.val() ).appendTo($hiddenArea);
		//				$("<input></input>").attr({'type':'hidden', 'name': name + '[' + idx + '].name'}).val($option.text()).appendTo($hiddenArea);
		//			});
		//		});

		$hiddenArea.appendTo(this);
	});
});

export { Request };
