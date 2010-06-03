package com.asu.validator.referee;

import com.asu.validator.AbstractCompareReferee;
import com.asu.validator.State;
import com.asu.validator.Rule.NonEquals;

/**
 * 值不相等比较
 * 
 * data == null && target != null
 * -> success
 * data != null && target == null
 * -> success
 * data equals target 
 * -> failure
 * 
 * @author Bevis.Zhao(avengerbevis@gmail.com)
 *
 */
public class NonEqualsReferee extends AbstractCompareReferee<NonEquals> {
	@Override
	public State check(Object data) {
		Object target = getFieldValue(rule.value());
		if((data == null && target != null) || (data != null && target == null))
			return simpleSuccess();
		if(data == null && target == null || data.equals(target))
			return failure(getMessageRuleFirst("object.nonEquals", "The data equals target data."));
		return simpleSuccess();
	}
}
