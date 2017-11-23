var ip="http://120.25.245.90:8089/cage_car/pl_php/";
var href=window.location.href;
if(href.split("uid=")[1]){
	var id=href.split("uid=");
	var uid = id[1];
	var str = uid.split("&order=");
	if(str[1]){
		uid = str[0];
		
		var strs = str[1].split("&cage=");
		if(strs[0]){
			var order = strs[0];
			var cage = strs[1];
		}

	}
}
