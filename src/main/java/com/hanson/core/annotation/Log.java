package com.hanson.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.hanson.foundation.domain.SystemLog;


@Target({java.lang.annotation.ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented//表示该注解信息被例如javadoc此类的工具文档化，即生成的chm文档会包含该注解
@Inherited//阐述了该注解的类型是被继承的。如果一个使用了@Inherited修饰的annotation类型被用于一个class，则这个annotation将被用于该class的子类
//注意：@Inherited annotation类型是被标注过的class的子类所继承。类并不从它所实现的接口继承annotation，方法并不从它所重载的方法继承annotation。
public @interface Log {
	
	/** 标题  */
	public abstract String title();
	/** 日志类型  */
	public abstract SystemLog.LogType type();
	/** 操作的实体 */
	public String entityName() default "";
	
}
