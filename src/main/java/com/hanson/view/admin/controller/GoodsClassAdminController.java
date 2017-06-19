package com.hanson.view.admin.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hanson.core.mv.JModelAndView;
import com.hanson.core.query.IPageObject;
import com.hanson.core.query.QueryObject;
import com.hanson.core.tools.QueryHelper;
import com.hanson.foundation.domain.GoodsClass;
import com.hanson.foundation.service.IGoodsClassService;

@Controller
@RequestMapping("/admin")
public class GoodsClassAdminController {

	@Autowired
	private IGoodsClassService goodsClassService;
	
	@RequestMapping("/goods_class_list")
	public ModelAndView goods_class_list(HttpServletRequest request, String currentPage, String pageRows){
		JModelAndView mv = new JModelAndView("goods_class_list.html", 11, request);
		QueryHelper.queryParamsIntoModel(request, mv);
		QueryObject qo = new QueryObject(mv, currentPage, pageRows, null, null);
		String[] fields = {"name"};
		QueryHelper.addQueryBatch(GoodsClass.class, qo, request, fields, "like");
		IPageObject pageObj = this.goodsClassService.list(qo);
		mv.addObject("pageObj", pageObj);
		return  mv;
	}
	
	@ResponseBody
	@RequestMapping("/goods_class_save")
	public Map goods_class_save(HttpServletRequest request, String obj_id, String name){
		Map data = new HashMap();
		boolean result = false;
		String error_msg = "";
		GoodsClass gc = null;
		if(StringUtils.isNotEmpty(obj_id)){
			gc = this.goodsClassService.getObjById(Long.valueOf(obj_id));
			gc.setName(name);
		}else{
			gc = new GoodsClass();
			gc.setName(name);
		}
		result = this.goodsClassService.saveOrUpdate(gc);
		data.put("result", result);
		data.put("error_msg", error_msg);
		return data;
	}
	
	@ResponseBody
	@RequestMapping("/goods_class_delete")
	public Map goods_class_delete(HttpServletRequest request, String obj_id){
		Map data = new HashMap();
		boolean result = false;
		String error_msg = "";
		if(StringUtils.isNotEmpty(obj_id)){
			result = this.goodsClassService.maintainDelete(Long.valueOf(obj_id));
		}else{
			error_msg = "参数有误";
		}
		data.put("result", result);
		data.put("error_msg", error_msg);
		return data;
	}
	
	@ResponseBody
	@RequestMapping("/goods_class_validate_name")
	public boolean goods_class_validate_name(String obj_id, String name){
		Long id = StringUtils.isNotEmpty(obj_id) ? Long.valueOf(obj_id) : null;
		return !this.goodsClassService.existSameName("name", name, id);
	}
}
