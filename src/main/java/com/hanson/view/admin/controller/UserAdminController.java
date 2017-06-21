package com.hanson.view.admin.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.hanson.core.mv.JModelAndView;
import com.hanson.core.query.IPageObject;
import com.hanson.core.query.QueryObject;
import com.hanson.core.tools.CommUtil;
import com.hanson.core.tools.QueryHelper;
import com.hanson.core.tools.WebFormHelper;
import com.hanson.foundation.domain.AdvertisementPhoto;
import com.hanson.security.domain.User;
import com.hanson.security.service.IUserService;

@Controller
@RequestMapping("/admin")
public class UserAdminController {

	@Autowired
	private IUserService userService;
	
	@RequestMapping("/user_list")
	public ModelAndView advertisement_photo_list(HttpServletRequest request, String currentPage, String pageRows){
		JModelAndView mv = new JModelAndView("user_list.html", 11, request);
		QueryHelper.queryParamsIntoModel(request, mv);
		QueryObject qo = new QueryObject(mv, currentPage, pageRows, null, null);
		String[] fields = {"userName"};
		QueryHelper.addQueryBatch(User.class, qo, request, fields, "like");
		String[] fields1 = {"user_type"};
		QueryHelper.addQueryBatch(User.class, qo, request, fields1, "=");
		IPageObject pageObj = this.userService.list(qo);
		mv.addObject("pageObj", pageObj);
		mv.addObject("user_type_list", User.UserType.values());
		return  mv;
	}
	
	@RequestMapping("/user_add")
	public ModelAndView advertisement_photo_add(HttpServletRequest request, String obj_id){
		JModelAndView mv = new JModelAndView("user_add.html", 11, request);
		if(StringUtils.isNotEmpty(obj_id)){
			User obj = this.userService.getObjById(Long.valueOf(obj_id));
			mv.addObject("obj", obj);
		}
		mv.addObject("user_type_list", User.UserType.values());
		return  mv;
	}
	
	@RequestMapping("user_save")
	public ModelAndView user_save(HttpServletRequest request, String obj_id, User user){
		JModelAndView mv = new JModelAndView("handle_msg.html", 11, request);
		boolean result = false;
		String error_msg = "";
		User user_true = null;
		if(StringUtils.isNotEmpty(obj_id)){
			user_true = this.userService.getObjById(Long.valueOf(obj_id));
			user_true = (User) WebFormHelper.toPo(request, user, user_true, "obj", 0);
		}else{
			user_true = WebFormHelper.toPo(request, user, User.class, "obj", 0);
		}
		result = this.userService.saveOrUpdate(user_true);
		String msg_title = result ? "操作成功" : error_msg;
		String ctx = CommUtil.getContextPath(request);
		mv.addObject("result", result);
		mv.addObject("msg_title", msg_title);
		mv.addObject("error_msg", error_msg);
		mv.addObject("list_url", ctx + "/admin/user_list");
		mv.addObject("add_url", ctx + "/admin/user_add");
		if(user_true!=null){
			mv.addObject("edit_url", ctx + "/admin/user_add?obj_id=" + user_true.getId());
		}
		return mv;
	}
	
	@ResponseBody
	@RequestMapping("/user_delete")
	public Map user_delete(HttpServletRequest request, String obj_id){
		Map data = new HashMap();
		boolean result = false;
		String error_msg = "";
		if(StringUtils.isNotEmpty(obj_id)){
			result = this.userService.maintainDelete(Long.valueOf(obj_id));
		}else{
			error_msg = "参数有误";
		}
		data.put("result", result);
		data.put("error_msg", error_msg);
		return data;
	}
}
