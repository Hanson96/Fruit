package com.hanson.core.tools;

import java.util.Enumeration;

/**
 * 自定义的枚举类型接口
 * @author hanson
 *
 */
public interface CustomEnum{

	/**
	 * 获取自定义枚举类型的名称
	 * @return
	 */
	public abstract String typeName();
	
	/**
	 * 获取自定义枚举类型的值
	 * @return
	 */
	public abstract int value();
	
	/**
	 * 获取原始的name 也就是 英文大写名称
	 * @return
	 */
	public abstract String originalName();
}
