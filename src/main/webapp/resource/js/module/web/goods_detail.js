define(['jquery','imagezoom','util'],function($, Imagezoom, Util){
	'use strict';
	
	var page = function(){}
	
	var DOM = {
			photo_main: $('.photo_main'),
			btn_add_cart: $('.btn_add_cart'),
			btn_buy_now: $('.btn_buy_now'),
			
			section_goods_info: $('.section_goods_info')
	}
	
	var main = function(){
		handleEvent();
	}
	
	function handleEvent(){
		// 图片放大效果
		$('.jqzoom').imagezoom();
		
		// 添加购物车
		DOM.btn_add_cart.click(function(){
			if(Util.checkUserLogin() == false) return;
			var goods_id = DOM.section_goods_info.attr('goods_id');
			var count = DOM.section_goods_info.find('.inp_count').val();
			var url = _ctx + '/buyer/add_cart';
			var form_data = {
					'goods_id':goods_id,
					'count':count
			}
			$.ajax({
					url:url,
					type:'post',
					contentType:'application/json',
					dataType:'json',
					data:JSON.stringify(form_data),
					success:function(data){
						if(data.result){
							console.log('添加购物侧成功');
						}else{
							alert(data.error_msg);
						}
					}
			});
		});
		
		// 立即购买
		DOM.btn_buy_now.click(function(){
			if(Util.checkUserLogin() == false) return;
			var goods_id = DOM.section_goods_info.attr('goods_id');
			var count = DOM.section_goods_info.find('.inp_count').val();
			var url = _ctx + '/buyer/buy_info';
			var goods_data = {
					'goods_id':goods_id,
					'count':count
			};
			var form_data = {
					'type':'goods_buy',
					'goods_data':JSON.stringify(goods_data)
			}
			Util.StandardPost(url,form_data);
		});
	}
	
	var method = {
			main:main
	}
	
	page.prototype = method;
	return page;
});

