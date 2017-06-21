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
import com.hanson.foundation.domain.Accessory;
import com.hanson.foundation.domain.AdvertisementPhoto;
import com.hanson.foundation.domain.Group;
import com.hanson.foundation.service.IAccessoryService;
import com.hanson.foundation.service.IGroupService;

@Controller
@RequestMapping("/admin")
public class GroupAdminController {

	@Autowired
	private IGroupService groupService;
	@Autowired
	private IAccessoryService accessoryService;
	
	@RequestMapping("/group_list")
	public ModelAndView group_list(HttpServletRequest request, String currentPage, String pageRows){
		JModelAndView mv = new JModelAndView("group_list.html", 11, request);
		QueryHelper.queryParamsIntoModel(request, mv);
		QueryObject qo = new QueryObject(mv, currentPage, pageRows, null, null);
		String[] fields = {"name"};
		QueryHelper.addQueryBatch(Group.class, qo, request, fields, "like");
		IPageObject pageObj = this.groupService.list(qo);
		mv.addObject("pageObj", pageObj);
		return  mv;
	}
	
	@RequestMapping("/group_add")
	public ModelAndView group_add(HttpServletRequest request, String obj_id){
		JModelAndView mv = new JModelAndView("group_add.html", 11, request);
		if(StringUtils.isNotEmpty(obj_id)){
			Group obj = this.groupService.getObjById(Long.valueOf(obj_id));
			mv.addObject("obj", obj);
		}
		return  mv;
	}
	
	@RequestMapping("group_save")
	public ModelAndView group_save(HttpServletRequest request, String obj_id, Group group, 
			@RequestParam MultipartFile acc_file){
		JModelAndView mv = new JModelAndView("handle_msg.html", 11, request);
		boolean result = false;
		String error_msg = "";
		Group group_true = null;
		if(StringUtils.isNotEmpty(obj_id)){
			group_true = this.groupService.getObjById(Long.valueOf(obj_id));
			group_true = (Group) WebFormHelper.toPo(request, group, group_true, "obj", 0);
		}else{
			group_true = WebFormHelper.toPo(request, group, Group.class, "obj", 0);
		}
		Map uploadConfig = new HashMap();
		if(acc_file!=null){
			uploadConfig.put("multipartFile", acc_file);
			uploadConfig.put("uploadFolderPath", Globals.DEFAULT_UPLOAD_FOLDER + File.separator + "group");
			uploadConfig.put("originalAccessory", group_true.getAcc());
			Accessory acc = FileHelper.saveFileToAcc(request, uploadConfig);
			this.accessoryService.saveOrUpdate(acc);
			group_true.setAcc(acc);
		}
		result = this.groupService.saveOrUpdate(group_true);
		String msg_title = result ? "操作成功" : error_msg;
		String ctx = CommUtil.getContextPath(request);
		mv.addObject("result", result);
		mv.addObject("msg_title", msg_title);
		mv.addObject("error_msg", error_msg);
		mv.addObject("list_url", ctx + "/admin/group_list");
		mv.addObject("add_url", ctx + "/admin/group_add");
		if(group_true!=null){
			mv.addObject("edit_url", ctx + "/admin/group_add?obj_id=" + group_true.getId());
		}
		return  mv;
	}
	
	@ResponseBody
	@RequestMapping("/group_delete")
	public Map group_delete(HttpServletRequest request, String obj_id){
		Map data = new HashMap();
		boolean result = false;
		String error_msg = "";
		if(StringUtils.isNotEmpty(obj_id)){
			result = this.groupService.maintainDelete(Long.valueOf(obj_id));
		}else{
			error_msg = "参数有误";
		}
		data.put("result", result);
		data.put("error_msg", error_msg);
		return data;
	}
}
