/**
 * 登录页面
 */
define(['jquery','jquery-validate-messages_zh','user/../../common/validate','user/../../common/util'],function($, Message, Validate, Util){
	'use strict';
	
	var page = function(){}
	var validate = new Validate();
	
	var DOM = {
			section_login: $('.section_login'),
			form_login: $('.form_login'),
			btn_form_login_submit: $('.btn_form_login_submit'),
			
			section_register: $('.section_register'),
			form_register: $('.form_register'),
			btn_form_register_submit: $('.btn_form_register_submit')
	}
	
	var main = function(){
		handleEvent();
		if(Util.gerUrlParamValue('type') == 'register'){
			DOM.section_login.addClass('hide');
			DOM.section_register.removeClass('hide');
			DOM.form_register.find('.img_verify_code').click(); // 刷新验证码
		}else{
			DOM.form_login.find('.img_verify_code').click();
		}
	}
	
	function handleEvent(){
		// 登录表单校验
		DOM.form_login.validate({
			errorClass : 'text-danger '+ Validate.inp_feedback.error_selector_class,
			rules : {
				verify_code:{
					remote:{
						type:'post',
						url:_ctx + '/validate_verify_code',
						data:{
							verify_code:function(){return DOM.form_login.find('.inp_verify_code').val();},
							refresh:'false'
						}
					}
				}
			},
			messages:{
				verify_code:{
					remote:'验证码不正确'
				}
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
		
		// 提交登录表单
		DOM.btn_form_login_submit.click(function(){console.log('aaa');
			if(DOM.form_login.valid()){
				DOM.form_login.submit();
			}
		});
		
		// 注册表单校验
		DOM.form_register.validate({
			errorClass : 'text-danger '+ Validate.inp_feedback.error_selector_class,
			rules : {
				userName:{
					letter_number_underline:true,
					remote:{
						type:'post',
						url:_ctx + '/validate_userName_same',
						data:{
							userName:function(){return DOM.form_register.find('.inp_userName').val();}
						}
					}
				},
				password:{
					letter_number_underline:true,
					rangelength:[6,20]
				},
				password_confirm:{
					equalTo:'.form_register .inp_password'
				},
				verify_code:{
					remote:{
						type:'post',
						url:_ctx + '/validate_verify_code',
						data:{
							verify_code:function(){return DOM.form_register.find('.inp_verify_code').val();},
							refresh:'false'
						}
					}
				}
			},
			messages:{
				userName:{
					remote:'用户名已存在'
				},
				verify_code:{
					remote:'验证码不正确'
				}
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
		
		// 提交注册表单
		DOM.btn_form_register_submit.click(function(){
			if(DOM.form_register.valid()){
				DOM.form_register.submit();
			}
		});
		
		// 点击  更新登录表单的验证码图片
		DOM.form_login.find('.img_verify_code').click(function(){
			var seconds = new Date().getTime();
			var src = _ctx + '/get_verify_code?s='+seconds;
			$(this).attr('src',src);
		});
		
		// 点击  更新注册表单的验证码图片
		DOM.form_register.find('.img_verify_code').click(function(){
			var seconds = new Date().getTime();
			var src = _ctx + '/get_verify_code?s='+seconds;
			$(this).attr('src',src);
		});
	}
	
	var method = {
			main:main
	}
	
	page.prototype = method;
	
	return page;
});