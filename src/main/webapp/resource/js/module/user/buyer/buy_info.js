define(['jquery','jquery-validate-messages_zh', 'validate'],function($, Message, Validate){
	'use strict';
	
	var page = function(){}
	var validate = new Validate();
	
	var DOM = {
			form_buy_info_confrim: $('.form_buy_info_confrim'),
			btn_form_buy_info_confirm_submit: $('.btn_form_buy_info_confirm_submit')
	}
	
	var main = function(){
		handleEvent();
	}
	
	function handleEvent(){
		// 表单校验
		DOM.form_buy_info_confrim.validate({
			errorClass : 'text-danger '+ Validate.inp_feedback.error_selector_class,
			rules : {
				phone:{mobile_phone:true}
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
		DOM.btn_form_buy_info_confirm_submit.click(function(){
			if(DOM.form_buy_info_confrim.valid()){
				var form_data = DOM.form_buy_info_confrim.serialize();
				var action =  DOM.form_buy_info_confrim.attr('action');
				$.post(action,form_data,function(data){
					if(data.result){
						
					}else{
						alert(data.error_msg);
					}
				},'json');
			}
		});
	}
	
	var method = {
			main:main
	}
	
	page.prototype = method;
	return page;
});