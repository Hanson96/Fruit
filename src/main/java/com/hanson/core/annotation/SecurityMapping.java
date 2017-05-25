package com.hanson.core.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 路径资源注解     便于对访问资源的路径进行权限控制
 * @author hanson
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.METHOD})
public @interface SecurityMapping{
	/** 资源名称 */
	public abstract String name(); 
	/** 资源路径 */
	public abstract String value(); 
	/** 所属权限 */
	//public abstract Authority.Detail[] authority();
	/**
	 * 路径资源的级别 路径范围越小越精细级别越小（默认使用精准路径0）
	 *  /aaa/bbb/ccc/dddd精准路径可设为0，/aaa/bbb/ccc也可设为0，
	 *  /aaa/bbb/ccc/**可设为10，
	 *  /aaa/bbb/**设为20
	 *  /aaa/**设为30
	 *  /**设为40
	 * 在加载路径资源时，需要把这些级别大的路径放在最后，避免由于先满足大路径权限后，像/aaa/bbb/ccc/dddd这样的精细路径不验证
	 * @return
	 */
	public int level() default 0;
}
