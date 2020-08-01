import { Request } from "../common/request.js";
$(function () {
    /** カラムの入力項目を追加する **/
    $("#addColumn").on("click", function () {
        var request = new Request();
        request.postSubmit("/tableRegister/addColumn");
    });
    /** カラムの入力項目を削除する **/
    $(".deleteColumn").on("click", function (e) {
        // 削除対象のカラム番号をformに追加
        var $div = $('<div/>');
        var $parentTd = $(e.target).closest("td");
        var index = $parentTd.find('#deleteTargetIndex').val();
        $div.append($("<input type='hidden' name='targetIndex' value='" + index + "'/>"));
        // リクエスト実行
        var request = new Request();
        request.postSubmitAddInputs("/tableRegister/deleteColumn", $div);
    });
});
