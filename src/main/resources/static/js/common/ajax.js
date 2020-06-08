$(function() {
	AjaxObject = function(url, data, callBack) {
		this.url = url;
		this.data = data;
		this.callBack = callBack;
	};

	var errorCallBack = function(data) {
		$('#errorMessage').html(messages.MSG001S);
	}

	doAsyncPost = function(url, data, callBack) {
		ajaxObject.data._csrf = $('input[name="_csrf"]').val();
		$.ajax({
    		type: 'POST',
    		async: true,
      	   	url: url,
      	   	data: data,
      	   	success: callBack,
      	   	error: errorCallBack
		})
	};

	doAsyncGet = function(url, data, callBack) {
		$.ajax({
			type: 'GET',
			async: true,
		    url: url,
		    data: data,
		    success: callBack,
		    error: errorCallBack
		})
	};

	doSyncPost = function(url, data, callBack) {
		ajaxObject.data._csrf = $('input[name="_csrf"]').val();
		$.ajax({
    		type: 'POST',
    		async: false,
      	   	url: url,
      	   	data: data,
      	   	success: callBack,
      	   	error: errorCallBack
				})
	};

	doSyncGet = function(url, data, callBack) {
		$.ajax({
			type: 'GET',
			async: false,
		    url: url,
		    data: data,
		    success: callBack,
		    error: errorCallBack
		})
	};

	doAsyncPostJson = function(url, data, callBack) {
		$.ajax({
		    type : "POST",
			async: true,
		    url : url,
		    data : JSON.stringify(data),
		    contentType: 'application/json',
		    dataType : "json",
		    headers: {
                'X-CSRF-Token': $('input[name="_csrf"]').val()
                },
	        success: callBack,
			error: errorCallBack
		})
	};

	doSyncPostJson = function(url, data, callBack) {
		$.ajax({
		    type : "POST",
			async: false,
		    url : url,
		    data : JSON.stringify(data),
		    contentType: 'application/json',
		    dataType : "json",
		    headers: {
                'X-CSRF-Token': $('input[name="_csrf"]').val()
                },
	        success: callBack,
			error: errorCallBack
		})
	};
});
