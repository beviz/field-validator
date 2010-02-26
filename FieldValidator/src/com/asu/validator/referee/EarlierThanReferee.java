package com.asu.validator.referee;

import java.util.Date;

import com.asu.validator.AbstractCompareReferee;
import com.asu.validator.State;
import com.asu.validator.Rule.EarlierThan;

/**
 * 日期早于比较
 * 
 * 比较日期与被比较日期需均为@{code java.util.Date}类型, 且不为null 否则抛出IllegalArgumentException
 * 
 * @author Bevis.Zhao(avengerbevis@gmail.com)
 * 
 */
public class EarlierThanReferee extends AbstractCompareReferee<EarlierThan> {

	@Override
	public State check(Object data) {
		Object target = getFieldValue(rule.value());
		// 不可为null
		if (data == null || target == null)
			throw new IllegalArgumentException(String.format(
					"Both field<%s> and to-compare-field<%s> cannot be null.",
					fieldName, rule.value()));

		// 必须为date类型
		if (!(data instanceof Date) || !(target instanceof Date))
			throw new IllegalArgumentException(String.format(
					"Both field<%s> and to-compare-field<%s> only allowed type of java.util.Date.",
					fieldName, rule.value()));

		Date currDate = (Date) data, targetDate = (Date) target;

		if (currDate.getTime() <= targetDate.getTime())
			return simpleSuccess();
		else
			return failure("The data is not earlier than target data.");

	}

}
