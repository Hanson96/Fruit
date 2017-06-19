/**
 * 公共的js,使用别名简化路径配置
 * @author hanson 
 */

// 如果是ie浏览器  获取浏览器版本
function getIEVersion(){
	var userAgent = navigator.userAgent; // 取得浏览器的userAgent字符串
	var isOpera = userAgent.indexOf("Opera") > -1; // 判断是否Opera浏览器
	var isIE = userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1 && !isOpera; // 判断是否IE浏览器
	if (isIE) {
		var reIE = new RegExp("MSIE (\\d+\\.\\d+);");
		reIE.test(userAgent);
		var fIEVersion = parseFloat(RegExp["$1"]);console.log('fIEVersion:'+fIEVersion);
		if (fIEVersion == 7) {
			return 7;
		} else if (fIEVersion == 8) {
			return 8;
		} else if (fIEVersion == 9) {
			return 9;
		} else if (fIEVersion == 10) {
			return 10;
		} else if (fIEVersion == 11) {
			return 11;
		} else {
			return 0;
		}// IE版本过低
	}else{
		return -1;
	}
}
// 判断ie浏览器版本来加载jquery
var jquery_url = '../../plugin/jquery/jquery-3.1.1';
var ie_version = -1;
(function(){
	ie_version = getIEVersion();
	if(ie_version != -1){
		if(ie_version >= 8 && ie_version <= 9){
			jquery_url = '../../plugin/jquery/jquery-1.9.1';
		}else if(ie_version >= 0 && ie_version <= 7){
			jquery_url = '../../plugin/jquery/jquery-1.9.1';
			alert('您的ie浏览器版本太低，请尝试其他浏览器或更高版本的ie浏览器');
		}
	}
})();

require.config({
	baseUrl: _ctx + '/resource/js/module',
	paths: {
      'jquery': jquery_url,
      'bootstrap': '../../plugin/bootstrap/bootstrap-3.3.7/js/bootstrap.min',
      'datetimepicker': '../../plugin/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min',
      'datetimepicker.zh-CN': '../../plugin/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN',
      'jquery.validate': '../../plugin/jquery/jquery-validation-1.13.0/jquery.validate.min',
      'additional-methods': '../../plugin/jquery/jquery-validation-1.13.0/additional-methods',
      'jquery-validate-messages_zh': '../../plugin/jquery/jquery-validation-1.13.0/localization/messages_zh',
      'jquery.metadata': '../../plugin/jquery/jquery.metadata',
      'jquery.placeholder': '../../plugin/jquery/jquery.placeholder.min',
      'bootstrap-fileinput': '../../plugin/bootstrap/bootstrap-fileinput/js/fileinput.min',
      'bootstrap-fileinput_zh': '../../plugin/bootstrap/bootstrap-fileinput/js/locales/zh',
      'ueditor': '../../plugin/ueditor/ueditor.all',
      'ueditor_zh-cn': '../../plugin/ueditor/lang/zh-cn/zh-cn',
      'zeroclipboard': '../../plugin/ueditor/third-party/zeroclipboard/ZeroClipboard.min'
    },
    map: { 
        '*': {
        	// 加载这个可以引入css样式    依赖中要以  css! 开头  比如'css!../../plugin/ueditor/themes/default/css/ueditor' 表示引入ueditor.css
            'css': '../../plugin/requirejs/lib/css'
        }
    },
    shim:{
    	'bootstrap': {
			deps:['jquery']
		},
		'datetimepicker': {
			deps:['bootstrap']
		},
		'datetimepicker.zh-CN': {
			deps:['datetimepicker']
		},
		'jquery.validate': {
			deps:['jquery']
		},
		'additional-methods': {
			deps:['jquery.validate']
		},
		'jquery-validate-messages_zh': {
			deps:['additional-methods','jquery.validate']
		},
		'jquery.metadata': {
			deps:['jquery.validate']
		},
		'jquery.placeholder': {
			deps:['jquery']
		},
		'bootstrap-fileinput_zh': {
			deps:['bootstrap','bootstrap-fileinput']
		},
		'ueditor': {
			deps:['zeroclipboard','../../plugin/ueditor/ueditor.config','css!../../plugin/ueditor/themes/default/css/ueditor'],
			exports: 'UE',
            init:function(ZeroClipboard){
                //导出到全局变量，供ueditor使用
                window.ZeroClipboard = ZeroClipboard;
            }
		},
		'ueditor_zh-cn': {
			deps:['ueditor']
		}
    }
});
if(ie_version >= 0 && ie_version <= 9){
	// 使ie9以下支持placeholder
	require(['respond','jquery.placeholder'],function(){
	    $('input, textarea').placeholder();
	});
}

