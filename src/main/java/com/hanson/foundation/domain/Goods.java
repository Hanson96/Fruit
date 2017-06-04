package com.hanson.foundation.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hanson.core.domain.IdEntity;
import com.hanson.core.tools.CustomEnum;

/**
 * 商品实体
 * @author hanson
 *
 */
@Entity
@Table(name="goods")
public class Goods extends IdEntity{

	// 商品名称
	private String name;
	// 商品分类
	@ManyToOne
	private GoodsClass goods_class;
	// 现价
	@Column(precision = 12, scale = 2)
	private BigDecimal price;
	// 原价
	@Column(precision = 12, scale = 2)
	private BigDecimal original_price;
	// 计量单位
	private String unit;
	// 产地
	private String production_place;
	// 商品主图
	@OneToOne(cascade={CascadeType.REMOVE})
	private Accessory main_photo;
	// 商品其它图片
	@OneToMany(mappedBy="goods")
	private List<Accessory> photo_list = new ArrayList<Accessory>();
	// 商品详情描述的html
	@Lob
    @Column(columnDefinition = "LongText")
    private String description_html;
	
	// 活动状态  
	@Column(columnDefinition="int default 0")
	private Integer activity_status = 0;
	// 活动状态枚举
	public static enum ActivityStatus implements CustomEnum{
		NORMAL("正常商品",0),GROUP("团购商品",1);
		private String typeName;
		private int value;
		private ActivityStatus(String typeName, int value){
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
	public String parseActivity_status(){
		for(Goods.ActivityStatus enum_type : Goods.ActivityStatus.values()){
			if(this.activity_status.intValue() == enum_type.value()){
				return enum_type.typeName();
			}
		}
		return "";
	}
	
	/* ----------- 团购相关    开始  -----------*/
	// 团购活动    一个商品只能参加一个团购活动
	@ManyToOne
	private Group group;
	// 团购商品的 单价
	@Column(precision = 12, scale = 2)
	private BigDecimal group_price;
	// 商品成为团购的起始量   到达这个数量才会发货
	@Column(columnDefinition="int default 1")
	private Integer group_count = 1;
	// 已卖出的数量
	@Column(columnDefinition="int default 0")
	private Integer group_sold_count = 0;
	/* ----------- 团购相关    结束  -----------*/
}
