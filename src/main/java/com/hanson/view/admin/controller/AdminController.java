package com.hanson.view.admin.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hanson.core.mv.JModelAndView;

@Controller
@RequestMapping("/user/admin")
public class AdminController {

	@RequestMapping("/jsp/{fileName}")
	public ModelAndView fileName(@PathVariable String fileName, HttpServletRequest request){
		JModelAndView mv = new JModelAndView(fileName+".html", 11, request);
		return mv;
	}
}
