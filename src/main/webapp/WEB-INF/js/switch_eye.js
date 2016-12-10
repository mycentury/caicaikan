$("#switch_eye").click(function(){
	if ("password"==$("#password").attr("type")) {
		$("#password").attr("type","text");
		$("#eye").attr("class","glyphicon glyphicon-eye-close");
	} else {
		$("#password").attr("type","password");
		$("#eye").attr("class","glyphicon glyphicon-eye-open");
	}
});