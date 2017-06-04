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
@Table(name="group")
public class Group extends IdEntity{

	// 团购活动名称
	private String name;
	
	@OneToMany(mappedBy="group")
	private List<Goods> goods_list = new ArrayList<Goods>();
	// 活动开始时间
	private Date start_time;
	// 活动结束时间
	private Date end_time;
}
