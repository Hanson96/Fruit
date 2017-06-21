package com.hanson.core.tools;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

/**
 * 公共工具类
 * @author:hanson
 */
public class CommUtil {

	private static final Logger log = LoggerFactory.getLogger(CommUtil.class);
	
	private static CommUtil instance = new CommUtil();
	/**默认格式化日期*/
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	private CommUtil(){}
	
	/**
	 * 单例模式
	 * @return
	 */
	public static CommUtil getInstance(){
		return instance;
	}
	
	/**
    * 判断对象是否为null或者空字符串
    * @author:hanson
    */
	public static boolean isNotNull(Object obj)
	{
	   return (obj != null) && (!obj.toString().equals(""));
	}
	
	public static boolean isNull(Object obj)
	{
	   return (obj == null) || (obj.toString().equals(""));
	}
	
	/**
	 * 转整型
	 * @param obj
	 * @author:hanson
	 */
	public static int null2Int(Object obj){
		int value = 0;
		try{
			value = Integer.parseInt(obj.toString());
		}catch(Exception e){
			log.info("整型转换错误");
		}
	    return value;
	}
	
	/**
	 * 
	 * @Description: 将obj转为Float,默认为0.0f
	 * @param s
	 * @return
	 */
	public static float null2Float(Object s) {
		float v = 0.0F;
		if (s != null)
			try {
				v = Float.parseFloat(s.toString());
			} catch (Exception localException) {
			}
		return v;
	}

	/**
	 * 
	 * @Description: 将obj转为double,默认为0.0d
	 * @param s
	 * @return
	 */
	public static double null2Double(Object s) {
		double v = 0.0D;
		if (s != null)
			try {
				v = Double.parseDouble(null2String(s));
			} catch (Exception localException) {
			}
		return v;
	}

	/**
	 * 
	 * @Description: 将obj转为boolean,默认为false
	 * @param s
	 * @return
	 */
	public static boolean null2Boolean(Object s) {
		boolean v = false;
		if (s != null)
			try {
				v = Boolean.parseBoolean(s.toString());
			} catch (Exception localException) {
			}
		return v;
	}

	/**
	 * 
	 * @Description: 将obj转为String,默认为""
	 * @param s
	 * @return
	 */
	public static String null2String(Object s) {
		return s == null ? "" : s.toString().trim();
	}
	
	/**
	 * 
	 * @Description: 将obj转为Long,默认为new Long(-1L);
	 * @param s
	 * @return
	 */
	public static Long null2Long(Object s) {
		Long v = Long.valueOf(-1L);
		if (s != null)
			try {
				v = Long.valueOf(Long.parseLong(s.toString()));
			} catch (Exception localException) {
			}
		return v;
	}
	
	/**
	 * 判断字符串数组中是否包含有某个字符串
	 * @param item
	 * @param array
	 * @return
	 */
	public static boolean isContainStr(String[] array ,String item){
		if(isNull(item) || isNull(array)){
			return false;
		}else{
			for(String str : array){
				if(str.equals(item)){
					return true; 
				}
			}
		}
		return false;
	}
	
	/**
	 * 获取字符串中某个字符出现的个数
	 * @param str
	 * @param c
	 * @return
	 */
	public static int getCharCountInStr(String str, char c){
		int count = 0;
		for(char ch : str.toCharArray()){
			if(ch == c) count++;
		}
		return count;
	}
	
