package com.hanson.core.constant;

import java.io.File;

/**
 * 全局静态常量
 * @author hanson
 *
 */
public class Globals {
	
	/** 验证码session字段*/
	public static final String CURRENTUSER_SESSION_KEY = "currentUser";
	/** 验证码session字段*/
	public static final String VERIFYCODE_SESSION_KEY = "verifycode_session_key";
	/** 短信验证码session字段 */
	public static final String SMSCODE_SESSION_KEY = "smscode_session_key";
	/** 刚激活账号的状态session字段 */
	public static final String ACTIVATE_SESSION_KEY = "activate_session_key";
	/** 文件默认的上传目录 */
	public static final String DEFAULT_UPLOAD_FOLDER = "upload";
	/** 校验用户的状态是否正确 */
	public static final String SESSION_UUID = "session_uuid";
}
