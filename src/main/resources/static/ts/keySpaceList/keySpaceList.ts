import { Request } from "../common/request.js";
import { IsCompleteKeyspaceRegist } from "../async/isCompleteKeyspaceRegist.js";
import { GetEndPoint } from "../async/getEndPoint.js";
import { StringUtils } from "../utils/stringUtils.js";

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

	var getEndPoint = function () {
		var $endPoint = $("#endPoint");

		// エンドポイントを取得済の場合、処理しない
		if (StringUtils.isNotEmpty($endPoint.html())) {
			return false;
		}

		// エンドポイントを取得し、項目に設定して画面に表示させる
		let getEndPoint = new GetEndPoint();
		getEndPoint.execRequest(
			function (endPoint : string) {
				if (StringUtils.isNotEmpty(endPoint)) {
					var $endPoint = $("#endPoint");
					$endPoint.html(endPoint);
					$endPoint.closest("td").removeClass('displayNone');
					return false;
				}
			});
	}
	setInterval(getEndPoint, 1000);

});
