package com.hanson.foundation.domain;

import javax.persistence.Entity;
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
}
