package com.hanson.view.web.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.WebUtils;

import com.google.code.kaptcha.Producer;
import com.hanson.core.constant.Globals;
import com.hanson.core.tools.CommUtil;
import com.hanson.security.service.IUserService;

/**
 * 公共的表单验证请求
 * @author hanson
 *
 */
@Controller
public class ValidateController {

	@Autowired
	private IUserService userService;
	
	@Autowired  
    private Producer captchaProducer;  
	
	/**
	 * 获取验证码
	 * @throws IOException 
	 */
	@RequestMapping("/get_verify_code")
	public void get_verify_code(HttpServletRequest request, HttpServletResponse response) throws IOException{
        response.setDateHeader("Expires", 0);  
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");  
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");  
        response.setHeader("Pragma", "no-cache");  
        response.setContentType("image/jpeg");  
        
        String capText = captchaProducer.createText();  
        WebUtils.setSessionAttribute(request, Globals.VERIFYCODE_SESSION_KEY, capText);
        System.out.println("验证码: " + capText ); 
        BufferedImage bi = captchaProducer.createImage(capText);  
        ServletOutputStream out = response.getOutputStream();  
        ImageIO.write(bi, "jpg", out);  
        try {  
            out.flush();  
        } finally {  
            out.close();  
        }  
	}
	
	/**
	 * 登录或注册账号之前先进行ajax验证码验证
	 * @param request
	 * @param response
	 * @param verify_code 验证码
	 * @param refresh 验证不正确时是否清除验证码
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping("/validate_verify_code")
	public boolean validate_verify_code(HttpServletRequest request, String verify_code, String refresh) throws IOException{
		String code_true = (String)WebUtils.getSessionAttribute(request, Globals.VERIFYCODE_SESSION_KEY);
		boolean result = false;
		if(CommUtil.isNotNull(verify_code) && CommUtil.isNotNull(code_true) &&code_true.equals(verify_code)){
			result = true;
		}
		if(!result && !StringUtils.equals(refresh, "false")){ // 验证码错误并且未请求不刷新验证码时则清除验证码      如果正确  则在之后后再清除
			WebUtils.setSessionAttribute(request, Globals.VERIFYCODE_SESSION_KEY, null);
		}
		return result;
	}
	
	/**
	 * 验证用户名是否已经存在
	 * @param request
	 * @param obj_id  用户的id  不存在用户则为空
	 * @param userName
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/validate_userName_same")
	public boolean validate_userName_same(HttpServletRequest request, String obj_id, String userName){
		Long id = StringUtils.isNotEmpty(obj_id) ? Long.valueOf(obj_id) : null;
		boolean result = this.userService.existSameName("userName", userName, id);
		return !result;
	}
	
}
