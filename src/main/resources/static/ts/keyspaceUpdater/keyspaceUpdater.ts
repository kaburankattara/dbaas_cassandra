import { Request } from "../common/request.js";

$(function () {
	/** テーブル登録画面に遷移 **/
	$("#createTable").on("click", function () {
		// 登録画面に遷移するためのForm情報を作成
		var $div = $('<div/>');
		var keyspace = $("#keyspace").val();
		$div.append($("<input type='hidden' id='keyspace' name='keyspace' value='" + keyspace + "'>"));

		// リクエストを実行
		var request = new Request();
		request.getSubmitByNewForm("/tableRegister", $div);
	});
	
	/** キースペース削除 **/
	$("#deleteKeyspace").on("click", function () {
		var request = new Request();
		request.postSubmit("/keyspaceUpdater/delete");
	});

	/** 遷移元であるキースペース更新画面に遷移する **/
	$("#referrer").on("click", function(){
		var request = new Request();
		request.getSubmit("/keyspaceList");
	});
});
