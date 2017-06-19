package com.hanson.view.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hanson.core.mv.JModelAndView;
import com.hanson.core.tools.WebViewHelper;
import com.hanson.security.domain.User;
import com.hanson.security.service.IUserService;

@Controller
public class LoginController {

	@Autowired
	private IUserService userService;
	
	@RequestMapping("/login")
	public ModelAndView login(HttpServletRequest request){
		JModelAndView mv = new JModelAndView("login.html", 0, request);
		mv.addObject("UserType", WebViewHelper.enumToMap(User.UserType.values()));
		return mv;
	}
	
	@ResponseBody
	@RequestMapping("/register_submit")
	public Map register_submit(String userName, String password){
		Map data = new HashMap();
		boolean result = false;
		String error_msg = "";
		if(this.userService.existSameName("userName", userName, null)){
			error_msg = "用户名已存在";
		}else{
			User user = new User();
			user.setUserName(userName);
			user.setUser_type(User.UserType.BUYER.value());
			user.setPassword(DigestUtils.md5Hex(password)); // 密码md5加密
			result = this.userService.save(user);
		}
		data.put("result", result);
		data.put("error_msg", error_msg);
		return data;
	}
	
	
}
