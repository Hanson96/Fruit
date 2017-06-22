package com.hanson.view.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.hanson.core.constant.Globals;
import com.hanson.core.mv.JModelAndView;
import com.hanson.core.query.QueryObject;
import com.hanson.core.tools.CommUtil;
import com.hanson.core.tools.SimpleMap;
import com.hanson.core.tools.WebViewHelper;
import com.hanson.foundation.domain.AdvertisementPhoto;
import com.hanson.foundation.service.IAdvertisementPhotoService;
import com.hanson.security.domain.User;
import com.hanson.security.service.IUserService;
import com.hanson.security.shiro.ShiroUtils;

@Controller
public class LoginController {

	@Autowired
	private IUserService userService;
	@Autowired
	private IAdvertisementPhotoService advertisementPhotoService;
	
	@RequestMapping("/login")
	public ModelAndView login(HttpServletRequest request){
		JModelAndView mv = new JModelAndView("login.html", 0, request);
		QueryObject qo = new QueryObject();
		qo.addQuery("and obj.sequence>=0", null);
		qo.addQuery("obj.position", new SimpleMap("position", AdvertisementPhoto.Position.LOGIN.value()), "=");
		List<AdvertisementPhoto> advert_login_list = this.advertisementPhotoService.query(qo);
		if(advert_login_list.size()>0){
			int rand_num = new Random().nextInt(advert_login_list.size());
			mv.addObject("advert_photo", advert_login_list.get(rand_num));
		}
		mv.addObject("UserType", WebViewHelper.enumToMap(User.UserType.values()));
		return mv;
	}
	
	@ResponseBody
	@RequestMapping("/login_submit")
	public Map login_submit(HttpServletRequest request, HttpServletResponse response, String userName, String password,
			String verify_code, String user_type){
		Map data = new HashMap();
		boolean result = false;
		String error_msg = "";
		String verify_code_true = (String) WebUtils.getSessionAttribute(request, Globals.VERIFYCODE_SESSION_KEY);
		WebUtils.setSessionAttribute(request, Globals.VERIFYCODE_SESSION_KEY, null);
		User user_login = this.userService.getObjByProperty("userName", userName);
		if(StringUtils.isEmpty(verify_code) || !StringUtils.equals(verify_code_true, verify_code)){
			error_msg = "验证码错误";
		}else if(user_login!=null && CommUtil.null2Int(user_type) != user_login.getUser_type().intValue()){
			error_msg = "用户类型错误";
		}else{
			UsernamePasswordToken token = new UsernamePasswordToken(userName, DigestUtils.md5Hex(password));
			Subject subject = SecurityUtils.getSubject();
			try{
				subject.login(token);
				result = true;
				User user = ShiroUtils.getUser();
				WebUtils.setSessionAttribute(request, Globals.CURRENTUSER_SESSION_KEY, user);
				if(user.getUser_type() == User.UserType.ADMIN.value()){
					data.put("user_type", "admin");
				}else{
					data.put("user_type", "buyer");
				}
			}catch (UnknownAccountException e) {
				error_msg = "此用户名不存在";
			}catch(IncorrectCredentialsException e){
				error_msg = "密码错误";
			}catch(Exception e){
				error_msg = "发生错误";
			}
		}
		data.put("result", result);
		data.put("error_msg", error_msg);
		return data;
	}
	
	@ResponseBody
	@RequestMapping("/register_submit")
	public Map register_submit(HttpServletRequest request, String userName, String password, String verify_code){
		Map data = new HashMap();
		boolean result = false;
		String error_msg = "";
		String verify_code_true = (String) WebUtils.getSessionAttribute(request, Globals.VERIFYCODE_SESSION_KEY);
		WebUtils.setSessionAttribute(request, Globals.VERIFYCODE_SESSION_KEY, null);
		if(StringUtils.isEmpty(verify_code) || !StringUtils.equals(verify_code_true, verify_code)){
			error_msg = "验证码错误";
		}else if(this.userService.existSameName("userName", userName, null)){
			error_msg = "用户名已存在";
		}else{
			User user = new User();
			user.setUserName(userName);
			user.setUser_type(User.UserType.BUYER.value());
			user.setPassword(DigestUtils.md5Hex(password)); // 密码md5加密
			result = this.userService.save(user);
		}
		data.put("result", result);
		data.put("error_msg", error_msg);
		return data;
	}
	
	@RequestMapping("/init_admin")
	public void init_user(){
		if(!this.userService.existSameName("userName", "admin", null)){
			System.out.println("生成超级管理员");
			User admin = new User();
			admin.setUserName("admin");
			admin.setUser_type(User.UserType.ADMIN.value());
			admin.setPassword(DigestUtils.md5Hex("123456"));
			this.userService.save(admin);
		}
	}
	
}
