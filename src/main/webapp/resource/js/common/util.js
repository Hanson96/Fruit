/**
 * 工具类  都是静态方法
 */

define(['jquery'],function(){
	var Util = function(){}
	
	var U = function(){};
	// 判断是否为空
	U.isEmpty = function(obj){
		if(typeof(obj)=='undefined' || obj==null || obj=='undefined' || obj==""){
			return true;
		}
		return false;
	}
	// 根据参数名获取url的参数值
	U.gerUrlParamValue = function(paramName){
		var url = window.location.href;
		if(url.indexOf('?')!=-1){
			var url_param = url.split('?')[1]; // 获取参数部分
			var param_item_array = url_param.split('&'); // 获取参数数组
			for(var i=0; i<param_item_array.length; i++){
				var name = param_item_array[i].split('=')[0];
				var value = param_item_array[i].split('=')[1];
				if(name == paramName){
					return value; // 返回参数值
				}
			}
		}
		return '';
	}
	
	// 验证用户是否已经登录
	U.checkUserLogin = function(){
		var login = false;
		$.ajax({
			url:_ctx + '/checkUserLogin',
			type:'post',
			async:false,
			dataType:'json',
			success:function(data){
				if(data.login){
					login = true;
				}else{
					login = false;
					alert('您还未登录，请先登录');
					window.top.location.href = _ctx + '/login';
				}
			}
		});
		return login;
	}
	
	// 构造一个标准的form表单进行post提交 且页面跳转
	U.StandardPost = function(url,args){
	    var $form = $("<form method='post'></form>");
	    var $input;
        $form.attr({"action":url});
        $.each(args,function(key,value){console.log('key:'+key+' value:'+value)
            $input = $("<input type='hidden'>");
            $input.attr({"name":key});
            $input.val(value);
            $form.append($input);
        });
        $form.appendTo($('body'));
        $form.submit();
        $form.remove();
    }
	
	U.refresh_shopping_cart = function(result){
		if(result && $('#header .shopping_cart')[0]){ // 如果存在header 的购物车 就需要刷新
			var url = _ctx + '/shopping_cart';
			$.post(url,{},function(data){
				$('#header .shopping_cart').html(data);
			},'html');
		}
	}
	
	U.exact_count_down = function(end_time_string,$seletor){
		//var s = "2017-12-15 09:41:30";
		var end_time = new Date(Date.parse(end_time_string.replace(/-/g, "/")));
		function start(){
			 var distance = end_time - new Date(); // 计算剩余的毫秒数
			 var days = parseInt(distance / 1000 / 60 / 60 / 24 , 10); // 计算剩余的天数
			 var hours = parseInt(distance / 1000 / 60 / 60 % 24 , 10); // 计算剩余的小时
			 var minutes = parseInt(distance / 1000 / 60 % 60, 10);// 计算剩余的分钟
			 var seconds = parseInt(distance / 1000 % 60, 10);// 计算剩余的秒数
			 days = checkTime(days); 
			 hours = checkTime(hours); 
			 minutes = checkTime(minutes); 
			 seconds = checkTime(seconds);
			 var html = days+"天" + hours+"时" + minutes+"分"+seconds+"秒"; console.log(html);
			 $seletor.html(html);
		}
		function checkTime(i){ // 将0-9的数字前面加上0，例1变为01
			if(i<10){ 
				i = "0" + i; 
			} 
			return i; 
		} 
		window.setInterval(start,1000);
	}
	
	Util = U;
	return Util;
});