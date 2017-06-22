package com.hanson.view.admin.controller;

import java.util.Date;
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
import com.hanson.core.tools.CommUtil;
import com.hanson.core.tools.QueryHelper;
import com.hanson.foundation.domain.Group;
import com.hanson.foundation.domain.OrderForm;
import com.hanson.foundation.service.IOrderFormService;

@Controller
@RequestMapping("/admin")
public class OrderFormAdminController {

	@Autowired
	private IOrderFormService orderFormService;
	
	@RequestMapping("/order_list")
	public ModelAndView order_list(HttpServletRequest request, String currentPage, String pageRows){
		JModelAndView mv = new JModelAndView("order_list.html", 11, request);
		QueryHelper.queryParamsIntoModel(request, mv);
		QueryObject qo = new QueryObject(mv, currentPage, pageRows, null, null);
		String[] fields = {"number","user.userName","payment"};
		QueryHelper.addQueryBatch(OrderForm.class, qo, request, fields, "like");
		String[] fields1 = {"pay_status"};
		QueryHelper.addQueryBatch(OrderForm.class, qo, request, fields1, "=");
		IPageObject pageObj = this.orderFormService.list(qo);
		mv.addObject("pageObj", pageObj);
		mv.addObject("pay_status_list", OrderForm.PayStatus.values());
		return  mv;
	}
	
	
	@ResponseBody
	@RequestMapping("/order_delete")
	public Map order_delete(HttpServletRequest request, String obj_id){
		Map data = new HashMap();
		boolean result = false;
		String error_msg = "";
		if(StringUtils.isNotEmpty(obj_id)){
			result = this.orderFormService.maintainDelete(Long.valueOf(obj_id));
		}else{
			error_msg = "参数有误";
		}
		data.put("result", result);
		data.put("error_msg", error_msg);
		return data;
	}
}
