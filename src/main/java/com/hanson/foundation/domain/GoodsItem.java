package com.hanson.foundation.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.hanson.core.domain.IdEntity;

/**
 * 商品项    一个商品项要么只属于购物车。要么只属于订单。当购物车中的商品项结算后，此商品项变为订单的
 * @author hanson
 *
 */
@Entity
@Table(name="goods_item")
public class GoodsItem extends IdEntity{

	// 对应的商品
	@ManyToOne
	private Goods goods;
	// 所属购物车  
	@ManyToOne
	private Cart cart;
	// 所属订单
	@ManyToOne
	private OrderForm order;
	// 商品数量
	@Column(columnDefinition="int default 1")
	private Integer count = 1;
	// 结算后的商品单价
	@Column(precision = 12, scale = 2)
	private BigDecimal price;
	
	// 活动状态    表明是什么活动的商品    与 Goods中的activity_status相对应
	@Column(columnDefinition="int default 0")
	private Integer activity_status = 0;

	public Goods getGoods() {
		return goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public OrderForm getOrder() {
		return order;
	}

	public void setOrder(OrderForm order) {
		this.order = order;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getActivity_status() {
		return activity_status;
	}

	public void setActivity_status(Integer activity_status) {
		this.activity_status = activity_status;
	}
	
	
}
