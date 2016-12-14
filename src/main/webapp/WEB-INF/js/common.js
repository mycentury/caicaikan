/*$("#checkcode_img").click(function(event){
	var time = new Date().getTime();
	event = event || window.event; 
	var target = event.target;
	$(target).attr("src",target.src+"?"+time);
});*/

/**
 * 刷新属性，增加时间戳
 * 
 * @param elem
 * @param attr
 */
function refresh_attr(elem, attr) {
	var time = new Date().getTime();
	var original = $(elem).attr(attr).split("?")[0];
	$(elem).attr(attr, original + "?" + time);
}

String.prototype.startWith = function(str) {
	if (str == null || str == "" || this.length == 0
			|| str.length > this.length)
		return false;
	if (this.substr(0, str.length) == str)
		return true;
	else
		return false;
	return true;
}
String.prototype.endWith = function(str) {
	if (str == null || str == "" || this.length == 0
			|| str.length > this.length)
		return false;
	if (this.substring(this.length - str.length) == str)
		return true;
	else
		return false;
	return true;
}

function get_type(obj) {
	return Object.prototype.toString.call(obj);
}
