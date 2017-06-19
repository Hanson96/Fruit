package com.hanson.foundation.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.hanson.core.domain.IdEntity;

/**
 * 团购实体
 * @author hanson
 *
 */
@Entity
@Table(name="group_activity")
public class Group extends IdEntity{

	// 团购活动名称
	private String name;
	
	@OneToMany(mappedBy="group")
	private List<Goods> goods_list = new ArrayList<Goods>();
	// 活动开始时间
	private Date start_time;
	// 活动结束时间
	private Date end_time;
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
	public Date getStart_time() {
		return start_time;
	}
	public void setStart_time(Date start_time) {
		this.start_time = start_time;
	}
	public Date getEnd_time() {
		return end_time;
	}
	public void setEnd_time(Date end_time) {
		this.end_time = end_time;
	}
	
	
}
