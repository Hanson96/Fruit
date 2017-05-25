package com.hanson.core.exception;
/**
 * 业务异常类    用户业务操作错误则会抛出此异常
 * @author hanson
 *
 */
public class BusinessException extends RuntimeException{

	private static final long serialVersionUID = 4103212582387658649L;

    private int error_code = -1; //异常代码   默认-1，即没有异常  
    
    public enum Code{
    	NOT_LOGIN, //未登录  
    }
      
    private Object[] data;//一些其他信息  
    
    private String original_url; // 原来访问的网址
    
    private String redirect_url; // 重定向地址
    
    private boolean redirect_now = false;
    
    private boolean login_auto_go = true; // 如果是未登录引起的异常，登录成功后自动跳转回原来请求的页面
      
    public BusinessException() {  
        super();  
    }  
  
    public BusinessException(String message, Throwable throwable) {  
        super(message, throwable);  
    }  
  
    public BusinessException(String message) {  
        super(message);  
    }  
  
    public BusinessException(Throwable throwable) {  
        super(throwable);  
    }

	public int getError_code() {
		return error_code;
	}

	public void setError_code(int error_code) {
		this.error_code = error_code;
	}

	public Object[] getData() {
		return data;
	}

	public void setData(Object[] data) {
		this.data = data;
	}

	public String getOriginal_url() {
		return original_url;
	}

	public void setOriginal_url(String original_url) {
		this.original_url = original_url;
	}

	public String getRedirect_url() {
		return redirect_url;
	}

	public void setRedirect_url(String redirect_url) {
		this.redirect_url = redirect_url;
	}

	public boolean getRedirect_now() {
		return redirect_now;
	}

	public void setRedirect_now(boolean redirect_now) {
		this.redirect_now = redirect_now;
	}

	public boolean getLogin_auto_go() {
		return login_auto_go;
	}

	public void setLogin_auto_go(boolean login_auto_go) {
		this.login_auto_go = login_auto_go;
	}  
    
    
    
}
