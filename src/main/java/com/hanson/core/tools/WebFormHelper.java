package com.hanson.core.tools;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.InvalidPropertyException;

import com.hanson.core.annotation.Lock;

/**
 * 将表单提交的内容自动注入到对象当中
 * @author hanson
 *
 */
public class WebFormHelper {
	private static final Logger log = LoggerFactory.getLogger(WebFormHelper.class);
	
	/**
	 * 同下面的方法一样，只不过传入的是一个class，最终会返回一个新实例
	 * @param request
	 * @param classType
	 * @return
	 */
	public static <T> T toPo(HttpServletRequest request, Class<T> classType){
		Object obj = null;
		try {
			obj = classType.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		toPo(request, obj);
		return (T)obj;
	}
	
	/**
	 * 将request提交的数据中字段与obj一致的属性进行赋值
	 * @param request 
	 * @param obj
	 * @return
	 */
	public static Object toPo(HttpServletRequest request, Object obj){
		Enumeration names = request.getParameterNames();
		List<Map> maps = new ArrayList<Map>();
		while(names.hasMoreElements()){
			String paramName = (String)names.nextElement();
			String paramValue = request.getParameter(paramName);
			Map map = new HashMap();
			map.put(paramName, paramValue);
			maps.add(map);
		}
		Map2Obj(maps, obj);
		return obj;
	}
	
	/**
	 * 同下面的方法一样，只不过传入的是一个class，最终会返回一个新实例
	 * @param request  请求参数
	 * @param newObj   新的对象，存有表单提交的数据
	 * @param classType 对象类型，将会newInstance()一个对象
	 * @param obj_name 实体对象的名称
	 * @param ordinal  实体对象在参数字段中出现的顺序位置 ，位置不可小于0。
	 * 	                      比如表单字段 user.name的user实体对象出现的位置为1； user.admin.name的admin位置为2；name的obj对象出现的位置为0
	 * @return
	 */
	public static <T> T toPo(HttpServletRequest request, Object newObj, Class<T> classType, String obj_name, int ordinal){
		Object oldObj = null;
		try {
			oldObj = classType.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		toPo(request, newObj, oldObj, obj_name, ordinal);
		return (T)oldObj;
	}
	/**
	 * 表单提交后，springmvc自动将数据赋值给新对象。此方法根据request提交的字段自动将新对象的数据赋值给实体对象
	 * 达到直接跟实体对象赋值的目的。只支持对象属性之间的赋值，复杂对象不会被赋值
	 * @param request  请求参数
	 * @param newObj   新的对象，存有表单提交的数据
	 * @param oldObj   老对象，也就是从数据库获取的实体对象
	 * @param obj_name 实体对象的名称
	 * @param ordinal  实体对象在参数字段中出现的顺序位置 ，位置不可小于0。
	 * 	                      比如表单字段 user.name的user实体对象出现的位置为1； user.admin.name的admin位置为2；name的obj对象出现的位置为0
	 * @return
	 */
	public static Object toPo(HttpServletRequest request, Object newObj, Object oldObj, String obj_name, int ordinal){
		Enumeration names = request.getParameterNames();
		List<Map> maps = new ArrayList<Map>();
		while(names.hasMoreElements()){
			String paramName = (String)names.nextElement();
			if(CommUtil.getCharCountInStr(paramName, '.') != ordinal){ // ordinal是多少就说明有多少个点号
				continue;
			}
			int pos = StringUtils.ordinalIndexOf(paramName, ".", ordinal); // 获取第 ordinal 个.点号出现的位置
			String param_obj_name = null; 
			// 从参数中的提取对象名
			if(ordinal > 1){
				int pos_previous = StringUtils.ordinalIndexOf(paramName, ".", ordinal-1);
				param_obj_name = paramName.substring(pos_previous + 1, pos);
			}else if(ordinal == 1){
				param_obj_name = paramName.substring(0, pos);
			}else if(ordinal == 0){
				param_obj_name = obj_name; // 为0，说明默认就是本身，只需要获取属性名即可
			}else{
				log.error("request参数中对象名出现的顺序位置不可能小于0");
			}
			// 从参数中提取属性名
			int pos_next = StringUtils.ordinalIndexOf(paramName, ".", ordinal+1);
			String obj_property_name = null;
			if(pos_next==-1){//说明 obj_name 后面没有对象了。后面就是属性名
				obj_property_name =  paramName.substring(pos + 1); // 获取属性名
			}
			// 说明存在此对象 并且属性不为空
			if(StringUtils.equals(obj_name, param_obj_name) && StringUtils.isNotEmpty(obj_property_name)){ 
				String paramValue = request.getParameter(paramName);
				BeanWrapper wrapper = new BeanWrapperImpl(newObj);
				PropertyDescriptor property = null;
				try{
					property = wrapper.getPropertyDescriptor(obj_property_name);
				}catch(InvalidPropertyException e){
					property = null;
				}
				if(property==null || !wrapper.isWritableProperty(obj_property_name) || property.getWriteMethod() == null){
					continue; // 属性不存在 或 不可写 或者 没有get方法 则跳过
				}
				if(isPropertyLock(property, obj_property_name)){
					continue; // 如果属性被锁了 则跳过
				} 
				Object value = wrapper.getPropertyValue(obj_property_name);
				wrapper = new BeanWrapperImpl(oldObj);
				wrapper.setPropertyValue(obj_property_name, value);
			}
		}
		return oldObj;
	}
	/**
	 * 将map数据对应赋值到obj这个bean当中
	 * @param maps
	 * @param obj
	 */
	private static void Map2Obj(List<Map> maps, Object obj) {
		BeanWrapper wrapper = new BeanWrapperImpl(obj);
		PropertyDescriptor properties[] = wrapper.getPropertyDescriptors();
		for(int i=0; i<properties.length; i++){
			String propertyName = properties[i].getName();
			if(!wrapper.isWritableProperty(propertyName) || properties[i].getWriteMethod() == null){
				continue; // 属性不可写 或者 没有get方法 则跳过
			}
			for(int j=0; j<maps.size(); j++){
				Map map = maps.get(j);
				Iterator keys = map.keySet().iterator();
				while(keys.hasNext()){
					String key = (String)keys.next();
					if(key.equals(propertyName)){
						if(isPropertyLock(properties[i], propertyName)){
							continue;
						} 
						Object propertyValue = null;
						try {
							Class<?> clazz = properties[i].getPropertyType();
							propertyValue = ConvertUtils.convert(map.get(key), clazz);;
						}catch (Exception e) {
							log.warn("类型转换错误："+properties[i].getPropertyType());
							if(properties[i].getPropertyType().toString().equals("int")){
								propertyValue = Integer.valueOf(0);
							}
							if(properties[i].getPropertyType().toString().toLowerCase().indexOf("boolean") >= 0){
								propertyValue = Boolean.valueOf(false);
							}
			            }
						wrapper.setPropertyValue(properties[i].getName(), propertyValue);
					}
					
				}
			}
		}
	}
	
	/**
	 * 获取参数中以 key_ 开头的。表明是值对行数据
	 * @param request
	 * @return
	 */
	public static Map getRequestKeyParam(HttpServletRequest request){
		Map key_map = new HashMap();
		Enumeration names = request.getParameterNames();
		List<Map> maps = new ArrayList<Map>();
		while(names.hasMoreElements()){
			String paramName = (String)names.nextElement();
			if(paramName.startsWith("key_")){
				String paramValue = request.getParameter(paramName);
				key_map.put(paramName.substring(4), paramValue);
			}
		}
		return key_map;
	}
	
	/**
	 * 判断属性是否被锁
	 * @param property
	 * @param propertyName
	 * @return
	 */
	private static boolean isPropertyLock(PropertyDescriptor property, String propertyName){
		Lock lock = null;
		lock = (Lock)property.getWriteMethod().getAnnotation(Lock.class);
		// 类的属性的写方法上有没有标注lock
		if(lock == null){
			try {
				Field f = property.getWriteMethod().getDeclaringClass().getDeclaredField(propertyName);
				// 属性上边有没有标注lock注解
				lock = f.getAnnotation(Lock.class);
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
		if(lock != null){
			return true;
		}else{
			return false;
		}
	}
}
