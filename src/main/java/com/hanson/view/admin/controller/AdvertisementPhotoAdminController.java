package com.hanson.view.admin.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.hanson.core.constant.Globals;
import com.hanson.core.mv.JModelAndView;
import com.hanson.core.query.IPageObject;
import com.hanson.core.query.QueryObject;
import com.hanson.core.tools.CommUtil;
import com.hanson.core.tools.FileHelper;
import com.hanson.core.tools.QueryHelper;
import com.hanson.core.tools.WebFormHelper;
import com.hanson.core.tools.WebViewHelper;
import com.hanson.foundation.domain.Accessory;
import com.hanson.foundation.domain.AdvertisementPhoto;
import com.hanson.foundation.domain.Goods;
import com.hanson.foundation.domain.GoodsClass;
import com.hanson.foundation.service.IAccessoryService;
import com.hanson.foundation.service.IAdvertisementPhotoService;

@Controller
@RequestMapping("/admin")
public class AdvertisementPhotoAdminController {

	@Autowired
	private IAdvertisementPhotoService advertisementPhotoService;
	@Autowired
	private IAccessoryService accessoryService;
	
	@RequestMapping("/advertisement_photo_list")
	public ModelAndView advertisement_photo_list(HttpServletRequest request, String currentPage, String pageRows,
			String q_show, String q_orderType){
		JModelAndView mv = new JModelAndView("advertisement_photo_list.html", 11, request);
		QueryHelper.queryParamsIntoModel(request, mv);
		QueryObject qo = new QueryObject(mv, currentPage, pageRows, null, null);
		String[] fields = {"name"};
		QueryHelper.addQueryBatch(AdvertisementPhoto.class, qo, request, fields, "like");
		String[] fields1 = {"position"};
		QueryHelper.addQueryBatch(AdvertisementPhoto.class, qo, request, fields1, "=");
		if(StringUtils.equals(q_show, "0")){
			qo.addQuery("and obj.sequence < 0", null);
		}
		if(StringUtils.equals(q_show, "1")){
			qo.addQuery("and obj.sequence >= 0", null);
		}
		if(StringUtils.equals(q_orderType, "asc")){
			qo.setOrderBy("sequence");
			qo.setOrderType("asc");
		}
		if(StringUtils.equals(q_orderType, "desc")){
			qo.setOrderBy("sequence");
			qo.setOrderType("desc");
		}
		IPageObject pageObj = this.advertisementPhotoService.list(qo);
		mv.addObject("pageObj", pageObj);
		mv.addObject("position_list", AdvertisementPhoto.Position.values());
		return  mv;
	}
	
	@RequestMapping("/advertisement_photo_add")
	public ModelAndView advertisement_photo_add(HttpServletRequest request, String obj_id){
		JModelAndView mv = new JModelAndView("advertisement_photo_add.html", 11, request);
		if(StringUtils.isNotEmpty(obj_id)){
			AdvertisementPhoto obj = this.advertisementPhotoService.getObjById(Long.valueOf(obj_id));
			mv.addObject("obj", obj);
		}
		mv.addObject("position_list", AdvertisementPhoto.Position.values());
		return  mv;
	}
	
	@RequestMapping("advertisement_photo_save")
	public ModelAndView advertisement_photo_save(HttpServletRequest request, String obj_id, AdvertisementPhoto advert, 
			@RequestParam MultipartFile acc_file){
		JModelAndView mv = new JModelAndView("handle_msg.html", 11, request);
		boolean result = false;
		String error_msg = "";
		AdvertisementPhoto advert_true = null;
		if(StringUtils.isNotEmpty(obj_id)){
			advert_true = this.advertisementPhotoService.getObjById(Long.valueOf(obj_id));
			advert_true = (AdvertisementPhoto) WebFormHelper.toPo(request, advert, advert_true, "obj", 0);
		}else{
			advert_true = WebFormHelper.toPo(request, advert, AdvertisementPhoto.class, "obj", 0);
		}
		Map uploadConfig = new HashMap();
		if(acc_file!=null){
			uploadConfig.put("multipartFile", acc_file);
			uploadConfig.put("uploadFolderPath", Globals.DEFAULT_UPLOAD_FOLDER + File.separator + "advert");
			uploadConfig.put("originalAccessory", advert_true.getAcc());
			Accessory acc = FileHelper.saveFileToAcc(request, uploadConfig);
			this.accessoryService.saveOrUpdate(acc);
			advert_true.setAcc(acc);
		}
		result = this.advertisementPhotoService.saveOrUpdate(advert_true);
		String msg_title = result ? "操作成功" : error_msg;
		String ctx = CommUtil.getContextPath(request);
		mv.addObject("result", result);
		mv.addObject("msg_title", msg_title);
		mv.addObject("error_msg", error_msg);
		mv.addObject("list_url", ctx + "/admin/advertisement_photo_list");
		mv.addObject("add_url", ctx + "/admin/advertisement_photo_add");
		if(advert_true!=null){
			mv.addObject("edit_url", ctx + "/admin/advertisement_photo_add?obj_id=" + advert_true.getId());
		}
		return  mv;
	}
	
	@ResponseBody
	@RequestMapping("/advertisement_photo_delete")
	public Map advertisement_photo_delete(HttpServletRequest request, String obj_id){
		Map data = new HashMap();
		boolean result = false;
		String error_msg = "";
		if(StringUtils.isNotEmpty(obj_id)){
			result = this.advertisementPhotoService.maintainDelete(Long.valueOf(obj_id));
		}else{
			error_msg = "参数有误";
		}
		data.put("result", result);
		data.put("error_msg", error_msg);
		return data;
	}
	
}
