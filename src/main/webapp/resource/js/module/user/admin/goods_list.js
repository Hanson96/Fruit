define(['jquery','bootstrap'],function($){
	'use strict';
	
	var page = function(){}
	
	var DOM = {
			
	}
	
	var main = function(){
		handleEvent();
		$('[data-toggle="tooltip"]').tooltip();
	}
	
	function handleEvent(){
		
	}
	
	var method = {
			main:main,
			recommend_control:function(element){
				var $this = $(element);
				var obj_id = $this.attr('obj_id');
				var url = _ctx + '/admin/goods_handle';
				$.post(url,{'obj_id':obj_id,'type':'recommend'},function(data){
					if(data.result){
						$this.fadeOut();
						$this.tooltip('hide');
						setTimeout(function(){
							if($this.hasClass('btn-primary')){
								$this.removeClass('btn-primary').addClass('btn-success');
								$this.text('已推荐');
								$this.attr('data-original-title','点击取消推荐');
							}else{
								$this.removeClass('btn-success').addClass('btn-primary');
								$this.text('未推荐');
								$this.attr('data-original-title','点击进行推荐');
							}
							$this.fadeIn();
							$this.tooltip('show');
						},500);
					}else{
						alert(data.error_msg);
					}
				},'json');
			}
	}
	
	page.prototype = method;
	return page;
});