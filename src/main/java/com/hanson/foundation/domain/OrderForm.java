package com.hanson.foundation.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hanson.core.domain.IdEntity;
import com.hanson.core.tools.CustomEnum;
import com.hanson.security.domain.User;

/**
 * 订单实体
 * @author hanson
 *
 */
@Entity
@Table(name="order_form")
public class OrderForm extends IdEntity{
	
	// 购物车所属用户
	@ManyToOne
	private User user;
	// 订单编号
	private String number;
	// 商品项
	@OneToMany(mappedBy="order")
	private List<GoodsItem> goods_item_list = new ArrayList<GoodsItem>();
	// 订单总价
	@Column(precision=12, scale=2)
	private BigDecimal total_price;
	
	// 收货人
	private String consignee;
	// 收货人手机号
	private String phone;
	// 收货地址
	private String address;
	// 支付方式
	private String payment;
	// 支付时间
	private Date pay_time;
	
	@Column(columnDefinition="int default 0")
	private Integer pay_status = 0;
	// 支付状态枚举
	public static enum PayStatus implements CustomEnum{
		NO_PAY("未支付",0),PAID("已支付",1);
		private String typeName;
		private int value;
		private PayStatus(String typeName, int value){
			this.typeName = typeName;
			this.value = value;
		}
		public int value(){
			return this.value;
		}
		public String typeName(){
			return this.typeName;
		}
		@Override
		public String originalName() {
			return this.name();
		}
	}
	
	@Transient
	public String parsePay_status(){
		for(OrderForm.PayStatus type : OrderForm.PayStatus.values()){
			if(this.pay_status.intValue() == type.value()){
				return type.typeName();
			}
		}
		return "";
	}
	
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getConsignee() {
		return consignee;
	}
	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public List<GoodsItem> getGoods_item_list() {
		return goods_item_list;
	}
	public void setGoods_item_list(List<GoodsItem> goods_item_list) {
		this.goods_item_list = goods_item_list;
	}
	public BigDecimal getTotal_price() {
		return total_price;
	}
	public void setTotal_price(BigDecimal total_price) {
		this.total_price = total_price;
	}
	public String getPayment() {
		return payment;
	}
	public void setPayment(String payment) {
		this.payment = payment;
	}
	public Integer getPay_status() {
		return pay_status;
	}
	public void setPay_status(Integer pay_status) {
		this.pay_status = pay_status;
	}
	public Date getPay_time() {
		return pay_time;
	}
	public void setPay_time(Date pay_time) {
		this.pay_time = pay_time;
	}
	
}
