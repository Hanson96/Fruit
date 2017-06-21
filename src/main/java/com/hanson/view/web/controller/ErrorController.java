package com.hanson.view.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController {

	// 没有权限的用户
	@RequestMapping("/unauthorized")
	public void unauthorized(){
		
	}
}
