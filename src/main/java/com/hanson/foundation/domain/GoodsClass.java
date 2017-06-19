package com.hanson.foundation.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.hanson.core.domain.IdEntity;

/**
 * 商品分类实体
 * @author hanson
 *
 */
@Entity
@Table(name="goods_class")
public class GoodsClass extends IdEntity{

	// 商品分类名称
	private String name;

	@OneToMany(mappedBy="goods_class")
	private List<Goods> goods_list = new ArrayList<Goods>();
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Goods> getGoods_list() {
		return goods_list;
	}

	public void setGoods_list(List<Goods> goods_list) {
		this.goods_list = goods_list;
	}
	
	
}
