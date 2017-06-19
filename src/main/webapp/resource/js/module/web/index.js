define(['jquery'],function($){
	'user strict';
	var page = function(){};
	
	var DOM = {
			banner_:$('.banner_')
	}
	
	var main = function(){
		handleEvent();
	}
	
	function handleEvent(){
		bannerEvent();
	}
	
	function bannerEvent(){
		// banner轮播
		var banner_width = DOM.banner_.width(); // 单个轮播图宽度
		var banner_length = DOM.banner_.find('ul.image li').length; // 轮播图个数
		var ul_width = banner_width * banner_length; // 所有轮播图连接起来的长度
		var $banner_ul = DOM.banner_.find('ul.image');
		var $banner_circle_ul =  DOM.banner_.find('.circle ul');
		$banner_ul.find('li').width(banner_width);
		$banner_ul.width(ul_width);
		var current = 1; // 当前是第几个轮播图   默认从第一个开始
		var timer = window.setInterval(switch_banner,3000);
		// 轮播图  鼠标停留时   禁止动画
		DOM.banner_.hover(function(){
			window.clearInterval(timer);
		},function(){
			timer = window.setInterval(switch_banner,3000);
		});
		// 圆点停留事件
		$banner_circle_ul.find('li').click(function(){
			current = $(this).index()+1;
			$(this).addClass('active').siblings().removeClass('active');
			left = -1 * (current-1) * banner_width;
			$banner_ul.animate({
				left:left
			});
		});
		function switch_banner(){
			current++;
			if(current > banner_length){
				current = 1;
				$banner_ul.css({
					left:banner_width
				});
			}
			$banner_circle_ul.find('li').removeClass('active').eq(current-1).addClass('active');
			left = -1 * (current-1) * banner_width;
			$banner_ul.animate({
				left:left
			});
		}
	}
	
	var method = {
			main:main
	}
	
	page.prototype = method;
	return page;
});