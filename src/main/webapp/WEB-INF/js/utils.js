function getCurrentTime() {
	var now = new Date();
	var time = now.getFullYear() + "年" + now.getMonth() + "月" + now.getDate()
			+ "日 " + now.getHours() + ":" + now.getMinutes() + ":"
			+ now.getSeconds() + "," + now.getMilliseconds() + " "
			+ now.getDay();
	return time;
}

function generateRandomString(len) {
	var array = [ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B',
			'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
			'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b',
			'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o',
			'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' ];
	var result = "";
	for (var i = 0; i < len; i++) {
		result += array[Math.round(Math.random() * 62)];
	}
	return result;
}
