import { Request } from "../common/request.js";
$(function () {
    /** テーブル登録画面に遷移 **/
    $("#createTable").on("click", function () {
        // 登録画面に遷移するためのForm情報を作成
        var $div = $('<div/>');
        var keySpace = $("#keySpace").val();
        $div.append($("<input type='hidden' id='keySpace' name='keySpace' value='" + keySpace + "'>"));
        // リクエストを実行
        var request = new Request();
        request.getSubmitByNewForm("/tableRegister", $div);
    });
    /** キースペース削除 **/
    $("#deleteKeySpace").on("click", function () {
        var request = new Request();
        request.postSubmit("/keySpaceUpdater/delete");
    });
});
