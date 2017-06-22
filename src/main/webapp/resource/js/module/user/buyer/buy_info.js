define(['jquery','jquery-validate-messages_zh', 'validate'],function($, Message, Validate){
	'use strict';
	
	var page = function(){}
	
	var DOM = {
			form_buy_info_confrim: $('.form_buy_info_confrim'),
			btn_buy_info_confrim_submit: $('.btn_buy_info_confrim_submit')
	}
	
	var main = function(){
		handleEvent();
	}
	
	function handleEvent(){
		// 表单校验
		DOM.form_buy_info_confrim.validate({
			errorClass : 'text-danger '+ Validate.inp_feedback.error_selector_class,
			rules : {
				
			},
			errorPlacement : function(error_label, element) { // 错误元素处理
				var $parent = $(element).parent();
				validate.error_handle($parent, 'right-15');
				error_label.appendTo($parent);
			},
			success : function(error_label) { // 正确时回调
				var $parent = $(error_label).parent();
				validate.success_handle($parent, 'right-15');
			}
		});
		// 提交表单
		DOM.btn_form_group_goods_submit.click(function(){
			if(DOM.form_buy_info_confrim.valid()){
				DOM.form_buy_info_confrim.submit();
			}
		});
	}
	
	var method = {
			main:main
	}
	
	page.prototype = method;
	return page;
});