package com.asu.validator;

import java.lang.annotation.Annotation;
import java.util.regex.Pattern;

public abstract class AbstractReferee<T extends Annotation> implements Referee {

	// 规则
	protected T rule;
	// Bean实例
	protected Object instance;
	// 校验字段的字段名
	protected String fieldName;
	
	private final static State SIMPLE_SUCCESS =  new State(true, "");
	/**
	 * 创建简单的成功状态的State对象 
	 */
	protected final static State simpleSuccess(){
		return SIMPLE_SUCCESS;
	}
	
	/**
	 * 创建成功对象
	 * @param message
	 * @return
	 */
	protected final static State success(String message){
		return new State(true, message);
	}
	
	/**
	 * 创建失败对象
	 * @return
	 */
	protected final static State failure(String message){
		return new State(false, message);
	}
	
	/**
	 * 设置相关数据 
	 */
	@SuppressWarnings("unchecked")
	protected void setup(Object instance, Annotation annotation, String fieldName){
		this.instance = instance;
		this.rule = (T)annotation;
		this.fieldName = fieldName;
	}
	
	/**
	 * 自动处理Null检查
	 */
	public State check(Object instance, Object data, Annotation annotation, String fieldName) {
		setup(instance, annotation, fieldName);
		if(data == null)
			return simpleSuccess();
		return check(data);
	}
	/**
	 * 正则检查 
	 */
	protected final State regexMatch(String regex, Object data, String errorMessage){
		if(Pattern.matches(regex, (String)data))
			return simpleSuccess();
		else
			return failure(errorMessage);
	}
	
	/**
	 * 
	 */
	public abstract State check(Object data) ;
}
