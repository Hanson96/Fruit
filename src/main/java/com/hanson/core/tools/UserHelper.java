package com.hanson.core.tools;

import com.hanson.core.exception.BusinessException;
import com.hanson.security.domain.User;

/**
 * 跟用户相关的工具   主要是一些权限的认证
 * @author hanson
 *
 */
public class UserHelper {

	/**
	 * 检查用户是否登录
	 * @param user
	 * @return
	 */
	public static boolean checkUserLogin(User user){
		if(user==null){
			BusinessException ex = new BusinessException("用户未登录");
			ex.setError_code(BusinessException.Code.NOT_LOGIN.ordinal());
			ex.setLogin_auto_go(false);
			throw ex;
		}
		return true;
	}
	
	/**
	 * 检查用户身份是否相符
	 * @param user
	 * @param user_tpye
	 * @return
	 */
	public static boolean checkUserType(User user, int user_type){
		if(checkUserLogin(user)){
			if(user.getUser_type().intValue() != user_type){
				throw new BusinessException("您的用户身份不符");
			}
		}
		return true;
	}
	
	/**
	 * 检查用户的身份是否相符   和  检查用户操作的对象是否属于这个用户
	 * @param user
	 * @param user_type
	 * @param belong_uid 此对象所属于的用户id
	 * @return
	 */
	public static boolean checkUserTypeAndUserId(User user, int user_type, Long belong_uid){
		if(checkUserType(user, user_type)){
			if(!user.getId().equals(belong_uid)){
				throw new BusinessException("您的不具有此操作权限");
			}
		}
		return true;
	}
	
	/**
	 * 检查用户的身份是否相符    并且   比较操作对象所属的具有操作权限的顶层对象是否一致
	 * 比如 此操作需要具有A对象的身份，而用户只具有B对象的身份，A,B的id不一致所以没有权限
	 * @param user
	 * @param user_type
	 * @param belong_id  操作对象所属的具有权限的顶层对象id
	 * @param now_id     用户所具有权限的顶层对象的id
	 * @return
	 */
	public static boolean checkUserTypeAndObjId(User user, int user_type, Long belong_id, Long now_id){
		if(checkUserType(user, user_type)){
			if(!belong_id.equals(now_id)){
				throw new BusinessException("您的不具有此操作权限");
			}
		}
		return true;
	}
	
}
