define(['jquery','imagezoom','util'],function($, Imagezoom, Util){
	'use strict';
	
	var page = function(){}
	
	var DOM = {
			photo_main: $('.photo_main'),
			btn_add_cart: $('.btn_add_cart'),
			btn_buy_now: $('.btn_buy_now'),
			photo_: $('.photo_'),
			section_goods_info: $('.section_goods_info')
	}
	
	var main = function(){
		handleEvent();
	}
	
	function handleEvent(){
		// 图片放大效果
		$('.jqzoom').imagezoom({
			xzoom: 350,
			yzoom: 350,
			offset: 10,
			position: "BTR",
			preload: 1
		});
		
		DOM.photo_.find('.photo_list ul>li').hover(function(){
			var img_src = $(this).find('img').attr('src');
			$(this).addClass('active').siblings('active');
			DOM.photo_.find('.photo_main img').attr('src',img_src).attr('rel',img_src);
		});
		
		// 改变商品项数量
		DOM.section_goods_info.find('.item_count .btn_change_count').click(function(){
			var type = $(this).attr('type');
			var $item_count = $(this).parents('.item_count');
			var count = $item_count.find('.inp_count').val();
			if(/^[1-9][0-9]*$/.test(count)==false){
				count = 1;
			}else if(type=='add'){
				count++;
			}else if(type=='subtract'){
				if(count>1){
					count--;
				}
			}
			$item_count.find('.inp_count').val(count);
		});
		
		// 手动输入数量
		DOM.section_goods_info.find('.item_count .inp_count').change(function(){
			var count = $(this).val();
			if(/^[1-9][0-9]*$/.test(count)==false){
				count = 1;
				$(this).val(count);
			}
		});
		
		// 添加购物车
		DOM.btn_add_cart.click(function(){
			if(Util.checkUserLogin() == false) return;
			var $this = $(this);
			var goods_id = DOM.section_goods_info.attr('goods_id');
			var count = DOM.section_goods_info.find('.inp_count').val();
			var url = _ctx + '/buyer/add_cart';
			var form_data = {
					'goods_id':goods_id,
					'count':count
			}
			var result = false;
			$.ajax({
					url:url,
					type:'post',
					contentType:'application/json',
					dataType:'json',
					async:false,
					data:JSON.stringify(form_data),
					success:function(data){
						if(data.result){
							result = true;
							$this.html('<span class="add_cart_success">已成功加入</span>');
							$this.find('.add_cart_success').fadeOut(1500);
							window.setTimeout(function(){
								$this.html('加入购物车');
							},1500);
						}else{
							alert(data.error_msg);
						}
					}
			});
			Util.refresh_shopping_cart(result);
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

