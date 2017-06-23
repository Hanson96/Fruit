package com.hanson.view.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hanson.core.mv.JModelAndView;
import com.hanson.core.query.IPageObject;
import com.hanson.core.query.QueryObject;
import com.hanson.core.tools.QueryHelper;
import com.hanson.foundation.domain.Goods;
import com.hanson.foundation.service.IGoodsClassService;
import com.hanson.foundation.service.IGoodsService;
import com.hanson.security.domain.User;
import com.hanson.security.service.IUserService;
import com.hanson.security.shiro.ShiroUtils;

@Controller
public class SearchController {

	@Autowired
	private IGoodsService goodsService;
	@Autowired
	private IGoodsClassService goodsClassService;
	@Autowired
	private IUserService userService;
	
	@RequestMapping("/search")
	public ModelAndView search(HttpServletRequest request, String currentPage, String pageRows){
		JModelAndView mv = new JModelAndView("search.html", 0, request);
		QueryHelper.queryParamsIntoModel(request, mv);
		QueryObject qo = new QueryObject(mv, currentPage, pageRows, null, null);
		String[] fields = {"name"};
		QueryHelper.addQueryBatch(Goods.class, qo, request, fields, "like");
		IPageObject pageObj = this.goodsService.list(qo);
		mv.addObject("pageObj", pageObj);
		User user = this.userService.getObjById(ShiroUtils.getUserId());
		mv.addObject("goods_class_list", this.goodsClassService.listAll());
		return mv;
	}
	
}
