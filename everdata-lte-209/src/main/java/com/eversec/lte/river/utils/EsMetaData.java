package com.eversec.lte.river.utils;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = { FIELD, LOCAL_VARIABLE,METHOD,CONSTRUCTOR,TYPE})
public @interface EsMetaData {
	
	String classIndexName() default "";
	
	boolean use() default true;

	String filedName() default "";

	String dataType() default "";

	String format() default "yyyyMMddHHmm";
	
	boolean store()  default true;
	
	boolean index()  default true;//no

	String indexType()  default "not_analyzed";
	
}
