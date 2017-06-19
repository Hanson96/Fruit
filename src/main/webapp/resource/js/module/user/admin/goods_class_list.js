define(['jquery','user/../../common/util','jquery-validate-messages_zh','user/../../common/validate'],function($,Util, Message, Validate){
	'use strict'
	var page = function(){}
	var validate = new Validate();
	
	var DOM = {
			goods_class_add_modal:$('#goods_class_add_modal'),
			form_goods_class: $('.form_goods_class'),
			form_goods_class_copy: $('.form_goods_class_copy'),
			btn_form_goods_class_submit: $('.btn_form_goods_class_submit')
	}
	
	var main = function(){
		handleEvent();
		//$('[data-toggle="tooltip"]:input').tooltip();
	}
	
	function handleEvent(){
		// 模态框事件
		DOM.goods_class_add_modal.on('show.bs.modal', function(event) {
			DOM.form_goods_class.html(DOM.form_goods_class_copy.html()); // 复制表单副本
			// 表单校验
			DOM.form_goods_class.validate({
				errorClass : 'text-danger '+ Validate.inp_feedback.error_selector_class,
				rules : {
					name : {
						remote:{
							type:'post',
							url:_ctx+'/admin/goods_class_validate_name',
							data:{
								obj_id:function(){return DOM.form_goods_class.find('.inp_obj_id').val()},
								name:function(){return DOM.form_goods_class.find('.inp_name').val()}
							}
							
						}
					}
				},
				messages:{
					name:{
						remote:'此分类名称已存在'
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
			var $button = $(event.relatedTarget);
			var info = $button.data('info');
			var obj_id = $button.data('obj-id');
			var name = '';
			if (Util.isEmpty(obj_id)) {
				obj_id = '';
			}else{
				name = $button.data('name');
			}
			var $modal = $(this);
			$modal.find('.modal-title').text(info)
			DOM.form_goods_class.find('.inp_obj_id').val(obj_id);
			DOM.form_goods_class.find('.inp_name').val(name);
		});
		
		// 提交表单
		DOM.btn_form_goods_class_submit.click(function(){
			if(DOM.form_goods_class.valid()){
				var form_data = DOM.form_goods_class.serialize();
				var action = DOM.form_goods_class.attr('action');
				$.post(action,form_data,function(data){
					if(data.result){
						window.location = window.location.href;
					}else{
						alert(data.error_msg);
					}
				},'json')
			}
		});
	}
	
	var method = {
			main:main
	}
	
	page.prototype = method;
	return page;
});