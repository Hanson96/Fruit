package com.hanson.view.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hanson.core.mv.JModelAndView;
import com.hanson.foundation.tools.CartHelper;
import com.hanson.security.domain.User;
import com.hanson.security.service.IUserService;
import com.hanson.security.shiro.ShiroUtils;

@Controller
public class TemplateController {

	@Autowired
	private IUserService userService;
	
	@RequestMapping("/shopping_cart")
	public ModelAndView shopping_cart(HttpServletRequest request){
		JModelAndView mv = new JModelAndView("shopping_cart.html", 20, request);
		User user = this.userService.getObjById(ShiroUtils.getUserId());
		if(user!=null && user.getCart_list().size()>0){
			mv.addObject("user_cart", user.getCart_list().get(0));
			mv.addObject("CartHelper", CartHelper.getInstance());
		}
		return mv;
	}
	
}
