class ClipBoardCopy {
	execById = function (tagId: string) {
    	// コピーする文章の取得
		let text = $("#" + tagId).text();
    	// テキストエリアの作成
    	let $textarea = $('<textarea></textarea>');
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
		alert("クリップボードにコピーしました。");
    	//$('#js-copyalert').show().delay(2000).fadeOut(400);
	}
}

export { ClipBoardCopy };
