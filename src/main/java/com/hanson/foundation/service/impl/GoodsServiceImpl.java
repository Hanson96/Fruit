package com.hanson.foundation.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hanson.core.query.QueryObject;
import com.hanson.core.service.impl.CommonServiceImpl;
import com.hanson.core.tools.SimpleMap;
import com.hanson.foundation.domain.Goods;
import com.hanson.foundation.service.IGoodsService;

@Service
public class GoodsServiceImpl extends CommonServiceImpl<Goods> implements IGoodsService{

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

}
