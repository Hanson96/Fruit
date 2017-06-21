define(['jquery','validate','datetimepicker.zh-CN','jquery-validate-messages_zh','bootstrap-fileinput_zh',],function($, Validate){
	
	"use strict";
	
	var page = function(){}
	var validate = new Validate();
	
	var DOM = {
			form_group: $('.form_group'),
			acc_file: $('.acc_file'),
			btn_group_submit: $('.btn_group_submit')
	}
	
	var main = function(){
		handleEvent();
	}
	
	function handleEvent(){
		// 团购活动 的 开始时间 和 结束时间
		var start_time, end_time;
		$('#datetimepicker_start_time').datetimepicker({
	        language:  'zh-CN',
	        weekStart: 1,
	        todayBtn:  true,
			autoclose: true,
			todayHighlight: true,
			startView: 2,
			minView: 2,
			forceParse: true
	    }).on('changeDate', function(start){
	    	start_time = $('#datetimepicker_start_time').find('input').val();
	    	$('#datetimepicker_end_time').datetimepicker('setStartDate', start_time);
	    });
		$('#datetimepicker_end_time').datetimepicker({
	        language:  'zh-CN',
	        weekStart: 1,
	        todayBtn:  true,
			autoclose: true,
			todayHighlight: true,
			startView: 2,
			minView: 2,
			forceParse: true
	    }).on('changeDate', function(end){
	    	start_time = $('#datetimepicker_start_time').find('input').val();
	    	end_time = $('#datetimepicker_end_time').find('input').val();
		    $("#datetimepicker_start_time").datetimepicker('setEndDate',end_time);
	    	if(start_time > end_time){
	    		alert('结束时间不允许小于开始时间');
	    		$('#datetimepicker_end_time').find('input').val('');
	    	}
	    });
		
		// 表单校验
		DOM.form_group.validate({
			errorClass : 'text-danger '+ Validate.inp_feedback.error_selector_class,
			rules : {
				
			},
			errorPlacement : function(error_label, element) { // 错误元素处理
				var $parent = $(element).parents('.parent');
				validate.error_handle($parent, 'right-15');
				error_label.appendTo($parent);
			},
			success : function(error_label) { // 正确时回调
				var $parent = $(error_label).parents('.parent');
				validate.success_handle($parent, 'right-15');
			}
		});
		
		DOM.btn_group_submit.click(function(){
			if(DOM.form_group.valid()){
				var acc_count = DOM.acc_file.fileinput('getFilesCount'); // 未上传的图片
				var acc_origin_count =  DOM.acc_file.attr('file_count'); // 原来就存在的图片
				if(acc_count==0 && acc_origin_count==0){
					alert('还未选择图片');
				}else{
					DOM.form_group.submit();
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