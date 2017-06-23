package com.hanson.foundation.tools;

import java.text.SimpleDateFormat;

import com.hanson.core.tools.CommUtil;
import com.hanson.foundation.domain.Cart;
import com.hanson.foundation.domain.GoodsItem;

public class CartHelper {

	private static CartHelper instance = new CartHelper();
	
	private CartHelper(){}
	
	/**
	 * 单例模式
	 * @return
	 */
	public static CartHelper getInstance(){
		return instance;
	}
	
	/**
	 * 计算购物车的总价格
	 * @param cart
	 * @return
	 */
	public static double calculateCartTotalPrice(Cart cart){
		double total_price = 0;
		for(GoodsItem goods_item : cart.getGoods_item_list()){
			double item_money = CommUtil.mul(goods_item.getGoods().getPrice(), goods_item.getCount());
			total_price += item_money;
		}
		total_price = CommUtil.formatMoney(total_price); // 只保留两位小数
		return total_price;
	}
	
	/**
	 * 计算购物车的总数量
	 * @param cart
	 * @return
	 */
	public static double calculateCartTotalCount(Cart cart){
		int total_count = 0;
		for(GoodsItem goods_item : cart.getGoods_item_list()){
			total_count += goods_item.getCount();
		}
		return total_count;
	}
}
