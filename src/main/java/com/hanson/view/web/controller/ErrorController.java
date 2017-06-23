package com.hanson.view.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hanson.core.exception.BusinessException;
import com.hanson.core.mv.JModelAndView;

@Controller
public class ErrorController {

private static final Logger log = LoggerFactory.getLogger(ErrorController.class);
	
	@RequestMapping("/404")
	public ModelAndView error_404(HttpServletRequest request, Exception e){
		JModelAndView mv = new JModelAndView("error.html", 0, request);
		mv.addObject("error_msg", e.getMessage());
		mv.addObject("error_type", "404");
		return mv;
	}
	
	@RequestMapping("/500")
	public ModelAndView error_500(HttpServletRequest request, Exception e){
		JModelAndView mv = new JModelAndView("error.html", 0, request);
		mv.addObject("error_msg", e.getMessage());
		mv.addObject("error_type", "500");
		return mv;
	}
	
	@RequestMapping("/unauthorized")
	public ModelAndView unauthorized(HttpServletRequest request){
		JModelAndView mv = new JModelAndView("error.html", 0, request);
		throw new BusinessException("您没有相应的操作权限");
		//mv.addObject("error_msg", "您没有相应的操作权限！");
		//return mv;
	}
}
