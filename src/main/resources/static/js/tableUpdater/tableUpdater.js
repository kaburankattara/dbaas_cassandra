$(function() {
	
	/** カラムの入力項目を追加する **/
	$("#addColumn").on("click", function(){
		postSubmit("/tableUpdater/addColumn");
	});
	
	/** テーブル削除 **/
	$("#deleteTable").on("click", function(){
		postSubmit("/tableUpdater/deleteTable");
	});
	
	/** カラムの入力項目を追加する **/
	$(".deleteColumn").on("click", function(e){
		$form = $("#form");
		$parentTd = $(e.target).closest("td");
		var index = $parentTd.find('#deleteTargetIndex').val();
		$form.append("<input type='hidden' name='targetIndex' value='" + index + "'/>")
		
		postSubmit("/tableUpdater/deleteColumn");
	});
});
