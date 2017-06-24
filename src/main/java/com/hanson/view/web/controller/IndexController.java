package com.hanson.view.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hanson.core.mv.JModelAndView;
import com.hanson.core.query.QueryObject;
import com.hanson.core.tools.SimpleMap;
import com.hanson.core.tools.WebViewHelper;
import com.hanson.foundation.domain.AdvertisementPhoto;
import com.hanson.foundation.domain.Cart;
import com.hanson.foundation.domain.Goods;
import com.hanson.foundation.service.IAdvertisementPhotoService;
import com.hanson.foundation.service.IGoodsService;
import com.hanson.foundation.tools.CartHelper;
import com.hanson.security.domain.User;
import com.hanson.security.service.IUserService;
import com.hanson.security.shiro.ShiroUtils;

@Controller
public class IndexController {

	@Autowired
	private IAdvertisementPhotoService advertisementPhotoService;
	@Autowired
	private IGoodsService goodsService;
	@Autowired
	private IUserService userService;
	
	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request){
		JModelAndView mv = new JModelAndView("index.html", 0, request);
		Map map = new HashMap();
		map.put("position", AdvertisementPhoto.Position.INDEX_SLIDE.value());
		String queryStr = "select obj from AdvertisementPhoto obj where obj.sequence>=0 and obj.position=:position order by obj.sequence asc";
		List<AdvertisementPhoto> index_slide_list = this.advertisementPhotoService.query(queryStr, map, -1, -1);
		// 首页轮播图
		mv.addObject("index_slide_list", index_slide_list);
		// 最新发布的5个商品
		mv.addObject("new_goods_list", this.goodsService.findNewGoods(5));
		// 团购商品  最新的5个
		mv.addObject("group_goods_list", this.goodsService.findNewGroupGoods(5));
		// 热门商品  销量最高的5个
		mv.addObject("hot_goods_list", this.goodsService.findHotGoods(5));
		// 推荐商品  最多5个
		mv.addObject("recommend_goods_list", this.goodsService.findRecommendGoods(5));
		mv.addObject("ActivityStatus", WebViewHelper.enumToMap(Goods.ActivityStatus.values()));
		return mv;
	}
	
	@RequestMapping("/top")
	public ModelAndView top(HttpServletRequest request){
		JModelAndView mv = new JModelAndView("top.html", 0, request);
		User user = this.userService.getObjById(ShiroUtils.getUserId());
		if(user!=null && user.getCart_list().size() > 0){
			mv.addObject("user_cart", user.getCart_list().get(0));
		}
		return mv;
	}
	
	@RequestMapping("/header")
	public ModelAndView header(HttpServletRequest request){
		JModelAndView mv = new JModelAndView("header.html", 0, request);
		QueryObject qo = new QueryObject();
		qo.addQuery("and obj.sequence>=0", null);
		qo.addQuery("obj.position", new SimpleMap("position", AdvertisementPhoto.Position.LOGO.value()), "=");
		qo.setMax(1);
		List<AdvertisementPhoto> logo_list = this.advertisementPhotoService.query(qo);
		if(logo_list.size()>0){
			mv.addObject("logo", logo_list.get(0));
		}
		/*User user = this.userService.getObjById(ShiroUtils.getUserId());
		if(user!=null && user.getCart_list().size()>0){
			mv.addObject("user_cart", user.getCart_list().get(0));
			mv.addObject("CartHelper", CartHelper.getInstance());
		}*/
		return mv;
	}
	
	@RequestMapping("/footer")
	public ModelAndView footer(HttpServletRequest request){
		JModelAndView mv = new JModelAndView("footer.html", 0, request);
		return mv;
	}
}
