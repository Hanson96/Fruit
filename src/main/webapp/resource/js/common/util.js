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
	
	Util = U;
	return Util;
});