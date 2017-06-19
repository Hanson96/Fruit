package com.hanson.foundation.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hanson.core.service.impl.CommonServiceImpl;
import com.hanson.foundation.domain.Goods;
import com.hanson.foundation.domain.GoodsClass;
import com.hanson.foundation.service.IGoodsClassService;
import com.hanson.foundation.service.IGoodsService;

@Service
public class GoodsClassServiceImpl extends CommonServiceImpl<GoodsClass> implements IGoodsClassService{

	@Autowired
	private IGoodsService goodsService;
	
	@Override
	public boolean cascadeDelete(Long id) {
		GoodsClass obj = this.getObjById(id);
		for(Goods goods : obj.getGoods_list()){ // 取消商品与商品分类的关联
			goods.setGoods_class(null);
			this.goodsService.update(goods);
		}
		return this.delete(id);
	}
	
	@Override
	public boolean maintainDelete(Long id) {
		boolean result = this.cascadeDelete(id);
		return result;
	}
}
