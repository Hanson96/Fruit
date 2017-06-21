package com.hanson.security.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hanson.core.domain.IdEntity;
import com.hanson.core.tools.CustomEnum;
import com.hanson.foundation.domain.Cart;

/**
 * 所有用户
 * @author hanson
 *
 */
@Entity
@Table(name="user")
public class User extends IdEntity{

	/** 用户名*/
	private String userName;
	/** 密码*/
	private String password;
	
	// 用户类型（0：企业用户，1：区级用户，2：市级用户，  99：系统管理员）
	@Column(columnDefinition="int default 0")
	private Integer user_type = 0;
	// 用户类型枚举
	public static enum UserType implements CustomEnum{
		BUYER("买家",0),ADMIN("管理员",99);
		private String typeName;
		private int value;
		private UserType(String typeName, int value){
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
	public String parseUserType(){
		int user_type = this.user_type.intValue();;
		for(User.UserType type : User.UserType.values()){
			if(this.user_type.intValue() == type.value()){
				return type.typeName();
			}
		}
		return "";
	}
	
	// 收货地址
	private String address;
	
	@OneToMany(mappedBy="user")
	private List<Cart> cart_list = new ArrayList<Cart>();
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getUser_type() {
		return user_type;
	}
	public void setUser_type(Integer user_type) {
		this.user_type = user_type;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public List<Cart> getCart_list() {
		return cart_list;
	}
	public void setCart_list(List<Cart> cart_list) {
		this.cart_list = cart_list;
	}
	@Transient
	public List<String> getRolesName(){
		List<String> rolesName = new ArrayList<>();
		if(this.user_type.intValue() == User.UserType.BUYER.value()){
			rolesName.add("buyer");
		}else if(this.user_type.intValue() == User.UserType.ADMIN.value()){
			rolesName.add("admin");
		}
		return rolesName;
	}
}
