/*$("#checkcode_img").click(function(event){
	var time = new Date().getTime();
	event = event || window.event; 
	var target = event.target;
	$(target).attr("src",target.src+"?"+time);
});*/

/**
 * 刷新属性，增加时间戳
 * @param elem
 * @param attr
 */
function refresh(elem,attr){
	var time = new Date().getTime();
	var original = $(elem).attr(attr).split("?")[0];
	$(elem).attr(attr,original+"?"+time);
}
