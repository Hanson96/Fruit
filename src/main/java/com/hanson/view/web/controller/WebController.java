package com.hanson.view.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hanson.core.mv.JModelAndView;
import com.hanson.core.tools.CommUtil;

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
	
	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request){
		JModelAndView mv = new JModelAndView("index.html", 0, request);
		return mv;
	}
}
