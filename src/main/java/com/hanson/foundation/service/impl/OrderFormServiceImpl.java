package com.hanson.foundation.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hanson.core.service.impl.CommonServiceImpl;
import com.hanson.foundation.domain.GoodsItem;
import com.hanson.foundation.domain.OrderForm;
import com.hanson.foundation.service.IGoodsItemService;
import com.hanson.foundation.service.IOrderFormService;

@Service
public class OrderFormServiceImpl extends CommonServiceImpl<OrderForm> implements IOrderFormService{

	@Autowired
	private IGoodsItemService goodsItemService;
	
	@Override
	public boolean cascadeDelete(Long id) {
		OrderForm obj = this.getObjById(id);
		for(GoodsItem goods_item : obj.getGoods_item_list()){
			this.goodsItemService.maintainDelete(goods_item.getId());
		}
		return this.delete(obj.getId());
	}
	
	@Override
	public boolean maintainDelete(Long id) {
		boolean result = cascadeDelete(id);
		return result;
	}
}
