package com.hanson.view.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hanson.core.mv.JModelAndView;
import com.hanson.foundation.domain.AdvertisementPhoto;
import com.hanson.foundation.domain.Cart;
import com.hanson.foundation.service.IAdvertisementPhotoService;
import com.hanson.foundation.service.IGoodsService;
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
		String queryStr = "select obj from AdvertisementPhoto obj where obj.sequence>=0 order by obj.sequence asc";
		List<AdvertisementPhoto> index_slide_list = this.advertisementPhotoService.query(queryStr, null, -1, -1);
		// 首页轮播图
		mv.addObject("index_slide_list", index_slide_list);
		// 最新发布的5个商品
		mv.addObject("new_goods_list", this.goodsService.findNewGoods(5));
		return mv;
	}
	
	@RequestMapping("/top")
	public ModelAndView top(HttpServletRequest request){
		JModelAndView mv = new JModelAndView("top.html", 0, request);
		User user = this.userService.getObjById(ShiroUtils.getUserId());
		Integer cart_item_count = null;
		if(user!=null && user.getCart_list().size() > 0){
			mv.addObject("user_cart", user.getCart_list().get(0));
		}
		return mv;
	}
	
	@RequestMapping("/header")
	public ModelAndView header(HttpServletRequest request){
		JModelAndView mv = new JModelAndView("header.html", 0, request);
		
		return mv;
	}
}
