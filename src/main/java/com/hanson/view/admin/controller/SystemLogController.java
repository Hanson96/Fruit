package com.hanson.view.admin.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hanson.core.mv.JModelAndView;
import com.hanson.core.query.IPageObject;
import com.hanson.core.query.QueryObject;
import com.hanson.core.tools.CommUtil;
import com.hanson.core.tools.QueryHelper;
import com.hanson.core.tools.SimpleMap;
import com.hanson.foundation.domain.SystemLog;
import com.hanson.foundation.service.ISystemLogService;

@Controller
@RequestMapping("/admin")
public class SystemLogController {

	@Autowired
	private ISystemLogService systemLogService;
	
	@RequestMapping("/system_log_list")
	public ModelAndView system_log_list(HttpServletRequest request, String currentPage, String pageRows,
			String q_log_start_time, String q_log_end_time){
		JModelAndView mv = new JModelAndView("system_log_list.html", 11, request);
		QueryHelper.queryParamsIntoModel(request, mv); 
		QueryObject qo = new QueryObject(mv, currentPage, pageRows, null, null);
		String[] fields = {"user.userName","title"};
		QueryHelper.addQueryBatch(SystemLog.class, qo, request, fields, "like");
		// 发证时间
		if(StringUtils.isNotEmpty(q_log_start_time)){
			Date log_start_time = CommUtil.formatShortDateStr(q_log_start_time);
			qo.addQuery("obj.handle_time", new SimpleMap("log_start_time", log_start_time), ">=");
		}
		if(StringUtils.isNotEmpty(q_log_end_time)){
			Date log_end_time = CommUtil.formatShortDateStr(q_log_end_time);
			Calendar cal = Calendar.getInstance();
			cal.setTime(log_end_time); cal.add(Calendar.DATE, 1); // 延后一天 
			log_end_time = cal.getTime();
			qo.addQuery("obj.handle_time", new SimpleMap("log_end_time", log_end_time), "<=");
		}
		IPageObject pageObj = this.systemLogService.list(qo);
		mv.addObject("pageObj", pageObj);
		mv.addObject("LogType_list", SystemLog.LogType.values());
		return mv;
	}
	
	@ResponseBody
	@RequestMapping("/system_log_delete")
	public Map system_log_delete(HttpServletRequest request, String id){
		Map data = new HashMap();
		boolean result = false;
		String error_msg = "";
		if(StringUtils.isNotEmpty(id)){
			result = this.systemLogService.maintainDelete(Long.valueOf(id));
		}else{
			error_msg = "参数有误";
		}
		data.put("result", result);
		data.put("error_msg", error_msg);
		return data;
	}
}
