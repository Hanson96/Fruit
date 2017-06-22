package com.hanson.foundation.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hanson.core.domain.IdEntity;
import com.hanson.core.tools.CustomEnum;

/**
 * 广告图片
 * @author hanson
 *
 */
@Entity
@Table(name="advertisement_photo")
public class AdvertisementPhoto extends IdEntity{
	
	// 名称
	private String name;
	// 显示序号，序号越小越前面.负数表示不显示
	@Column(columnDefinition="int default 0")
	private Integer sequence = 0;
	// 对应的链接路径
	private String url;
	// 对应图片
	@OneToOne(cascade={javax.persistence.CascadeType.REMOVE}, fetch=FetchType.LAZY)
	private Accessory acc;
	// 图片位置
	@Column(columnDefinition="int default 0")
	private Integer position = 0;
	
	/** 广告图片的位置*/
	public static enum Position implements CustomEnum{
		LOGIN("登录页面",0),INDEX_SLIDE("首页滚动图",1),LOGO("网站logo",2);
		private String typeName;
		private int value;
		private Position(String typeName, int value){
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
	public String parsePosition(){
		for(AdvertisementPhoto.Position type : AdvertisementPhoto.Position.values()){
			if(this.position.intValue() == type.value()){
				return type.typeName();
			}
		}
		return "";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Accessory getAcc() {
		return acc;
	}

	public void setAcc(Accessory acc) {
		this.acc = acc;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}
	
	
}
