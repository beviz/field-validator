package com.asu.validator;

import java.lang.annotation.Annotation;


/**
 * 检查器
 * 
 * 所有检查器实现均要实现此接口
 * 
 * @author Bevis.Zhao
 */
public interface Referee{
	
	/**
	 * 对数据进行校验
	 * @param instance TODO
	 * @param data 处理的数据
	 * @param annotation 规则注解
	 * @param fieldName 字段名称
	 * 
	 * @return 校验结果
	 */
	State check(Object instance, Object data, Annotation annotation, String fieldName);
	
}
