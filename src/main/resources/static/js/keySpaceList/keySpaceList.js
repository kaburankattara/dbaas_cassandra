$(function() {
	
	/** サブミット（get） */
	function getSubmitByNewForm(url) {
		$('<form/>', {action: url, method: 'get'})
		.appendTo('body')
		.submit();
	}
	/** サブミット（post） */
	function postSubmit(url) {
		$('#form').attr('action', url);
		$('#form').attr('method', 'post');
		$('#form').submit();
	}
	
	/** キースペース登録画面に遷移 **/
	$("#keySpaceRegister").on("click", function(){
		getSubmitByNewForm("/keySpaceRegister");
	});
	
	var isCompleteCreateServer = function(){
		var url = "/isCompleteKeyspaceRegist";
		var param = {
				keySpace : $("#keySpace").val()
		}
		var callBack = function(completeKeySpaceList) {
			for(index in completeKeySpaceList){
				var $rowKeySpace = $("#row_" + completeKeySpaceList[index]);
				$rowKeySpace.find(".statusPending").each(function(index, e) {
					$(e).addClass('displayNone');
				});
				$rowKeySpace.find(".statusComplete").each(function(index, e) {
					$(e).removeClass('displayNone');
				});
			}
		}
		doAsyncGet(url, param, callBack);
	}
	setInterval(isCompleteCreateServer, 1000);
	
});
