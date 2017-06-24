define(['jquery','validate','jquery-validate-messages_zh'],function($, Validate){
	
	"use strict";
	
	var page = function(){}
	var validate = new Validate();
	
	var DOM = {
			form_user: $('.form_user'),
			btn_user_submit: $('.btn_user_submit')
	}
	
	var main = function(){
		handleEvent();
	}
	
	function handleEvent(){
		// 表单校验
		DOM.form_user.validate({
			errorClass: 'text-danger '+ Validate.inp_feedback.error_selector_class,
			rules : {
				password:{
					letter_number_underline:true,
					rangelength:[6,20]
				},
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
		
		DOM.btn_user_submit.click(function(){console.log(DOM.form_user.valid());
			if(DOM.form_user.valid()){
				DOM.form_user.submit();
			}
		});
		
	}
	
	var method = {
			main:main
	}
	
	page.prototype = method;
	return page;
});