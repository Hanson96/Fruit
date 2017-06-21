package com.hanson.view.buyer.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hanson.core.mv.JModelAndView;

@Controller
public class BuyBuyerController {

	
	@RequestMapping("buy_now")
	public ModelAndView buy_now(HttpServletRequest request){
		JModelAndView mv = new JModelAndView("buy.html", 12, request);
		
		return mv;
	}
}
