package com.hanson.foundation.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hanson.core.query.QueryObject;
import com.hanson.core.service.impl.CommonServiceImpl;
import com.hanson.core.tools.CommUtil;
import com.hanson.core.tools.FileHelper;
import com.hanson.core.tools.SimpleMap;
import com.hanson.foundation.domain.Accessory;
import com.hanson.foundation.domain.Goods;
import com.hanson.foundation.domain.GoodsItem;
import com.hanson.foundation.domain.OrderForm;
import com.hanson.foundation.service.IAccessoryService;
import com.hanson.foundation.service.IGoodsItemService;
import com.hanson.foundation.service.IGoodsService;

@Service
public class GoodsServiceImpl extends CommonServiceImpl<Goods> implements IGoodsService{

	@Autowired
	private IAccessoryService accessoryService;
	@Autowired
	private IGoodsItemService goodsItemService;
	
	@Override
	public boolean cascadeDelete(Long id) {
		Goods obj = this.getObjById(id);
		return cascadeDelete(obj);
	}
	
	public boolean cascadeDelete(Goods obj) {
		for(Accessory photo_other : obj.getPhoto_list()){
			this.accessoryService.maintainDelete(photo_other.getId());
		}
		List<GoodsItem> goods_item_list = this.goodsItemService.queryByProperties(new String[]{"goods.id"}, new Object[]{obj.getId()}, -1, -1);
		for(GoodsItem goods_item : goods_item_list){ 
			if(goods_item.getCart()!=null){ // 如果 是在购物车里的就删掉
				this.goodsItemService.maintainDelete(goods_item.getId());
			}else if(goods_item.getOrder()!=null){ 
				if(goods_item.getOrder().getPay_status() == OrderForm.PayStatus.NO_PAY.value()){
					// 如果订单未支付  则删除此商品条目
					this.goodsItemService.maintainDelete(goods_item.getId());
				}else if(goods_item.getOrder().getPay_status() == OrderForm.PayStatus.PAID.value()){
					// 如果订单已支付  则取消关联
					goods_item.setGoods(null);
					this.goodsItemService.update(goods_item);
				}
			}
		}
		return this.delete(obj.getId());
	}
	
	@Override
	public boolean maintainDelete(Long id) {
		Goods obj = this.getObjById(id);
		FileHelper.deleteAcc(CommUtil.getServletContextBySpring(), obj.getMain_photo()); // 删除主图
		boolean result = cascadeDelete(obj);
		return result;
	}
	
	
	@Override
	public List<Goods> findNewGoods(int count) {
		QueryObject qo = new QueryObject();
		qo.setMax(count);
		return this.query(qo);
	}

	@Override
	public List<Goods> findNewGroupGoods(int count) {
		QueryObject qo = new QueryObject();
		qo.setMax(count);
		Map paramMap = new HashMap();
		paramMap.put("activity_status", Goods.ActivityStatus.GROUP.value());
		String paramString = "and obj.group.id is not null and obj.activity_status=:activity_status";
		qo.addQuery(paramString, paramMap);
		return this.query(qo);
	}

	@Override
	public List<Goods> findHotGoods(int count) {
		QueryObject qo = new QueryObject();
		qo.setMax(count);
		qo.setOrderBy("sales_count");
		qo.setOrderType("desc");
		return this.query(qo);
	}

	@Override
	public List<Goods> findRecommendGoods(int count) {
		QueryObject qo = new QueryObject();
		qo.setMax(count);
		qo.setOrderBy("sales_count");
		qo.setOrderType("desc");
		qo.addQuery("obj.recommend", new SimpleMap("recommend", true), "=");
		return this.query(qo);
	}

	@Override
	public List<Goods> findRandomGoods(int count) {
		String sql = "select obj from Goods obj order by rand() ";
		return this.query(sql, null, 0, count);
	}

}
