package com.asu.validator.referee;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.asu.validator.AbstractReferee;
import com.asu.validator.State;
import com.asu.validator.Rule.DateTime;

public class DateTimeReferee extends AbstractReferee<DateTime> {
	private final static SimpleDateFormat sdf = new SimpleDateFormat();

	@Override
	public State check(Object data) {
		for(String format : rule.value()){
			sdf.applyPattern(format);
			try {
				sdf.parse((String)data);
				return simpleSuccess();
			} catch (ParseException e) {
				return failure("The field dose not matches any format");
			}
		}
		// Impossible
		return simpleSuccess();
	}

}
