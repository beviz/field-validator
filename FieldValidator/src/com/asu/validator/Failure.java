package com.asu.validator;

import java.io.Serializable;
import java.lang.annotation.Annotation;

/**
 * 校验失败信息类
 * @author Bevis.Zhao(avengerbevis@gmail.com)
 *
 */
public class Failure implements Serializable{
	private static final long serialVersionUID = 1L;

	private Class<? extends Annotation> rule;
	private String message;
	private Integer status ;
	
	public static Failure valueOf(Class<? extends Annotation> rule, String message, Integer status){
		return new Failure(rule, message, status);
	}
	
	private Failure(Class<? extends Annotation> rule, String message, Integer status){
		this.rule = rule;
		this.message = message;
		this.status = status;
	}

	public Class<? extends Annotation> rule() {
		return rule;
	}

	public String message() {
		return message;
	}

	public Integer status() {
		return status;
	}
	
	/**
	 * Equals判断, 为了在集合中可以contains查找
	 * 打破equals惯例, 可以使用Rule进行判断
	 * 当Rule相同时, 返回true;
	 * 
	 */
	@Override
	public String toString(){
		return String.format("Failure > rule : %s, message : %s, status : %s", rule.getName(), message, status);
	}
	
	/**
	 * 判断Failure的集合中是否包含具有指定Rule的Failure
	 * 
	 * @param iterable 失败信息Failure集合
	 * @param rule
	 * @return
	 */
	public static boolean containsRule(Iterable<Failure> iterable, Class<? extends Annotation> rule){
		for(Failure f : iterable)
			if(f.rule == rule)
				return true;
		return false;
	}
}
