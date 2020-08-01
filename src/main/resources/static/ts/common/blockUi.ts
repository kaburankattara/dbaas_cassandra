//import $ = require('jquery');
import * as ns from "jquery"

class BlockUI {

	blockUI = function () {
		if ($.blockUI == undefined) {
			return;
		}

		$.blockUI({ message: '<p><img src="/img/loader.gif"  style="vertical-align:middle; width:15px; height:15px;" /> Please wait...</p>' });
	};

	unblockUI = function () {
		if ($.unblockUI == undefined) {
			return;
		}
		$.unblockUI();
	};

};

$(function () {
	$(".clickLink").on("click", function () {
		let blockUI = new BlockUI();
		blockUI.blockUI;
	});
});

export { BlockUI };
