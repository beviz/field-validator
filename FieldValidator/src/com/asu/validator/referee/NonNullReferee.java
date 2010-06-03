package com.asu.validator.referee;

import java.lang.annotation.Annotation;

import com.asu.validator.AbstractReferee;
import com.asu.validator.State;
import com.asu.validator.Rule.NonNull;

/**
 * 检查数据是否不为空
 * 
 * @author Bevis.Zhao(avengerbevis@gmail.com)
 */
public class NonNullReferee extends AbstractReferee<NonNull> {

	@Override
	public State check(Object instance, Object data, Annotation rule, String fieldName) {
		return data == null 
			? failure(getMessageRuleFirst("object.nonNull","The field data is null")) 
			: simpleSuccess();
	}

	@Override
	// non-useful
	public State check(Object data) {
		return null;
	}
}
