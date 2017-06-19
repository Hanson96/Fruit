package com.hanson.view.admin.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hanson.core.mv.JModelAndView;

@Controller
@RequestMapping("/admin")
public class IndexAdminController {

	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request){
		JModelAndView mv = new JModelAndView("index.html", 11, request);
		return  mv;
	}
}
