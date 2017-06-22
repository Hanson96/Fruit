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

	/**
	 * 查询最新的团购商品
	 * @param count  查询的条数
	 * @return
	 */
	public List<Goods> findNewGroupGoods(int count);
	
	/**
	 * 查询热门商品  销量最多的
	 * @param count  查询的条数
	 * @return
	 */
	public List<Goods> findHotGoods(int count);
	
	/**
	 * 查询推荐商品   再根据销量排序
	 * @param count  查询的条数
	 * @return
	 */
	public List<Goods> findRecommendGoods(int count);
}
