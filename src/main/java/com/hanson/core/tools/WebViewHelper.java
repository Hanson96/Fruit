package com.hanson.core.tools;

import java.util.HashMap;
import java.util.Map;


/**
 * 返回web视图的一些 数据帮助方法
 * @author hanson
 *
 */
public class WebViewHelper {

	/**
	 * 将前台需要用到的枚举类型的整数值放置到map中，供前台取出。  因为静态的枚举类  前台无法直接引用
	 * @param type      类型的值
	 * @param enumType  实现自定义枚举接口的  枚举类型集合
	 * @return
	 */
	public static Map enumToMap(CustomEnum[] enumType){
		Map map = new HashMap();
		for(CustomEnum c : enumType){
			map.put(c.originalName().toLowerCase(), c.value());
		}
		return map;
	}
	
}




