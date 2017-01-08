function media_ajax(url, method, data, beforeSend, success, complete, error) {
	$.ajax({
		url : url, // 请求的url地址
		// dataType : "json", // 返回格式为json,可由SpringMVC定义
		async : true,// 请求是否异步，默认为异步，这也是ajax重要特性
		cache : false,
		timeout: 0,
		data : data, // 参数值
		type : method, // 请求方式
		contentType : false,
		processData : false,// 必须
		beforeSend : beforeSend,
		success : success,
		complete : complete,
		error : error
	});
}

function general_ajax(url, method, data, beforeSend, success, complete, error) {
	$.ajax({
		url : url, // 请求的url地址
		// dataType : "json", // 返回格式为json,可由SpringMVC定义
		async : true,// 请求是否异步，默认为异步，这也是ajax重要特性
		cache : false,
		type : method, // 请求方式
		timeout: 0,
		data : data, // 参数值
		// contentType : false,
		// processData : false,// 必须
		beforeSend : beforeSend,
		success : success,
		complete : complete,
		error : error
	});
}

function create_traditional_ajax() {
	var xhr;
	if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
		xhr = new XMLHttpRequest();
	} else {// code for IE6, IE5
		xhr = new ActiveXObject("Microsoft.XMLHTTP");
	}
	return xhr;
}

/**
 * @param url
 * @param method：参数少且简单，推荐GET，其他POST
 * @param data
 * @param beforeSend
 * @param success
 * @param complete
 * @param error
 */
function traditionalAjax(url, method, data, beforeSend, success, complete,
		error) {
	var xhr = create_traditional_ajax();
	xhr.onreadystatechange = function(msg) {
		if (xhr.readyState == 4 && xhr.status == 200) {
			success(xhr.responseText);
		} else if (xhr.readyState == 4 && xhr.status != 200) {
			error(xhr.responseText);
		}
	}
	xhr.open(method, url, true);
	xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	xhr.send(data);
}