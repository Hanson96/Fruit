package com.hanson.foundation.log;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.hanson.core.annotation.Log;
import com.hanson.core.tools.CommUtil;
import com.hanson.core.tools.UserHelper;
import com.hanson.foundation.domain.SystemLog;
import com.hanson.foundation.domain.SystemLog.LogType;
import com.hanson.foundation.service.ISystemLogService;
import com.hanson.security.domain.User;
import com.hanson.security.shiro.ShiroUtils;

/**
 * 系统日志切面
 * @author hanson
 *
 */
@Aspect
@Component
public class SystemLogAdvice {

	@Autowired
	private ISystemLogService systemLogService;
	
	/**
	 * 控制器中跟用户相关的操作 记录日志
	 * @param joinPoint
	 * @param annotation
	 */
	@AfterReturning(pointcut="execution(* com.hanson..controller.*.*(..)) && !execution(* com.hanson.view.web.controller.LoginController.logout(..)) "
			+ "&& @annotation(annotation)",returning="returnValue")
	public void userLog(JoinPoint joinPoint, Log annotation, Object returnValue){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		User user = ShiroUtils.getUser();
		String userName = makeUserName(user);
		
		LogType type = annotation.type();
		String title = annotation.title();
		String entityName = annotation.entityName();
		String content = "";
		switch(type){
			case REGISTER:{
				userName = request.getParameter("userName");
				userName = "[买家-"+userName+"] ";
				content = userName + "注册账号。";
				if(returnValue instanceof Map){
					Map data = ((Map) returnValue);
					content += makeResultContent(data);
				}
				break;
			}
			case LOGIN:{
				content = userName + "登录系统";
				break;
			}
			case CHANGEPWS:{
				content = userName + "修改密码 。";
				if(returnValue instanceof ModelAndView){
					Map model = ((ModelAndView)returnValue).getModelMap();
					content += makeResultContent(model);
				}
				break;
			}
			case UPDATE:{
				content = userName + "更新了id为"+makeObjId(request)+"的"+entityName+"。";
				if(returnValue instanceof ModelAndView){
					Map model = ((ModelAndView)returnValue).getModelMap();
					content += makeResultContent(model);
				}else if(returnValue instanceof Map){
					Map data = ((Map) returnValue);
					content += makeResultContent(data);
				}
			}
			case SAVE:{
				String obj_id = request.getParameter("id")!=null ? request.getParameter("id"):request.getParameter("obj_id");
				content = userName;
				if(StringUtils.isEmpty(obj_id)){
					content += "新增并保存了"+entityName+"。";
				}else{
					content += "修改并保存了id为"+obj_id+"的"+entityName+"。";
				}
				if(returnValue instanceof ModelAndView){
					Map model = ((ModelAndView)returnValue).getModelMap();
					content += makeResultContent(model);
				}
				break;
			}
			case DELETE:{
				content = userName + "删除了id为"+makeObjId(request)+"的"+entityName+"。";
				if(returnValue instanceof Map){
					Map data = ((Map) returnValue);
					content += makeResultContent(data);
				}
				break;
			}
			// 特殊类型的日志，日志内容具体的实现方法提供
			case SPECIAL:{
				content = userName;
				if(returnValue instanceof ModelAndView){
					Map model = ((ModelAndView)returnValue).getModelMap();
					content += (String)model.get("log_content"); // 获取方法返回值中提供的日志内容
					content += makeResultContent(model); 
					model.remove("log_content"); // 将返回值中的日志内容去掉
				}else if(returnValue instanceof Map){
					Map data = ((Map) returnValue);
					content += (String)data.get("log_content");
					content += makeResultContent(data);
					data.remove("log_content"); // 将返回值中的日志内容去掉
				}
			}
		}
		SystemLog sysLog = new SystemLog();
		sysLog.setUser(user);
		sysLog.setTitle(title);
		sysLog.setContent(content);
		sysLog.setIp(CommUtil.getIpAddr(request));
		sysLog.setType(type.value());
		System.out.println("日志内容:"+content);
		this.systemLogService.save(sysLog);
	}
	
	/**
	 * 用户退出系统切面记录
	 * @param joinPoint
	 * @param annotation
	 */
	@Before("execution(* com.hanson.view.web.controller.LoginController.logout(..))&& @annotation(annotation)")
	public void logoutLog(JoinPoint joinPoint, Log annotation){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		User user = ShiroUtils.getUser();
		String userName = makeUserName(user);
		LogType type = annotation.type();
		String title = annotation.title();
		String entityName = annotation.entityName();
		String content = userName + "退出系统";
		
		SystemLog sysLog = new SystemLog();
		sysLog.setUser(user);
		sysLog.setTitle(title);
		sysLog.setContent(content);
		sysLog.setIp(CommUtil.getIpAddr(request));
		sysLog.setType(type.value());
		this.systemLogService.save(sysLog);
	}
	
	private String makeObjId(HttpServletRequest request){
		String obj_id = request.getParameter("id")!=null ? request.getParameter("id"):request.getParameter("obj_id");
		if(StringUtils.isNotEmpty(obj_id)){
			return obj_id;
		}
		return " ";
	}
	
	/**
	 * 构造用户名为  [用户类型-用户名]
	 * @param user
	 * @return
	 */
	private static String makeUserName(User user){
		String userName = " ";
		int userType;
		try{
			userType = user.getUser_type().intValue();
		}catch(Exception e){
			return " ";
		}
		if(userType == User.UserType.BUYER.value()){
			userName = "[买家-"+user.getUserName()+"] ";
		}else if(userType == User.UserType.ADMIN.value()){
			userName = "[管理员-"+user.getUserName()+"] ";
		}
		return userName;
	}
	
	/**
	 * 生成操作的结果
	 * @param data
	 * @return
	 */
	private String makeResultContent(Map data){
		String result_content = "";
		boolean result = (boolean)data.get("result");
		if(result){
			result_content += "操作成功";
		}else{
			String error_msg = data.get("error_msg")!=null ? (String)data.get("error_msg") : (String)data.get("msg_title");
			result_content += "操作失败，失败原因："+error_msg;
		}
		return result_content;
	}

}
