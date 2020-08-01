import { Request } from "../common/request.js";
import { IsCompleteKeyspaceRegist } from "../async/isCompleteKeyspaceRegist.js";

$(function () {
	/** キースペース登録画面に遷移 **/
	$("#keySpaceRegister").on("click", function () {
		var request = new Request();
		request.getSubmit("/keySpaceRegister");
	});

	var isCompleteCreateServer = function () {
		// 登録済みチェックを実施
		let isCompleteKeyspaceRegist = new IsCompleteKeyspaceRegist();
		isCompleteKeyspaceRegist.execRequest(
			function (completeKeySpaceList: String[]) {
				for (var index in completeKeySpaceList) {
					var $rowKeySpace = $("#row_" + completeKeySpaceList[index]);
					$rowKeySpace.find(".statusPending").each(function (index, e) {
						$(e).addClass('displayNone');
					});
					$rowKeySpace.find(".statusComplete").each(function (index, e) {
						$(e).removeClass('displayNone');
					});
				}
			});
	}
	setInterval(isCompleteCreateServer, 1000);

});
