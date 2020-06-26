$(function() {
	
	/** テーブル登録画面に遷移 **/
	$("#createTable").on("click", function(){
		var $div = $('<div/>');
		var keySpace = $("#keySpace").val();
		$div.append($("<input type='hidden' id='keySpace' name='keySpace' value='" + keySpace + "'>"));
		getSubmitByNewForm("/tableRegister", $div);
	});
	
	/** キースペース削除 **/
	$("#deleteKeySpace").on("click", function(){
		postSubmit("/keySpaceUpdater/delete");
	});
});
