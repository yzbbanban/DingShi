var cookie_ip ="";
$(document).ready(function(){
	if(window.login&&window.pass){
		$("#login").val(window.login);
		$("#password").val(window.pass);
	}
	var ajax_ = ajax_fun('http://120.25.245.90:8089/ay_tbsj/depotip_ajax.php',"","","不能访问");
	//登录
	$("#but").click(function(){
		window.location.href="main.html";
//设置cookie  ip
		$.cookie('cookie_ip',$("#test1").val(), { expires: 7 }); 
		 cookie_ip = $("#test1").val();
//		alert(cookie_ip);
//		alert(window.cookie_ip);
			var url_ = "http://"+cookie_ip+":8089/ay_tbsj/ajax.php";
//			alert(url_);
			var login = $("#login").val();
			var pass = $("#password").val();
			var data_ = {"login":login,"password":pass};

			is_login(url_,data_);

	})
})
//判断用户名和密码是否正确
function is_login(url_,data_){
		$.ajax({
			type : "post",
			url:url_,
			data: data_,
			dataType : "jsonp",
			jsonp: "callback",//传递给请求处理程序或页面的，用以获得jsonp回调函数名的参数名(默认为:callback)
//			jsonpCallback:"success_jsonpCallback",//自定义的jsonp回调函数名称，默认为jQuery自动生成的随机函数名
			success : function(va_json){
				if(va_json!=''){
					$.cookie('cookie_uid',va_json,{expires:7});
					$.cookie('login',$("#login").val(),{expires:7});
					$.cookie('password',$("#password").val(),{expires:7});
					window.location.href="main.html";
				}else{
					window.location.href="main.html";
				}
						     
			},error: function(XMLHttpRequest, textStatus, errorThrown) {
   alert(XMLHttpRequest.readyState);
}
	});
}

//请求公司名称的
function ajax_fun(fu_url,post_data,tz_url,erooe){
	var data="";
		$.ajax({
						    type : "post",
						    url:fu_url,
						    data:post_data,
						    async:true,
						    // url : "http://192.168.100.109:8089/think_a/index.php/Public/ajaxlogin",
						    dataType : "jsonp",
						    jsonp: "callback",//传递给请求处理程序或页面的，用以获得jsonp回调函数名的参数名(默认为:callback)
						    jsonpCallback:"success_jsonpCallback",//自定义的jsonp回调函数名称，默认为jQuery自动生成的随机函数名
						    success : function(va_json){
						       if(va_json!=''){
						       	if(tz_url){
						       		window.location.href=tz_url;
						       	}else{
						       		data = va_json;
						       		var itemsSelectFuntion = function(){
									  var product_num=this.id;
									 
									  $("input[name=product_num]").val(product_num);
									};
									$.selectSuggest('testInput',va_json,itemsSelectFuntion);
						       	}
						       		
						       }else{
						       	    alert(erooe);
						       }
						    },
						    error:function(){
						        alert('fail');
						    }
	});
	return data
}



