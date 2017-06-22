define(['jquery','jquery-validate-messages_zh', 'validate','bootstrap'],function($, Message, Validate){
	'use strict'
	var page = function(){}
	var validate = new Validate();
	
	var DOM = {
			form_group_goods: $('.form_group_goods'),
			inp_goods_id: $('.inp_goods_id'),
			inp_goods_name: $('.inp_goods_name'),
			span_goods_price: $('.span_goods_price'),
			btn_form_group_goods_submit: $('.btn_form_group_goods_submit'),
			goods_choose_modal: $('#goods_choose_modal'),
			form_group_goods_list_ajax: $('.form_group_goods_list_ajax'),
			goods_list_content: $('.goods_list_content')
	}
	
	var main = function(){
		handleEvent();
	}
	
	function handleEvent(){
		// 表单校验
		DOM.form_group_goods.validate({
			errorClass : 'text-danger '+ Validate.inp_feedback.error_selector_class,
			rules : {
				group_price:{positive_decimal_two:true},
				group_count:{positive_integer:true}
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
		DOM.btn_form_group_goods_submit.click(function(){
			if(DOM.form_group_goods.valid()){
				DOM.form_group_goods.submit();
			}
		});
		
		// ajax搜索商品列表
		DOM.form_group_goods_list_ajax.find('.btn_search_ajax').click(function(){
			var action = DOM.form_group_goods_list_ajax.attr('action');
			$.post(action,{},function(data){
				DOM.goods_list_content.html(data);
			},'html');
		});
		
		// 模态框弹出
		DOM.goods_choose_modal.on('show.bs.modal', function (event) {
			DOM.form_group_goods_list_ajax.find('.btn_search_ajax').click();
		});
	}
	
	var method = {
			main:main,
			group_goods_choose:function(param_array){
				var goods_id = param_array[0]
				var goods_name = param_array[1];
				var goods_price = param_array[2];
				DOM.inp_goods_id.val(goods_id);
				DOM.inp_goods_name.val(goods_name);
				DOM.span_goods_price.html(goods_price);
				DOM.goods_choose_modal.modal('hide');
				DOM.goods_list_content.empty();
			}
	}
	
	page.prototype = method;
	return page;
});