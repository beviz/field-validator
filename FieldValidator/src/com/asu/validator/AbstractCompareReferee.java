package com.asu.validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * 基本比较校验器
 * 
 * @author Bevis.Zhao(avengerbevis@gmail.com)
 * 
 */
public abstract class AbstractCompareReferee<T extends Annotation> extends
		AbstractReferee<T> {
	/**
	 * 见父类
	 */
	@Override
	public State check(Object instance, Object data, Annotation annotation, String fieldName) {
		super.setup(instance, annotation, fieldName);
		return check(data);
	}
	
	/**
	 * 获取当前实例中某个字段的值
	 * 
	 * @param fieldName
	 * @return
	 */
	protected Object getFieldValue(String fieldName) {
		try {
			Field field = instance.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(instance);
		} catch (Exception e) {
			throw new IllegalArgumentException(String.format(
					"The field %s cannot read from %s", fieldName, instance
							.getClass().getName()));
		}
	}
}
