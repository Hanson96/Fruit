/**
 * 跟表单验证有关的一些属性和方法
 */
define(['jquery'],function($){
	'use strict';
	
	var validate = function(){}
	var V = function(){};
	// 静态属性
	V.inp_feedback = {
			success_selector:'.feedback_success',
			error_selector:'.feedback_error',
			error_selector_class:'feedback_error', // 应与error_selector对应
			success_html:'<span class="feedback_success glyphicon glyphicon-ok form-control-feedback" aria-hidden="true"></span>',
			error_html:'<span class="feedback_error glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>'
	}
	
	// 动态方法
	var method = {
			/**
			 * 表单输入框错误时的处理方法 
			 * parent_element:输入框的父级元素 , right_class:反馈图标附加的样式，主要用于调整图标的偏移量
			 */   
			error_handle:function(parent_element, right_class){
				var $parent = $(parent_element); // 输入框的父级元素
				$parent.find(V.inp_feedback.success_selector+':not(:input)').remove(); // 删除正确的勾号图标
				$parent.removeClass('has-success').addClass('has-error');
				var $error_html = $(V.inp_feedback.error_html);
				if(typeof(right_class)!='undefined' && right_class!='undefined' && right_class!=""){
					$error_html.addClass(right_class);
				}
				$parent.append($error_html); // 添加错误的叉号图标
			},
			/**
			 * 表单输入框正确时的处理方法 
			 * parent_element:输入框的父级元素 , right_class:反馈图标附加的样式，主要用于调整图标的偏移量
			 */ 
			success_handle:function(parent_element, right_class){
				var $parent = $(parent_element); // 输入框的父级元素
				$parent.find(V.inp_feedback.error_selector+':not(:input)').remove();   // 删除错误的叉号图标
		    	$parent.removeClass('has-error').addClass('has-success');
		    	var $success_html = $(V.inp_feedback.success_html);
		    	if(typeof(right_class)!='undefined' && right_class!='undefined' && right_class!=""){
		    		$success_html.addClass(right_class);
				}
		    	$parent.append($success_html); // 添加正确的勾号图标
			}
	}
	
	validate = V;
	validate.prototype = method;
	return validate;
});