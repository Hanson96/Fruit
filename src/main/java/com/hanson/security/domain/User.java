package com.hanson.security.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hanson.core.domain.IdEntity;
import com.hanson.core.tools.CustomEnum;

/**
 * 所有用户
 * @author hanson
 *
 */
@Entity
@Table(name="sec_user")
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
		STUDENT("学生",0),ADMIN("管理员",99);
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
	
	
}
