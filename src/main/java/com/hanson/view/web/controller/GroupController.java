package com.hanson.view.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hanson.core.mv.JModelAndView;
import com.hanson.core.query.IPageObject;
import com.hanson.core.query.QueryObject;
import com.hanson.core.tools.QueryHelper;
import com.hanson.core.tools.WebViewHelper;
import com.hanson.foundation.domain.Group;
import com.hanson.foundation.service.IGroupService;

@Controller
public class GroupController {
	
	@Autowired
	private IGroupService groupService; 

	@RequestMapping("/group")
	public ModelAndView group(HttpServletRequest request, String currentPage, String pageRows){
		JModelAndView mv = new JModelAndView("group.html", 0, request);
		Map queryMap = QueryHelper.queryParamsIntoModel(request, mv);
		QueryObject qo = new QueryObject(mv, currentPage, pageRows, null, null);
		String[] fields = {"name"};
		QueryHelper.addQueryBatch(Group.class, qo, request, fields, "like");
		IPageObject pageObj = this.groupService.list(qo);
		mv.addObject("pageObj", pageObj);
		mv.addObject("Status", WebViewHelper.enumToMap(Group.Status.values()));
		return mv;
	}
	
}
