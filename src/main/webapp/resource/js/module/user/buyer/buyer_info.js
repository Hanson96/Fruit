define(['jquery','jquery-validate-messages_zh', 'validate', 'util'],function($, Message, Validate, Util){
	'use strict';
	
	var page = function(){}
	var validate = new Validate();
	
	var DOM = {
			center_left_menu: $('.center_left_menu'),
			center_content: $('.center_content'),
			
			form_buyer_info: $('.form_buyer_info'),
			btn_form_buyer_info_submit: $('.btn_form_buyer_info_submit')
	}
	
	var main = function(){
		handleEvent();
		//DOM.center_left_menu.find('ul>li a.current').click();
	}
	
	function handleEvent(){
		// 点击导航栏
		/*DOM.center_left_menu.find('ul>li a').click(function(){
			var href = $(this).attr('href');
			if(!Util.isEmpty(href)){
				$.post(href,{},function(data){
					DOM.center_content.html(center_content);
				},'html');
			}
			return false;
		});*/
		
		// 表单校验
		DOM.form_buyer_info.validate({
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
		DOM.btn_form_buyer_info_submit.click(function(){
			if(DOM.form_buyer_info.valid()){
				var form_data = DOM.form_buyer_info.serialize();
				var action = DOM.form_buyer_info.attr('action');
				$.post(action,form_data,function(data){
					if(data.result){
						var $div_msg = DOM.form_buyer_info.find('.div_msg');
						var span_html = '<span class="text-success temp_msg">已修改成功<span>'
						$div_msg.html(span_html);
						$div_msg.find('.temp_msg').fadeOut(1500);
						window.setTimeout(function(){
							$div_msg.find('.temp_msg').remove();
						},1500);
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