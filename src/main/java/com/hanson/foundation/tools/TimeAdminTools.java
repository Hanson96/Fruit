package com.hanson.foundation.tools;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hanson.foundation.domain.Goods;
import com.hanson.foundation.domain.Group;
import com.hanson.foundation.service.IGoodsService;
import com.hanson.foundation.service.IGroupService;

/**
 * 时间工具
 * @author hanson
 *
 */
@Component
public class TimeAdminTools {

	@Autowired
	IGroupService groupService; 
	@Autowired
	private IGoodsService goodsService;
	
	/**
	 * 更新团购活动   的状态
	 * @return
	 * @throws ParseException 
	 */
	public boolean updateGroupStatus(){System.out.println("执行时间更新");
		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		int current_year = cal.get(Calendar.YEAR); // 获取当前年份
		
		// 需要用到的时间节点
		Date time_now1 = now;  // 当前时间
		cal.setTime(now);
		
		// 1.更新结束状态
		Map params = new HashMap();
		params.put("status", Group.Status.FINISH.value());
		params.put("time_now", time_now1);
		String sql = "select obj from Group obj where obj.status != :status "
				+ " and obj.end_time < :time_now ";
		List<Group> group_finish_list = this.groupService.query(sql, params, -1, -1);
		updateFinishGroup(group_finish_list);
		
		//2. 更新开始状态
		params.clear();
		params.put("status", Group.Status.STARTED.value());
		params.put("time_now", time_now1);
		sql = "select obj from Group obj where obj.status != :status "
				+ " and obj.start_time <= :time_now and obj.end_time >= :time_now ";
		List<Group> group_started_list = this.groupService.query(sql, params, -1, -1);
		updateStartedGroup(group_started_list);
		
		//3. 更新未开始状态
		params.clear();
		params.put("status", Group.Status.NO_START.value());
		params.put("time_now", time_now1);
		sql = "select obj from Group obj where obj.status != :status "
				+ " and obj.start_time > :time_now ";
		List<Group> group_no_start_list = this.groupService.query(sql, params, -1, -1);
		updateNo_startGroup(group_no_start_list);
		/* 更新许可证的状态     结束------------------------*/
		
		return true;
	}
	
	private boolean updateFinishGroup(List<Group> group_list){
		for(Group group : group_list){
			for(Goods goods : group.getGoods_list()){
				goods.setPrice(goods.getNow_price());
				goods.setActivity_status(Goods.ActivityStatus.NORMAL.value());
				goods.setGroup(null);
				goods.setGroup_sold_count(0);
				this.goodsService.update(goods);
			}
			group.setStatus(Group.Status.FINISH.value());
			this.groupService.update(group);
		}
		return true;
	}
	
	private boolean updateStartedGroup(List<Group> group_list){
		for(Group group : group_list){
			for(Goods goods : group.getGoods_list()){
				goods.setPrice(goods.getGroup_price());
				goods.setActivity_status(Goods.ActivityStatus.GROUP.value());
				this.goodsService.update(goods);
			}
			group.setStatus(Group.Status.STARTED.value());
			this.groupService.update(group);
		}
		return true;
	}
	
	private boolean updateNo_startGroup(List<Group> group_list){
		for(Group group : group_list){
			for(Goods goods : group.getGoods_list()){
				goods.setPrice(goods.getNow_price());
				goods.setActivity_status(Goods.ActivityStatus.NORMAL.value());
				goods.setGroup(null);
				goods.setGroup_sold_count(0);
				this.goodsService.update(goods);
			}
			group.setStatus(Group.Status.NO_START.value());
			this.groupService.update(group);
		}
		return true;
	}
	
}
