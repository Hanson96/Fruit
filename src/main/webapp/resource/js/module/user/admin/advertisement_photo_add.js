define(['jquery','user/../../common/util','jquery-validate-messages_zh',
	'user/../../common/validate','bootstrap-fileinput_zh','ueditor_zh-cn'],function($,Util, Message, Validate){
	'use strict'
	var page = function(){}
	var validate = new Validate();
	
	var DOM = {
			form_advertisement_photo: $('.form_advertisement_photo'),
			acc_file: $('#acc_file'),
			btn_advertisement_photo_submit: $('.btn_advertisement_photo_submit')
	}
	
	var main = function(){
		handleEvent();
	}
	
	function handleEvent(){
		// 表单校验
		DOM.form_advertisement_photo.validate({
			errorClass : 'text-danger '+ Validate.inp_feedback.error_selector_class,
			rules : {
				sequence:{integer:true}
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
		DOM.btn_advertisement_photo_submit.click(function(){
			if(DOM.form_advertisement_photo.valid()){
				var acc_count = DOM.acc_file.fileinput('getFilesCount'); // 未上传的图片
				var acc_origin_count =  DOM.acc_file.attr('file_count'); // 原来就存在的图片
				if(acc_count==0 && acc_origin_count==0){
					alert('还未选择图片');
				}else{
					DOM.form_advertisement_photo.submit();
				}
			}
		});
	}
	
	var method = {
			main:main
	}
	
	page.prototype = method;
	return page;
});