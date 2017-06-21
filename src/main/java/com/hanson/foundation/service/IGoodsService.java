package com.hanson.foundation.service;

import java.util.List;

import com.hanson.core.service.ICommonService;
import com.hanson.foundation.domain.Goods;

public interface IGoodsService extends ICommonService<Goods>{

	/**
	 * 查询最新发布的商品
	 * @param count  查询的条数
	 * @return
	 */
	public List<Goods> findNewGoods(int count);
}
