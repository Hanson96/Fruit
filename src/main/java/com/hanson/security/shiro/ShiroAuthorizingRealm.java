package com.hanson.security.shiro;

import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hanson.security.domain.User;
import com.hanson.security.service.IUserService;

public class ShiroAuthorizingRealm extends AuthorizingRealm{

	private static final Logger log = LoggerFactory.getLogger(ShiroAuthorizingRealm.class);
	
	@Autowired
	private IUserService userService;
	
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	/**
	 * 访问资源路径时，根据当前认证实体（用户信息）获取授权信息（授权包括：角色、权限）—— 权限认证
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		ShiroPrincipal subject = (ShiroPrincipal)super.getAvailablePrincipal(principals);
		String username = subject.getUsername();
		Long userid = subject.getId();
		log.info("用户【" + username + "】授权开始......");
		try {
			if(!subject.isAuthorized()) {
				//根据用户名称，获取该用户所有的权限列表
				User user = this.userService.getObjById(userid);
				subject.setUser(user);
				List<String> rolelist = user.getRolesName();
				subject.setRoles(rolelist);
				subject.setAuthorized(true);
				log.info("用户【" + username + "】授权初始化成功......");
				log.info("用户【" + username + "】 角色列表为：" + subject.getRoles());
			} else {
				log.info("用户【" + username + "】已授权......");
			}
		} catch(RuntimeException e) {
			throw new AuthorizationException("用户【" + username + "】授权失败");
		}
		//给当前用户设置权限
		info.addRoles(subject.getRoles());
		return info;
	}
	
	/**
	 * 用户登录时，根据用户名、密码获取用户的认证信息      —— 身份认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken uptoken = (UsernamePasswordToken)token;
		String userName = uptoken.getUsername();
		String password = String.valueOf(uptoken.getPassword());
		User user = this.userService.getObjByProperty("userName", userName);
		if(user == null){
			log.info("用户名[{}]不存在",userName);
			throw new UnknownAccountException("用户不存在");
		}else if(!user.getPassword().equals(password)){
			log.info("用户名[{}]的密码错误",userName);
			throw new IncorrectCredentialsException("密码错误"); 
		}
		log.info("用户[{}]登录成功",userName);
		ShiroPrincipal subject = new ShiroPrincipal(user);
		subject.setRoles(user.getRolesName());
		subject.setAuthorized(true);
		return new SimpleAuthenticationInfo(subject, user.getPassword(), getName());
	}

}
