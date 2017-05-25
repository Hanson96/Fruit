package com.hanson.core.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
/**
 * 注解锁   实体类的属性上如果标注此注解，
 * 则提交表单时 WebFormhelper不会给obj的这个属性赋值
 * @author hanson
 *
 */
@Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.CONSTRUCTOR, java.lang.annotation.ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Lock {

}
