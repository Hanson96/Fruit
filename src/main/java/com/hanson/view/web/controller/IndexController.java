package com.hanson.view.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hanson.core.mv.JModelAndView;
import com.hanson.foundation.domain.AdvertisementPhoto;
import com.hanson.foundation.service.IAdvertisementPhotoService;

@Controller
public class IndexController {

	@Autowired
	private IAdvertisementPhotoService advertisementPhotoService;
	
	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request){
		JModelAndView mv = new JModelAndView("index.html", 0, request);
		String queryStr = "select obj from AdvertisementPhoto obj where obj.sequence>=0 order by obj.sequence asc";
		List<AdvertisementPhoto> index_slide_list = this.advertisementPhotoService.query(queryStr, null, -1, -1);
		mv.addObject("index_slide_list", index_slide_list);
		return mv;
	}
	
	@RequestMapping("/top")
	public ModelAndView top(HttpServletRequest request){
		JModelAndView mv = new JModelAndView("top.html", 0, request);
		
		return mv;
	}
	
	@RequestMapping("/header")
	public ModelAndView header(HttpServletRequest request){
		JModelAndView mv = new JModelAndView("header.html", 0, request);
		
		return mv;
	}
}
