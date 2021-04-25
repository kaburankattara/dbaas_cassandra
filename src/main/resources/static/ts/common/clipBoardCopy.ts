class ClipBoardCopy {
	execById = function (message: string) {
    	// テキストエリアの作成
    	let $textarea = $('<textarea></textarea>');
    	// テキストエリアに文章を挿入
    	$textarea.text(message);
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
	}
}

export { ClipBoardCopy };
