package com.asu.validator.referee;

import com.asu.validator.AbstractReferee;
import com.asu.validator.State;
import com.asu.validator.Rule.English;

public class EnglishReferee extends AbstractReferee<English> {

	@Override
	public State check(Object data) {
		return regexMatch("^[A-Za-z\\s\\t\\n\\x0B\\f\\r]+$", data, "The value is not english");
	}

}
