package com.hanson.foundation.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.hanson.core.domain.IdEntity;
import com.hanson.security.domain.User;

/**
 * 购物车实体   
 * @author hanson
 *
 */
@Entity
@Table(name="cart")
public class Cart extends IdEntity{
	// 购物车所属用户
	@ManyToOne
	private User user;
	// 商品项
	@OneToMany(mappedBy="cart")
	private List<GoodsItem> goods_item_list = new ArrayList<GoodsItem>();
	//购物车有多少个商品
	@Column(columnDefinition="int default 0")
	private Integer goods_item_count = 0;
	// 购物车总价
	@Column(precision=12, scale=2)
	private BigDecimal total_price;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<GoodsItem> getGoods_item_list() {
		return goods_item_list;
	}

	public void setGoods_item_list(List<GoodsItem> goods_item_list) {
		this.goods_item_list = goods_item_list;
	}
	
	public Integer getGoods_item_count() {
		return goods_item_count;
	}

	public void setGoods_item_count(Integer goods_item_count) {
		this.goods_item_count = goods_item_count;
	}

	public BigDecimal getTotal_price() {
		return total_price;
	}

	public void setTotal_price(BigDecimal total_price) {
		this.total_price = total_price;
	}
	
}
