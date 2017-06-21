package com.hanson.foundation.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hanson.core.query.QueryObject;
import com.hanson.core.service.impl.CommonServiceImpl;
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

}