	/**
	 * 根据日期字符串  转化为Date日期格式    yyyy-MM-dd
	 * @param dateStr
	 * @return 
	 */
	public static Date formatShortDateStr(String dateStr){
		dateStr = correctTimeStr(dateStr);
		Date date = null;
		try {
			date = dateFormat.parse(dateStr);
		} catch (ParseException e) {
			log.info("日期解析错误");
		}
		return date;
	}
	/**
	 * 根据日期字符串  转化为Date日期格式    yyyy-MM-dd HH:mm:ss
	 * @param dateStr
	 * @return
	 */
	public static Date formatLongDateStr(String dateStr){
		dateStr = correctTimeStr(dateStr);
		Date date = null;
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			date = df.parse(dateStr);
		} catch (ParseException e) {
			log.info("日期解析错误");
		}
		return date;
	}
	/**
	 * 纠正不标准的日期格式
	 * @param timeStr
	 * @return
	 */
	private static String correctTimeStr(String timeStr){
		if(timeStr == null) return null;
		timeStr = timeStr.trim().replaceAll("\\s{1,}", " "); // 去除多余的空格
		timeStr = timeStr.replaceAll("[./。、]", "-").replaceAll("：", ":"); // 转成标准格式 
		return timeStr;
	}
	/**
	 * 格式化日期为指定格式的字符串，"yyyy-MM-dd HH:mm:ss"
	 * @return 
	 */
	public static String formatLongDate(Object v) {
		if ((v == null) || (v.equals("")))
			return "";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(v);
	}

	/**
	 * 格式化日期为指定格式的字符串，"yyyy-MM-dd"
	 * @return
	 */
	public static String formatShortDate(Object v) {
		if (v == null) return null;
		return dateFormat.format(v);
	}
	
	/**
	 * @Description: 两个浮点数相除，格式化结果保留两位小数
	 * @param a
	 * @param b
	 * @return
	 */
	public static double div(Object a, Object b) {
		double ret = 0.0D;
		if ((!null2String(a).equals("")) && (!null2String(b).equals(""))) {
			BigDecimal e = new BigDecimal(null2String(a));
			BigDecimal f = new BigDecimal(null2String(b));
			if (null2Double(f) > 0.0D)
				ret = e.divide(f, 3, 1).doubleValue();
		}
		DecimalFormat df = new DecimalFormat("0.00");
		return Double.valueOf(df.format(ret)).doubleValue();
	}

	/**
	 * @Description: 两个浮点数相减，格式化结果保留两位小数
	 * @param a
	 * @param b
	 * @return
	 */
	public static double subtract(Object a, Object b) {
		double ret = 0.0D;
		BigDecimal e = new BigDecimal(null2Double(a));
		BigDecimal f = new BigDecimal(null2Double(b));
		ret = e.subtract(f).doubleValue();
		DecimalFormat df = new DecimalFormat("0.00");
		return Double.valueOf(df.format(ret)).doubleValue();
	}

	/**
	 * @Description: 两个浮点数相加，格式化结果保留两位小数
	 * @param a
	 * @param b
	 * @return
	 */
	public static double add(Object a, Object b) {
		double ret = 0.0D;
		BigDecimal e = new BigDecimal(null2Double(a));
		BigDecimal f = new BigDecimal(null2Double(b));
		ret = e.add(f).doubleValue();
		DecimalFormat df = new DecimalFormat("0.00");
		return Double.valueOf(df.format(ret)).doubleValue();
	}

	/**
	 * @Description: 两个浮点数相乘，格式化结果保留两位小数
	 * @param a
	 * @param b
	 * @return
	 */
	public static double mul(Object a, Object b) {
		BigDecimal e = new BigDecimal(null2Double(a));
		BigDecimal f = new BigDecimal(null2Double(b));
		double ret = e.multiply(f).doubleValue();
		DecimalFormat df = new DecimalFormat("0.00");
		return Double.valueOf(df.format(ret)).doubleValue();
	}

	/**
	 * @Description: 格式化浮点类型，保留两位小数
	 * @param money
	 * @return
	 */
	public static double formatMoney(Object money) {
		DecimalFormat df = new DecimalFormat("0.00");
		return Double.valueOf(df.format(money)).doubleValue();
	}

	/**
	 * 获取请求的上下文路径
	 * @author hanson
	 * @param request
     * @return 上下文路径（/+项目名）
	 */
	public static String getContextPath(HttpServletRequest request){
		String contextPath = request.getContextPath().equals("/") ? "" : request.getContextPath();
		return contextPath;
	}
	
	/**
     * 获取请求的绝对路径（URL去掉资源路径）
     * @author hanson
     * @param request
     * @return 请求的绝对路径（协议+服务器名+端口+项目名）
     */
	public static String getWebPath(HttpServletRequest request) {
		String url = "http://" + request.getServerName();
			if (null2Int(Integer.valueOf(request.getServerPort())) != 80)
				url = url + ":" + null2Int(Integer.valueOf(request.getServerPort())) + getContextPath(request);
			else {
				url = url + getContextPath(request);
			}
		return url;
	}
	
	/**
     * 获得工程的classpath根目录
     * @author hanson
     * @return String
     */
	public static String getClasspath() {
	    try {
	    	// 随便填一个class类
	        return Class.forName("com.shifang.core.domain.IdEntity").getResource("/").getPath();
	    } catch (Exception e) {
	    	log.info("获取classpath错误", e);
	    }
	    return "";
	}
	 
	/**
	 * 获得工程的WebRoot根目录
	 * @author hanson
	 * @return String
	 */
	public static String getWebRootPath() {
	    try {
	        String classpath = getClasspath();// classes 目录的物理路径
	        String webInfoPath = new File(classpath).getParent();// WEB-INF 目录的物理路径
	        return new File(webInfoPath).getParent();// WebRoot 目录的物理路径
	    } catch (Exception e) {
	    	log.info("获取WebRoot根目录错误", e);
	    }
	    return "";
	}
	
	/**
	 * 获取项目部署的真实目录 跟getWebRootPath()差不多，但是通过request获取更准确
	 * @param request
	 * @return
	 */
	public static String getRealPath(HttpServletRequest request){
		String real_path = request.getSession().getServletContext().getRealPath("/");
		return real_path;
	}
	
	public static String getRealPath(ServletContext servletContext){
		String real_path = servletContext.getRealPath("/");
		return real_path;
	}
	
	/**
	* @Description: 获取客户端真实IP地址（解决了使用反向代理软件获取不到IP的情况）
	* @param request
	* @return
	*/
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
	    if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
	    	ip = request.getHeader("Proxy-Client-IP");
	    }
	    if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
	    	ip = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
	    	ip = request.getRemoteAddr();//返回客户端的ip地址
	    }
	    return ip;
    }
    
    public static ServletContext getServletContextBySpring(){
    	WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();    
        ServletContext servletContext = webApplicationContext.getServletContext();
        return servletContext;
    }
}
