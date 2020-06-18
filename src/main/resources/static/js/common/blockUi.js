$(function() {
	blockUI = function() {
		$.blockUI({ message: '<p><img src="/img/loader.gif"  style="vertical-align:middle; width:15px; height:15px;" /> Please wait...</p>' });
	};

	unblockUI = function() {
		$.unblockUI();
	};

	$(".clickLink").on("click", function() {
		blockUI();
	});

});
