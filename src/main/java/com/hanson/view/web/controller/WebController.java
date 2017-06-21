package com.hanson.view.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hanson.core.mv.JModelAndView;
import com.hanson.core.tools.CommUtil;
import com.hanson.security.shiro.ShiroUtils;

@Controller
public class WebController {

	@RequestMapping("/html/{fileName}")
	public ModelAndView fileName(@PathVariable String fileName, HttpServletRequest request){
		JModelAndView mv = new JModelAndView(fileName+".html", 0, request);
		CommUtil util = CommUtil.getInstance();
		mv.addObject("util", util);
		mv.addObject("req", request);
		return mv;
	}
	
	@ResponseBody
	@RequestMapping("/checkUserLogin")
	public Map checkUserLogin(){
		Map data = new HashMap();
		boolean login = false;
		String error_msg = "";
		if(ShiroUtils.getUser()==null){
			error_msg = "用户未登录";
		}else{
			login = true;
		}
		data.put("login", login);
		data.put("error_msg", error_msg);
		return data;
	}
	
}
