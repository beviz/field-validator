package com.asu.validator.referee;

import java.math.BigDecimal;

import com.asu.validator.AbstractReferee;
import com.asu.validator.State;
import com.asu.validator.Rule.Num;

/**
 * 校验字符串数字类型
 * 
 * @author Bevis.Zhao(avengerbevis@gmail.com)
 * 
 */
public class NumberReferee extends AbstractReferee<Num> {

	@Override
	public State check(Object data) {
		String value = String.valueOf(data);
		try {
			switch (rule.value()) {
			case SHORT:
				Short.parseShort(value);
				return simpleSuccess();
			case INTEGER:
				Integer.parseInt(value);
				return simpleSuccess();
			case LONG:
				Long.parseLong(value);
				return simpleSuccess();
			case FLOAT:
				Float.parseFloat(value);
				return simpleSuccess();
			case DOUBLE:
				Double.parseDouble(value);
				return simpleSuccess();
			case NUMBER:
				new BigDecimal(value);
				return simpleSuccess();
			}
			return null;
		} catch (Exception e) {
			return failure(String.format(getMessageRuleFirst("number.type",
					"The data<%s> format is not type of %s"), fieldName, rule.value().name()));
		}
	}

	/**
	 * 
	 case SHORT: return regexMatch("^[-\\+]?[0-9]{1,3}$", data,
	 * "The data is not short number."); case INTEGER: return
	 * regexMatch("^[-\\+]?[0-9]{1,10}$", data,
	 * "The data is not integer number."); case LONG: return
	 * regexMatch("^[-\\+]?[0-9]{1,19}$", data, "The data is not long number.");
	 * case FLOAT: return regexMatch("^[-\\+]?\\d+(\\.\\d{1,10})?$", data,
	 * "The data is not float number."); case DOUBLE: return
	 * regexMatch("^[-\\+]?\\d+(\\.\\d{1,19})?$", data,
	 * "The data is not double number."); case NUMBER: return
	 * regexMatch("^\\d+$", data, "The data is not number.");
	 */
}
