package com.hanson.view.admin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hanson.core.annotation.Log;
import com.hanson.core.mv.JModelAndView;
import com.hanson.core.query.IPageObject;
import com.hanson.core.query.QueryObject;
import com.hanson.core.tools.CommUtil;
import com.hanson.core.tools.QueryHelper;
import com.hanson.core.tools.WebFormHelper;
import com.hanson.foundation.domain.Goods;
import com.hanson.foundation.domain.Group;
import com.hanson.foundation.domain.SystemLog.LogType;
import com.hanson.foundation.service.IGoodsService;
import com.hanson.foundation.service.IGroupService;

@Controller
@RequestMapping("/admin")
public class GroupGoodsAdminController {

	@Autowired
	private IGoodsService goodsService;
	@Autowired
	private IGroupService groupService;
	
	@RequestMapping("/group_goods_list")
	public ModelAndView group_goods_list(HttpServletRequest request, String currentPage, String pageRows, String q_group_id){
		JModelAndView mv = new JModelAndView("group_goods_list.html", 11, request);
		QueryHelper.queryParamsIntoModel(request, mv);
		QueryObject qo = new QueryObject(mv, currentPage, pageRows, null, null);
		String[] fields = {"name"};
		QueryHelper.addQueryBatch(Goods.class, qo, request, fields, "like");
		if(StringUtils.isNotEmpty(q_group_id)){ // 团购筛选
			Map paramMap = new HashMap();
			paramMap.put("activity_status", Goods.ActivityStatus.GROUP.value());
			paramMap.put("group_id", Long.valueOf(q_group_id));
			String paramString = "and obj.activity_status=:activity_status and obj.group.id=:group_id";
			qo.addQuery(paramString, paramMap);
			Group group  = this.groupService.getObjById(Long.valueOf(q_group_id));
			mv.addObject("group", group);
		}
		IPageObject pageObj = this.goodsService.list(qo);
		mv.addObject("pageObj", pageObj);
		return  mv;
	}
	
	@RequestMapping("/group_goods_add")
	public ModelAndView goods_add(HttpServletRequest request, String obj_id, String group_id){
		JModelAndView mv = new JModelAndView("group_goods_add.html", 11, request);
		if(StringUtils.isNotEmpty(obj_id)){
			Goods obj = this.goodsService.getObjById(Long.valueOf(obj_id));
			mv.addObject("obj", obj);
		}
		if(StringUtils.isNotEmpty(group_id)){
			Group group_current = this.groupService.getObjById(Long.valueOf(group_id));
			mv.addObject("group_current", group_current);
		}
		/*String[] paramNames = {"status"};
		Object[] paramValues = {Group.Status.STARTED.value()};
		List<Group> group_started_list = this.groupService.queryByProperties(paramNames, paramValues, -1, -1);
		mv.addObject("group_started_list", group_started_list);*/
		return  mv;
	}
	
	@Log(title="管理员新增或修改团购商品", type=LogType.SAVE, entityName="Goods")
	@RequestMapping("/group_goods_save")
	public ModelAndView group_goods_save(HttpServletRequest request, String obj_id, Goods goods, 
			String group_id){
		JModelAndView mv = new JModelAndView("handle_msg.html", 11, request);
		boolean result = false;
		String error_msg = "";
		if(StringUtils.isAnyEmpty(obj_id, group_id)){
			error_msg = "参数有误，请重新添加团购商品";
		}else{
			Group group = this.groupService.getObjById(Long.valueOf(group_id));
			Goods goods_true = this.goodsService.getObjById(Long.valueOf(obj_id));
			goods_true = (Goods) WebFormHelper.toPo(request, goods, goods_true, "obj", 0);
			goods_true.setGroup(group);
			goods_true.setActivity_status(Goods.ActivityStatus.GROUP.value());
			result = this.goodsService.update(goods_true);
		}
		
		String msg_title = result ? "操作成功" : error_msg;
		String ctx = CommUtil.getContextPath(request);
		mv.addObject("result", result);
		mv.addObject("msg_title", msg_title);
		mv.addObject("error_msg", error_msg);
		mv.addObject("list_url", ctx + "/admin/group_list");
		return mv;
	}
	
	@Log(title = "管理员删除移除团购商品", type = LogType.DELETE, entityName="Goods")
	@ResponseBody
	@RequestMapping("/group_goods_delete")
	public Map group_goods_delete(HttpServletRequest request, String obj_id){
		Map data = new HashMap();
		boolean result = false;
		String error_msg = "";
		if(StringUtils.isNotEmpty(obj_id)){
			Goods goods = this.goodsService.getObjById(Long.valueOf(obj_id));
			goods.setActivity_status(Goods.ActivityStatus.NORMAL.value());
			goods.setGroup(null);
			goods.setGroup_sold_count(0);
			result = this.goodsService.update(goods);
		}else{
			error_msg = "参数有误";
		}
		data.put("result", result);
		data.put("error_msg", error_msg);
		return data;
	}
	
	@RequestMapping("/group_goods_list_ajax")
	public ModelAndView group_goods_list_ajax(HttpServletRequest request, String currentPage, String pageRows){
		JModelAndView mv = new JModelAndView("group_goods_list_ajax.html", 11, request);
		QueryHelper.queryParamsIntoModel(request, mv);
		QueryObject qo = new QueryObject(mv, currentPage, pageRows, null, null);
		String[] fields = {"name"};
		QueryHelper.addQueryBatch(Goods.class, qo, request, fields, "like");
		// 只能搜索出未参加团购的商品
		Map paramMap = new HashMap();
		paramMap.put("activity_status", Goods.ActivityStatus.NORMAL.value());
		String paramString = "and obj.group.id is null and obj.activity_status=:activity_status";
		qo.addQuery(paramString, paramMap);
		IPageObject pageObj = this.goodsService.list(qo);
		mv.addObject("pageObj", pageObj);
		return  mv;
	}
}
