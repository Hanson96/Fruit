define(['jquery','datetimepicker.zh-CN'],function($){
	
	"use strict";
	
	var page = function(){}
	
	var DOM = {
			
	}
	
	var main = function(){
		handleEvent();
	}
	
	function handleEvent(){
		// 日志时间 的 开始时间 和 结束时间
		var log_start_time, log_end_time;
		$('#datetimepicker_log_start_time').datetimepicker({
	        language:  'zh-CN',
	        weekStart: 1,
	        todayBtn:  true,
			autoclose: true,
			todayHighlight: true,
			startView: 2,
			minView: 2,
			forceParse: true
	    }).on('changeDate', function(start){
	    	log_start_time = $('#datetimepicker_log_start_time').find('input').val();
	    	$('#datetimepicker_log_end_time').datetimepicker('setStartDate', log_start_time);
	    });
		$('#datetimepicker_log_end_time').datetimepicker({
	        language:  'zh-CN',
	        weekStart: 1,
	        todayBtn:  true,
			autoclose: true,
			todayHighlight: true,
			startView: 2,
			minView: 2,
			forceParse: true
	    }).on('changeDate', function(end){
	    	log_start_time = $('#datetimepicker_log_start_time').find('input').val();
	    	log_end_time = $('#datetimepicker_log_end_time').find('input').val();
		    $("#datetimepicker_log_start_time").datetimepicker('setEndDate',log_end_time);
	    	if(log_start_time > log_end_time){
	    		alert('结束时间不允许小于开始时间');
	    		$('#datetimepicker_log_end_time').find('input').val('');
	    	}
	    });
		
	}
	
	var method = {
			main:main
	}
	
	page.prototype = method;
	return page;
});