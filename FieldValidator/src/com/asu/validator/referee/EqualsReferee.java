package com.asu.validator.referee;

import com.asu.validator.AbstractCompareReferee;
import com.asu.validator.State;
import com.asu.validator.Rule.Equals;

/**
 * ��ȱȽ�
 * @author Bevis.Zhao(avengerbevis@gmail.com)
 *
 */
public class EqualsReferee extends AbstractCompareReferee<Equals> {
	private final static String message = "The data is not equals with target field.";
	@Override
	public State check(Object data) {
		Object target = getFieldValue(rule.value());
		if(target == data)
			return simpleSuccess();
		if(data != null && data.equals(target))
			return simpleSuccess();
		return failure(message);
	}
}
