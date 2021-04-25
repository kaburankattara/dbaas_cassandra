var ClipBoardCopy = /** @class */ (function () {
    function ClipBoardCopy() {
        this.execById = function (tagId) {
            // コピーする文章の取得
            var text = $("#" + tagId).text();
            // テキストエリアの作成
            var $textarea = $('<textarea></textarea>');
            // テキストエリアに文章を挿入
            $textarea.text(text);
            //　テキストエリアを挿入
            $("#form").append($textarea);
            //　テキストエリアを選択
            $textarea.select();
            // コピー
            document.execCommand('copy');
            // テキストエリアの削除
            $textarea.remove();
            // アラート文の表示
            $('#copyAlert').show().delay(2000).fadeOut(400);
        };
    }
    return ClipBoardCopy;
}());
export { ClipBoardCopy };
