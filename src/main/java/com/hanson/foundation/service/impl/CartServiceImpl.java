package com.hanson.foundation.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hanson.core.service.impl.CommonServiceImpl;
import com.hanson.foundation.domain.Cart;
import com.hanson.foundation.domain.GoodsItem;
import com.hanson.foundation.service.ICartService;
import com.hanson.foundation.service.IGoodsItemService;
import com.hanson.security.domain.User;

@Service
public class CartServiceImpl extends CommonServiceImpl<Cart> implements ICartService{

	@Autowired
	private IGoodsItemService goodsItemService;
	
	@Override
	public boolean cascadeDelete(Long id) {
		Cart obj = this.getObjById(id);
		// 删除购物车条目
		for(GoodsItem goods_item : obj.getGoods_item_list()){
			this.goodsItemService.delete(goods_item.getId());
		}
		return this.delete(id);
	}
	
	@Override
	public boolean maintainDelete(Long id) {
		boolean result = cascadeDelete(id);
		return result;
	}
}
