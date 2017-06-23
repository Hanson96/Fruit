package com.hanson.view.buyer.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hanson.core.exception.BusinessException;
import com.hanson.core.mv.JModelAndView;
import com.hanson.core.tools.CommUtil;
import com.hanson.foundation.domain.Cart;
import com.hanson.foundation.domain.Goods;
import com.hanson.foundation.domain.GoodsItem;
import com.hanson.foundation.service.ICartService;
import com.hanson.foundation.service.IGoodsItemService;
import com.hanson.foundation.service.IGoodsService;
import com.hanson.foundation.tools.CartHelper;
import com.hanson.security.domain.User;
import com.hanson.security.service.IUserService;
import com.hanson.security.shiro.ShiroUtils;

@Controller
@RequestMapping("/buyer")
public class CartBuyerController {

	@Autowired
	private IUserService userService;
	@Autowired
	private IGoodsService goodsService;
	@Autowired
	private ICartService cartService;
	@Autowired
	private IGoodsItemService goodsItemService;
	
	// 添加购物车
	@ResponseBody
	@RequestMapping("/add_cart")
	public Map add_cart(HttpServletRequest request, @RequestBody Map<String,String> form_data){
		Map data = new HashMap();
		boolean result = false;
		String error_msg = "";
		User user = this.userService.getObjById(ShiroUtils.getUserId());
		Goods goods = this.goodsService.getObjById(Long.valueOf(Long.valueOf(form_data.get("goods_id"))));
		if(goods==null) {throw new BusinessException("参数有误，请刷新页面后重试");}
		int count = form_data.get("count")==null ? 1 : CommUtil.null2Int(form_data.get("count"));
		Long cart_id = null;
		System.out.println(new JSONObject(form_data).toString());
		if(user.getCart_list().size()==0){
			Cart cart = new Cart();
			cart.setUser(user);
			this.cartService.save(cart);
			cart_id = cart.getId();
			GoodsItem goods_item = new GoodsItem();
			goods_item.setGoods(goods);
			goods_item.setCount(count);
			goods_item.setCart(cart);
			result = this.goodsItemService.save(goods_item);
		}else{
			Cart cart = user.getCart_list().get(0);
			cart_id = cart.getId();
			boolean exist = false;
			for(GoodsItem goods_item : cart.getGoods_item_list()){
				if(goods_item.getGoods().getId().equals(goods.getId())){ // 发现购物车中已经存在此商品 则 增加商品数量
					goods_item.setCount(goods_item.getCount().intValue() + count);
					result = this.goodsItemService.update(goods_item);
					exist = true;
					break;
				}
			}
			if(!exist){ // 不存在就增加一个商品条目
				GoodsItem goods_item = new GoodsItem();
				goods_item.setGoods(goods);
				goods_item.setCount(count);
				goods_item.setCart(cart);
				result = this.goodsItemService.save(goods_item);
			}
		}
		Cart cart_latest = this.cartService.getObjById(cart_id);
		cart_latest.setGoods_item_count(cart_latest.getGoods_item_list().size());
		cart_latest.setTotal_price(BigDecimal.valueOf(CartHelper.calculateCartTotalPrice(cart_latest))); // 更新购物车价格
		this.cartService.update(cart_latest);
		Map cart_data = new HashMap();
		cart_data.put("goods_item_count", cart_latest.getGoods_item_count());
		cart_data.put("total_price", cart_latest.getTotal_price());
		data.put("cart_data", cart_data);
		data.put("result", result);
		data.put("error_msg", error_msg);
		return data;
	}
	
	@RequestMapping("/mycart")
	public ModelAndView mycart(HttpServletRequest request){
		JModelAndView mv = new JModelAndView("mycart.html", 12, request);
		User user = this.userService.getObjById(ShiroUtils.getUserId());
		if(user!=null && user.getCart_list().size()>0){
			mv.addObject("cart", user.getCart_list().get(0));
		}
		return mv;
	}
	
}
