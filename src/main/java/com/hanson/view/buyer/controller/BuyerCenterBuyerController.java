package com.hanson.view.buyer.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hanson.core.exception.BusinessException;
import com.hanson.core.mv.JModelAndView;
import com.hanson.core.query.IPageObject;
import com.hanson.core.query.QueryObject;
import com.hanson.core.tools.QueryHelper;
import com.hanson.core.tools.SimpleMap;
import com.hanson.core.tools.WebFormHelper;
import com.hanson.core.tools.WebViewHelper;
import com.hanson.foundation.domain.OrderForm;
import com.hanson.foundation.service.IOrderFormService;
import com.hanson.security.domain.User;
import com.hanson.security.service.IUserService;
import com.hanson.security.shiro.ShiroUtils;

@Controller
@RequestMapping("/buyer")
public class BuyerCenterBuyerController {

	@Autowired
	private IUserService userService;
	@Autowired
	private IOrderFormService orderFormService;
	
	/*@RequestMapping("buyer_center")
	public ModelAndView buyer_center(HttpServletRequest request){
		JModelAndView mv = new JModelAndView("buyer_center.html", 12, request);
		User user = this.userService.getObjById(ShiroUtils.getUserId());
		mv.addObject("user", user);
		return mv;
	}*/
	
	@RequestMapping("/buyer_info")
	public ModelAndView buyer_info(HttpServletRequest request){
		JModelAndView mv = new JModelAndView("buyer_info.html", 12, request);
		User user = this.userService.getObjById(ShiroUtils.getUserId());
		mv.addObject("user", user);
		return mv;
	}
	
	@ResponseBody
	@RequestMapping("/buyer_info_save")
	public Map buyer_info_save(HttpServletRequest request, String obj_id, User user){
		Map data = new HashMap();
		boolean result = false;
		String error_msg = "";
		User user_true = this.userService.getObjById(ShiroUtils.getUserId());
		if(StringUtils.isNotEmpty(obj_id)){
			if(!StringUtils.equals(String.valueOf(user_true.getId()), obj_id)){
				throw new BusinessException("这些个人信息不属于您，不允许修改");
			}
			WebFormHelper.toPo(request, user, user_true, "obj", 0);
			result = this.userService.update(user_true);
			
		}else{
			error_msg = "参数有误";
		}
		data.put("result", result);
		data.put("error_msg", error_msg);
		return data;
	}
	
	@RequestMapping("/myorder")
	public ModelAndView myorder(HttpServletRequest request, String currentPage, String pageRows){
		JModelAndView mv = new JModelAndView("myorder.html", 12, request);
		User user = this.userService.getObjById(ShiroUtils.getUserId());
		QueryHelper.queryParamsIntoModel(request, mv);
		QueryObject qo = new QueryObject(mv, currentPage, pageRows, null, null);
		String[] fields = {"number","payment"};
		QueryHelper.addQueryBatch(OrderForm.class, qo, request, fields, "like");
		String[] fields1 = {"pay_status"};
		QueryHelper.addQueryBatch(OrderForm.class, qo, request, fields1, "=");
		qo.addQuery("obj.user.id", new SimpleMap("user_id", user.getId()), "=");
		IPageObject pageObj = this.orderFormService.list(qo);
		mv.addObject("pageObj", pageObj);
		mv.addObject("pay_status_list", OrderForm.PayStatus.values());
		mv.addObject("PayStatus", WebViewHelper.enumToMap(OrderForm.PayStatus.values()));
		return  mv;
	}
	
}
