package com.hanson.core.tools;

import java.beans.PropertyDescriptor;
import java.text.ParseException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.web.servlet.ModelAndView;

import com.hanson.core.query.QueryObject;

/**
 * 查询相关的一些工具方法
 * @author hanson
 *
 */
public class QueryHelper {

	private static final Logger log = LoggerFactory.getLogger(QueryHelper.class);
	
	private static QueryHelper instance = new QueryHelper();
	
	private QueryHelper(){}
	
	/**
	 * 单例模式
	 * @return
	 */
	public static QueryHelper getInstance(){
		return instance;
	}
	
	/**
	 * @Description: 将表单查询的参数返回给mv，使查询的数据不随表单提交而消失
	 * @param request
	 * @param mv
	 * @return
	 */
	public static void queryParamsIntoModel(HttpServletRequest request, ModelAndView mv) {
		Map map = request.getParameterMap();
		Enumeration enum1 = request.getParameterNames();
		String value;
		Map queryMap = new HashMap();
		while (enum1.hasMoreElements()) {
			String paramName = (String) enum1.nextElement();
			if(paramName.startsWith("q_")){ // q_ 开头说明是查询字段
				value = request.getParameter(paramName);
				paramName = paramName.replaceAll("\\.", "_"); // 将点号替换成下划线
				queryMap.put(paramName, value);
			}
		}
		mv.addObject("queryMap", queryMap);
	}
	
	/**
	 * 批量添加查询表达式,比如  addQueryBatch(User.class, qo, request, new String[]{"age","sex"},"=")
	 * @param qo
	 * @param requset
	 * @param fields
	 * @param expression
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static void addQueryBatch(Class<?> clazz, QueryObject qo, HttpServletRequest request, String[] fields, String expression){
		Map map = request.getParameterMap();
		Enumeration enum1 = request.getParameterNames();
		SimpleMap s_map = new SimpleMap(null,null);
		while (enum1.hasMoreElements()) {
			String paramName = (String) enum1.nextElement();
			if(StringUtils.isNotEmpty(request.getParameter(paramName)) && paramName.startsWith("q_")){ // q_ 开头说明是查询字段
				String field = paramName.substring(2); // 截取字符串，前两个不要
				if(CommUtil.isContainStr(fields, field)){
					BeanWrapper wrapper = null;
					PropertyDescriptor property = null;
					Class<?> propertyType = clazz;
					String field_mark = field; // 寻找.点号，深层获取实体类的属性类型
					int mark_index = -1;
					boolean has_mark = false; // 标志是否进行了深层类型反射获取
					while(field_mark.indexOf(".")!=-1){
						has_mark = true;
						mark_index = field_mark.indexOf(".");
						String field_sub = field_mark.substring(0, mark_index); // 截取第一个点号前的字符串
						field_mark = field_mark.substring(mark_index+1);  // 截取第一个点号后的字符串(不包括第一个点号)
						wrapper = new BeanWrapperImpl(propertyType);
						property = wrapper.getPropertyDescriptor(field_sub);
						propertyType = property.getPropertyType();
					}
					wrapper = new BeanWrapperImpl(propertyType);
					property = wrapper.getPropertyDescriptor(field_mark); // 反射获取实体类的属性
					propertyType = property.getPropertyType(); // 获取这个属性的类型
					Object value = null;
					try{
						value = ConvertUtils.convert(request.getParameter(paramName), propertyType);//将参数值转化为相应的属性类型
					}catch(Exception e){
						if(propertyType==Date.class){
							value = CommUtil.formatLongDateStr(request.getParameter(paramName));
							if(value == null){
								value = CommUtil.formatShortDateStr(request.getParameter(paramName));
							}
						}
					}
					conditionHandle(field, value, s_map, expression);
					qo.addQuery("obj."+field, s_map, expression);
				}
			}
		}
	}
	
	private static void conditionHandle(String field, Object value, SimpleMap s_map, String expression){
		field = field.replaceAll("\\.", "_"); // 讲查询字段的.点号替换成下划线，避免hibernate不能解析
		System.out.println("field:"+field+" value:"+value);
		if(StringUtils.equals(expression.trim(), "like")){
			s_map.setKey(field);
			s_map.setValue("%"+value+"%");
			return;
		}
		s_map.setKey(field);
		s_map.setValue(value);
	}
}
