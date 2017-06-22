define(['jquery','util'],function($, Util){
	'use strict';
	
	var page = function(){}
	
	var DOM = {
			table_cart: $('.table_cart'),
			btn_settle: $('.table_cart .tr_end .item_settle .btn_settle')
	}
	
	var main = function(){
		handleEvent();
	}
	
	function handleEvent(){
		// 改变商品项数量
		DOM.table_cart.find('.goods_item .item_count .btn_change_count').click(function(){
			var type = $(this).attr('type');
			var $goods_item = $(this).parents('.goods_item');
			var price = parseFloat($goods_item.find('.item_price .number').html());
			var count = $goods_item.find('.inp_count').val();
			if(/^[1-9][0-9]*$/.test(count)==false){
				count = 1;
			}else if(type=='add'){
				count++;
			}else if(type=='subtract'){
				if(count>1){
					count--;
				}
			}
			$goods_item.find('.inp_count').val(count);
			var subtotal = price * count;
			$goods_item.find('.item_subtotal .number').html(subtotal.toFixed(2));
			calculateTotalPrice();
		});
		
		// 手动输入数量
		DOM.table_cart.find('.goods_item .item_count .inp_count').change(function(){
			var $goods_item = $(this).parents('.goods_item');
			var price = parseFloat($goods_item.find('.item_price .number').html());
			var count = $(this).val();
			if(/^[1-9][0-9]*$/.test(count)==false){
				count = 1;
				$(this).val(count);
			}
			var subtotal = price * count;
			$goods_item.find('.item_subtotal .number').html(subtotal.toFixed(2));
			calculateTotalPrice();
		});
		
		// 全选或全不选
		DOM.table_cart.find('.tr_end .item_select_all input[name=all]').click(function(){
			var $checkbox = $(this);
			var check = $checkbox.is(':checked');
			$checkbox.attr('checked',check);
			DOM.table_cart.find('.goods_item .item_checkbox_id input[name=goods_item_id]').prop('checked',check);
			calculateTotalPrice();
		});
		
		// 结算商品
		DOM.btn_settle.click(function(){
			var goods_item_id_array = new Array();
			DOM.table_cart.find('.goods_item .item_checkbox_id input[name=goods_item_id]:checked').each(function(){
				goods_item_id_array.push($(this).val());
			});
			if(goods_item_id_array.length==0){
				alert('请先选择要结算的商品');
				return;
			}
			var url = _ctx + '/buyer/buy_info';
			var goods_item_data = {
					'goods_item_ids':goods_item_id_array.join(',')
			};
			var form_data = {
					'type':'goods_item_buy',
					'goods_item_data':JSON.stringify(goods_item_data)
			}
			Util.StandardPost(url,form_data);
		});
	}
	
	function calculateTotalPrice(){
		var total_price = 0;
		var check_count = 0;
		var $tr_end = DOM.table_cart.find('.tr_end');
		DOM.table_cart.find('.goods_item').each(function(){
			var $goods_item = $(this);
			if($goods_item.find('.item_checkbox_id input[name=goods_item_id]').is(':checked')){
				check_count++;
				var subtotal = parseFloat($goods_item.find('.item_subtotal .number').html());
				total_price += subtotal;
			}
		});
		if(check_count==0){
			$tr_end.find('.item_settle .btn_settle').attr('disabled','disabled');
		}else{
			$tr_end.find('.btn_settle').removeAttr('disabled');
		}
		$tr_end.find('.item_settle .number').html(total_price.toFixed(2)); //保留两位小数
	}
	
	var method = {
			main:main,
			calculateTotalPrice:calculateTotalPrice
	}
	
	page.prototype = method;
	return page;
});