define(['jquery','util'],function($, Util){
	'use strict';
	
	var page = function(){}
	
	var DOM = {
			search_condition: $('.search_condition'),
			condition_activity: $('.search_condition .condition_activity'),
			condition_goods_class: $('.search_condition .condition_goods_class'),
			condition_price: $('.search_condition .condition_price'),
			condition_sort: $('.search_condition .condition_sort'),
			pagingNode: $('#pagingNode')
	}
	
	var main = function(){
		handleEvent();
	}
	
	function handleEvent(){
		// 选择条件事件
		DOM.search_condition.find('span[condition=1]').click(function(){
			$(this).addClass('select').siblings().removeClass('select');
			search([1,'','']);
		});
		
		DOM.search_condition.find('span[condition=all]').click(function(){
			$(this).addClass('select').siblings().removeClass('select');
			search([1,'','']);
		});
		
		DOM.condition_price.find('input[name^=price_]').blur(function(){
			var price_value = $(this).val();
			if(!/^\+?\d+\.?\d{0,2}$/.test(price_value)){
				var span_error = '<span class="text-danger temp_msg">输入格式有误</span>';
				DOM.condition_price.append(span_error);
				DOM.condition_price.find('.temp_msg').fadeOut(1500);
				window.setTimeout(function(){
					DOM.condition_price.find('.temp_msg').remove();
				},1500);
				$(this).val('');
			}
			var low = DOM.condition_price.find('.inp_price_low').val();
			var high = DOM.condition_price.find('.inp_price_high').val();
			if(low=='' && high==''){
				DOM.condition_price.find('span[condition=all]').addClass('select');
			}else{
				DOM.condition_price.find('span[condition=all]').removeClass('select');
			}
		});
		
		DOM.condition_price.find('.btn_condition_price').click(function(){
			search([1,'','']);
		});
		
		// 点击页码查询
		DOM.pagingNode.on('click','ul.pagination li',function(){
			var currentPage = $(this).find('a').attr('number');
			var goods_name = $('#header #search .inp_search').val();
			search([currentPage,'', goods_name]);
		});
		DOM.pagingNode.on('click','.btn_skip_number',function(){
			var currentPage = DOM.pagingNode.find('.inp_skip_number').val();
			var goods_name = $('#header #search .inp_search').val();
			search([currentPage,'', goods_name]);
		});
	}
	
	// 执行搜素   param_array数组   第0个代表currentPage，第1个代表pageRows，第2个代表q_name   要求都不能为空
	function search(param_array){
		if(param_array[1]==''){
			var pageRows = DOM.pagingNode.find('.inp_page_rows').val();
			if(pageRows == ''){
				param_array[1] = 20; // 默认一页显示的个数
			}else{
				param_array[1] = pageRows;
			}
		}
		var group_id = '';
		var goods_class_id = '';
		var $group_id = DOM.condition_activity.find('span[condition=1].select');
		if($group_id.length>0){
			group_id = $group_id.attr('group_id');
		}
		var $goods_class_id = DOM.condition_goods_class.find('span[condition=1].select');
		if($goods_class_id.length>0){
			goods_class_id = $goods_class_id.attr('goods_class_id');
		}
		var price_low = DOM.condition_price.find('.inp_price_low').val();
		var price_high = DOM.condition_price.find('.inp_price_high').val();
		var sort = '';
		var $sort = DOM.condition_sort.find('span[condition=1].select');
		if($sort.length>0){
			sort = $sort.attr('sort');
		}
		var form_data = {
				'currentPage':param_array[0],
				'pageRows':param_array[1],
				'q_name':param_array[2],
				'q_group_id':group_id,
				'q_goods_class.id':goods_class_id,
				'q_price_low':price_low,
				'q_price_high':price_high,
				'q_sort':sort
		}
		console.log(JSON.stringify(form_data));
		var url = _ctx + '/search';
		Util.StandardPost(url, form_data);
	}
	
	var method = {
			main:main,
			init_search_condition:function(queryMap_JSON){
				if(!Util.isEmpty(queryMap_JSON)){
					/*if(queryMap_JSON.q_name){
						$('#header #search .inp_search').val(queryMap_JSON.q_name);
					}*/
					if(queryMap_JSON.q_group_id){
						DOM.condition_activity.find('span[condition=1][group_id='+queryMap_JSON.q_group_id+']')
							.addClass('select').siblings().removeClass('select');
					}
					if(queryMap_JSON.q_goods_class_id){
						DOM.condition_goods_class.find('span[condition=1][goods_class_id='+queryMap_JSON.q_goods_class_id+']')
							.addClass('select').siblings().removeClass('select');
					}
					if(queryMap_JSON.q_price_low || queryMap_JSON.q_price_high){
						DOM.condition_price.find('span[condition=1]').removeClass('select');
					}
					if(queryMap_JSON.q_sort){
						DOM.condition_sort.find('span[condition=1][sort='+queryMap_JSON.q_sort+']')
							.addClass('select').siblings().removeClass('select');
					}
				}
			}
	}
	
	page.prototype = method;
	return page;
});

