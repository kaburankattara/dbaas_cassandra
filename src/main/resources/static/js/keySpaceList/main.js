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
});
