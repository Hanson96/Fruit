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
	// 销量  
	@Column(columnDefinition="int default 0")
	private Integer sales_count = 0;
	// 是否推荐  
	@Column(columnDefinition="bit default false")
	private Boolean recommend = false;
	
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public GoodsClass getGoods_class() {
		return goods_class;
	}
	public void setGoods_class(GoodsClass goods_class) {
		this.goods_class = goods_class;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public BigDecimal getOriginal_price() {
		return original_price;
	}
	public void setOriginal_price(BigDecimal original_price) {
		this.original_price = original_price;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getProduction_place() {
		return production_place;
	}
	public void setProduction_place(String production_place) {
		this.production_place = production_place;
	}
	public Accessory getMain_photo() {
		return main_photo;
	}
	public void setMain_photo(Accessory main_photo) {
		this.main_photo = main_photo;
	}
	public List<Accessory> getPhoto_list() {
		return photo_list;
	}
	public void setPhoto_list(List<Accessory> photo_list) {
		this.photo_list = photo_list;
	}
	public String getDescription_html() {
		return description_html;
	}
	public void setDescription_html(String description_html) {
		this.description_html = description_html;
	}
	public Integer getActivity_status() {
		return activity_status;
	}
	public void setActivity_status(Integer activity_status) {
		this.activity_status = activity_status;
	}
	public Group getGroup() {
		return group;
	}
	public void setGroup(Group group) {
		this.group = group;
	}
	public BigDecimal getGroup_price() {
		return group_price;
	}
	public void setGroup_price(BigDecimal group_price) {
		this.group_price = group_price;
	}
	public Integer getGroup_count() {
		return group_count;
	}
	public void setGroup_count(Integer group_count) {
		this.group_count = group_count;
	}
	public Integer getGroup_sold_count() {
		return group_sold_count;
	}
	public void setGroup_sold_count(Integer group_sold_count) {
		this.group_sold_count = group_sold_count;
	}
	public Integer getSales_count() {
		return sales_count;
	}
	public void setSales_count(Integer sales_count) {
		this.sales_count = sales_count;
	}
	public Boolean getRecommend() {
		return recommend;
	}
	public void setRecommend(Boolean recommend) {
		this.recommend = recommend;
	}
	
	
	// 只取出5张商品的其它图片
	@Transient
	public List getPhoto_list5(){
		if(this.photo_list.size()<=5){
			return this.photo_list;
		}else{
			return this.photo_list.subList(0, 5);
		}
	} 
}
