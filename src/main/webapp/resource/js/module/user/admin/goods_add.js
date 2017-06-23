define(['jquery','user/../../common/util','jquery-validate-messages_zh',
	'user/../../common/validate','bootstrap-fileinput_zh','ueditor_zh-cn'],function($,Util, Message, Validate){
	'use strict'
	var page = function(){}
	var validate = new Validate();
	
	var DOM = {
			form_goods: $('.form_goods'),
			main_photo_file: $('#main_photo_file'),
			photo_list_file: $('#photo_list_file'),
			btn_form_goods_submit: $('.btn_form_goods_submit')
	}
	
	var main = function(){
		handleEvent();
		fileUploadEvent();
		var ue = UE.getEditor('editor');
		ue.ready(function() {
			ue.setContent($('#get_description_html').val());
		});
	}
	
	function handleEvent(){
		// 表单校验
		DOM.form_goods.validate({
			errorClass : 'text-danger '+ Validate.inp_feedback.error_selector_class,
			rules : {
				name:{maxlength:20},
				now_price:{positive_decimal_two:true},
				original_price:{positive_decimal_two:true}
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
		DOM.btn_form_goods_submit.click(function(){
			
			if(DOM.form_goods.valid()){
				var main_photo_count = DOM.main_photo_file.fileinput('getFilesCount'); // 未上传的图片
				var main_photo_origin_count =  DOM.main_photo_file.attr('file_count'); // 原来就存在的图片
				var photo_list_count = DOM.photo_list_file.fileinput('getFilesCount');
				var photo_list_origin_count = DOM.photo_list_file.attr('file_count');
				console.log('main_photo_count:'+main_photo_count);
				console.log('photo_list_count:'+photo_list_count);
				if(main_photo_count==0 && main_photo_origin_count==0){
					alert('还未选择商品主图');
				}else if(photo_list_count==0 && photo_list_origin_count==0){
					alert('还未选择商品其它图片');
				}else{
					DOM.form_goods.submit();
				}
			}
		});
	}
	
	function fileUploadEvent(){
		//initFileInput('#main_photo_file',_ctx+'/admin/photo_list_file_upload', 1);
		//initFileInput('#photo_list_file',_ctx+'/admin/photo_list_file_upload', 5);
		// 文件上传之前触发
		DOM.main_photo_file.on('filepreupload', function(event, data, previewId, index) {
	        var form = data.form, files = data.files, extra = data.extra,
	            response = data.response, reader = data.reader;
	        console.log('File pre upload triggered');
	    });
		// 文件上传完成触发
	    DOM.photo_list_file.on('fileuploaded', function(event, data, previewId, index) {
	        var form = data.form, files = data.files, extra = data.extra,
	            response = data.response, reader = data.reader;
	        console.log(data.form);
	        console.log(data.extra);
	        console.log('previewId:'+previewId);
	        console.log('index:'+index);
	        console.log(data.response);
	        console.log('File uploaded triggered');
	    });
	    
	    
	    
	}
	
	//初始化fileinput控件（第一次初始化）
	function initFileInput(file_selector, uploadUrl, maxFileCount) {    
	    var control = $('' + file_selector); 
	    control.fileinput({
	        language: 'zh', //设置语言
	        uploadUrl: uploadUrl, //上传的地址
	        maxFileCount: maxFileCount, // 最大上传数量
	        allowedFileExtensions : ['jpg', 'png','gif'],//接收的文件后缀
	        //showUpload: true, //是否显示上传按钮
	        //showCaption: true,//是否显示标题
	        //browseClass: "btn btn-primary", //按钮样式             
	       // previewFileIcon: "<i class='glyphicon glyphicon-king'></i>", 
	    });
	}
	
	var method = {
			main:main
	}
	
	page.prototype = method;
	return page;
});