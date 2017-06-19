/**
 * 通用的方法
 * @author hanson
 */
define(['jquery'],function($){
	
	"use strict";
	var common = function(){}
	
	// 判断是否为空
	function isEmpty(obj){
		if(typeof(obj)=='undefined' || obj==null || obj=='undefined' || obj==""){
			return true;
		}
		return false;
	}
	
	// 默认的分页样式class
	var paging_default_class = {
			page_wrap_c:'page-wrap',
			page_word_wrap_c:'page-word-wrap',
			page_word_number_inp_c:'page-word-number-inp',
			page_word_skip_btn_c:'page-word-skip-btn',
			page_number_wrap_c:'page-number-wrap',
			page_first_c:'page-first',
			page_previous_c:'page-previous',
			page_number_c:'page-number',
			page_current_c:'page-current',
			page_next_c:'page-next',
			page_last_c:'page-last'
	}
	/*
	 * 分页关键节点的默认配置，：插入分页模板的分页节点、查询表单节点、查询表单中的当前页码输入框 节点
	 * 实际上点击页数时  就是改变查询表单的 当前页码字段，然后提交查询表单
	 */   
	var paging_node_default_config = {
			paging_node:'#pagingNode',
			query_form_node:'#queryForm',
			query_form_currentPage_inp:'#currentPage',
			query_form_pageRows_inp:'#pageRows',
			query_form_search_btn:'#queryFormBtn'
	}
	
	// 获取分页需要用到的参数    以便于自定义构造html
	function get_paging_parameter(currentPage, pageRows, totalPages, totalRows){
		if(totalPages > 0){
			/* 构造html页码 */
			var page_previous = (currentPage-1) >= 1 ? (currentPage-1) : currentPage; // 上一页
			var page_next = (currentPage+1) <= totalPages ? (currentPage+1) : totalPages; // 下一页
			var page_begin =  (currentPage-3) >= 1 ? (currentPage-3) : 1; // 第一个页数
			var page_end = (currentPage+3) <= totalPages ? (currentPage+3) : totalPages;  // 最后一个页数
			var page_first = 1;  // 首页
			var page_last = totalPages;  // 尾页
			var page_current = currentPage; // 当前页
			var page_total_number = totalPages; // 总共的页数
			var page_total_rows = totalRows; // 总共的记录数
			var page_rows = isEmpty(pageRows) ? 10 : pageRows; // 每页显示记录数，默认显示10条
			var page = {
					page_previous:page_previous,
					page_next:page_next,
					page_begin:page_begin,
					page_end:page_end,
					page_first:page_first,
					page_last:page_last,
					page_current:page_current,
					page_total_number:page_total_number,
					page_total_rows:page_total_rows,
					page_rows:page_rows
			}
			return page;
		}
		return null;
	}
	
	// 获取分页的   页码html模板
	function get_paging_template_html(config, page){
		var paging_html = '';
		// 生成分页模板
		paging_html += '<div class="'+config.page_wrap_c+'">'
		paging_html += '<div class="'+config.page_word_wrap_c+'">'+
							'<span>共'+page.page_total_rows+'条记录，'+page.page_total_number+'页；</span>'+
							'<span>显示<input type="text" value="'+page.page_rows+'" class="inp_page_rows '+config.page_word_number_inp_c+'"/>条/页，'+
								'跳到第<input type="text" class="inp_skip_number '+config.page_word_number_inp_c+'"/>页'+
								'<button class="btn_skip_number '+config.page_word_skip_btn_c+'">确定</button>'+
							'</span>'+
					   '</div>';
		paging_html += '<ul class="'+config.page_number_wrap_c+'">'+
						'<li class="'+config.page_first_c+'"><a number="'+page.page_first+'">首页</a></li>'+
						'<li class="'+config.page_previous_c+'"><a number="'+page.page_previous+'">&laquo;</a></li>';
		for(var i=page.page_begin; i<=page.page_end; i++){
			if(i==page.page_current){
				paging_html += '<li class="'+config.page_number_c+' '+config.page_current_c+'"><a number="'+i+'">'+i+'</a></li>';
			}else{
				paging_html += '<li class="'+config.page_number_c+'"><a number="'+i+'">'+i+'</a></li>';
			}
		}
		paging_html += '<li class="'+config.page_next_c+'"><a number="'+page.page_next+'">&raquo;</a></li>'+
						'<li class="'+config.page_last_c+'"><a number="'+page.page_last+'">尾页</a></li>'+
						'</ul>'+
						'</div>';
		return paging_html;
	}
	
	// 对外访问的方法
	var method = {
			paging:function(currentPage, pageRows, totalPages, totalRows, pagingNodeCofig, classConfig){
				var paging_html = '';
				if(totalPages > 0){
					var page = get_paging_parameter(currentPage, pageRows, totalPages, totalRows);
					// 可自定义class样式
					if(typeof(classConfig)=='undefined' || classConfig=='undefined' || classConfig==""){
						classConfig = {};
					}
					var config = $.extend({}, paging_default_class, classConfig);
					paging_html = get_paging_template_html(config, page);
				}else{
					return null;
				}
				// 分页需要用到的关键节点的配置，查询表单的节点参数  ： 插入分页模板的分页节点 、  查询表单节点  、 查询表单中的当前页码输入框 节点
				if(typeof(pagingNodeCofig)=='undefined' || pagingNodeCofig=='undefined' || pagingNodeCofig==""){
					return paging_html; // 未配置此参数    则返回分页模板html
				}else{
					var node = $.extend({}, paging_node_default_config, pagingNodeCofig);
					$(''+node.paging_node).html(paging_html);
					// 注册点击事件   
					$(''+node.paging_node).on('click','ul.'+config.page_number_wrap_c+' li',function(){
						var number = $(this).find('a').attr('number');
						var page_rows = $(''+node.paging_node).find('.inp_page_rows').val();
						$(''+node.query_form_node+' '+node.query_form_currentPage_inp).val(number);
						$(''+node.query_form_node+' '+node.query_form_pageRows_inp).val(page_rows);
						$(''+node.query_form_node).submit();
					});
					$(''+node.paging_node).on('click','.btn_skip_number',function(){
						var number = $(''+node.paging_node).find('.inp_skip_number').val();
						var page_rows = $(''+node.paging_node).find('.inp_page_rows').val();
						$(''+node.query_form_node+' '+node.query_form_currentPage_inp).val(number);
						$(''+node.query_form_node+' '+node.query_form_pageRows_inp).val(page_rows);
						$(''+node.query_form_node).submit();
					});
					return;
				}
			},
			// 提交查询表单
			get_paging_parameter:get_paging_parameter,
			query:function(queryForm_node){
				if(typeof(queryForm_node)=='undefined' || queryForm_node=='undefined' || queryForm_node==""){
					$('#queryForm').submit(); // 默认使用   #queryForm 作为节点
				}else{
					$(''+queryForm_node).submit();
				}
			},
			// 重置查询表单
			reset:function(queryForm_node){
				// 原生的reset()方法无法清空value里的值
				if(typeof(queryForm_node)=='undefined' || queryForm_node=='undefined' || queryForm_node==""){
					reset($('#queryForm')); // 默认使用   #queryForm 作为节点
				}else{
					reset($(''+queryForm_node));
				}
				function reset(form){
					form.find('input').val('');
					form.find('select').val($(this).find('option:first').val());
				}
			},
			ajax_paging:function(currentPage, pageRows, totalPages, totalRows, pagingNodeCofig, classConfig){
				var paging_html = '';
				if(totalPages > 0){
					var page = get_paging_parameter(currentPage, pageRows, totalPages, totalRows);
					// 可自定义class样式
					if(typeof(classConfig)=='undefined' || classConfig=='undefined' || classConfig==""){
						classConfig = {};
					}
					var config = $.extend({}, paging_default_class, classConfig);
					paging_html = get_paging_template_html(config, page);
				}else{
					return null;
				}
				// 分页需要用到的关键节点的配置，查询表单的节点参数  ： 插入分页模板的分页节点 、  查询表单节点  、 查询表单中的当前页码输入框 节点
				if(typeof(pagingNodeCofig)=='undefined' || pagingNodeCofig=='undefined' || pagingNodeCofig==""){
					return paging_html; // 未配置此参数    则返回分页模板html
				}else{
					var node = $.extend({}, paging_node_default_config, pagingNodeCofig);
					$(''+node.paging_node).html(paging_html);
					// 注册点击事件   
					$(''+node.paging_node).on('click','ul.'+config.page_number_wrap_c+' li',function(){
						var number = $(this).find('a').attr('number');
						var page_rows = $(''+node.paging_node).find('.inp_page_rows').val();
						$(''+node.query_form_node+' '+node.query_form_currentPage_inp).val(number);
						$(''+node.query_form_node+' '+node.query_form_pageRows_inp).val(page_rows);
						$(''+node.query_form_search_btn).click();
					});
					$(''+node.paging_node).on('click','.btn_skip_number',function(){
						var number = $(''+node.paging_node).find('.inp_skip_number').val();
						var page_rows = $(''+node.paging_node).find('.inp_page_rows').val();
						$(''+node.query_form_node+' '+node.query_form_currentPage_inp).val(number);
						$(''+node.query_form_node+' '+node.query_form_pageRows_inp).val(page_rows);
						$(''+node.query_form_search_btn).click();
					});
					return;
				}
			},
			delete_obj:function(url,form_data,callback){
				$.post(url,form_data,function(data){
					if(callback && callback instanceof Function){
						callback(data);
						return true;
					}else{
						if(data.result){
							window.location = window.location.href;
						}else{
							alert(data.error_msg);
						}
					}
				},'json');
			},
			delete_obj_by_id:function(url,id,hint){
				if(typeof(hint)=='undefined' || hint=='undefined' || hint==''){
					hint = '确认删除此条记录？';
				}
				if(confirm(hint)){
					$.post(url,{'obj_id':id},function(data){
						if(data.result){
							window.location = window.location.href;
						}else{
							alert(data.error_msg);
						}
					},'json');
				}
			}
	}
	
	common.prototype = method;
	return common;
	
});