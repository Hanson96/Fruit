define(['jquery','util'],function($, Util){
	'use strict';
	
	var page = function(){}
	
	var DOM = {
			search_condition: $('.search_condition'),
			condition_activity: $('.search_condition .condition_activity'),
			condition_goods_class: $('.search_condition .condition_goods_class'),
			condition_price: $('.search_condition .condition_price'),
			condition_sort: $('.search_condition .condition_sort'),
	}
	
	var main = function(){
		handleEvent();
	}
	
	function handleEvent(){
		// 选择条件事件
		DOM.search_condition.find('span[condition=1]').click(function(){
			$(this).addClass('select').siblings().removeClass('select');
		});
		
		DOM.search_condition.find('span[condition=all]').click(function(){
			$(this).addClass('select').siblings().removeClass('select');
		});
	}
	
	// 执行搜素
	function search(){
		var acticity = '';
		var goods_class_id = '';
		var $activity = DOM.condition_activity.find('span[condition=1].select');
		if($activity.length>0){
			acticity = $activity.attr('activity');
		}
		var $goods_class_id = DOM.condition_goods_class.find('span[condition=1].select');
		if($goods_class_id.length>0){
			goods_class_id = $goods_class_id.attr('goods_class_id');
		}
		var price_low = DOM.condition_price.find('.inp_price_low');
		var price_high = DOM.condition_price.find('.inp_price_high');
		var sort = '';
		var $sort = DOM.condition_sort.find('span[condition=1].select');
		if($sort.length>0){
			sort = $sort.attr('sort');
		}
		var form_data = {
				'currentPage':1,
				'pageRows':20,
				'q_acticity':acticity,
				'q_goods_class.id':goods_class_id,
				'q_price_low':price_low,
				'q_price_high':price_high,
				'q_sort':sort
		}
		var url = _ctx + '/search';
		Util.StandardPost(url, form_data);
	}
	
	var method = {
			main:main
	}
	
	page.prototype = method;
	return page;
});

