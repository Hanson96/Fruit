package com.hanson.view.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hanson.core.mv.JModelAndView;
import com.hanson.foundation.domain.Goods;
import com.hanson.foundation.service.IGoodsService;

@Controller
public class GoodsController {

	@Autowired
	private IGoodsService goodsService;
	
	@RequestMapping("/goods_detail")
	public ModelAndView goods_detail(HttpServletRequest request, String obj_id){
		JModelAndView mv = new JModelAndView("goods_detail.html", 0, request);
		if(StringUtils.isNotEmpty(obj_id)){
			Goods obj = this.goodsService.getObjById(Long.valueOf(obj_id));
			mv.addObject("obj", obj);
		}
		return mv;
	}
	
}
