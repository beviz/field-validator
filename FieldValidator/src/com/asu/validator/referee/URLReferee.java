package com.asu.validator.referee;

import com.asu.validator.AbstractReferee;
import com.asu.validator.State;
import com.asu.validator.Rule.URL;

/**
 * URL 校验
 * @author Bevis.Zhao(avengerbevis@gmail.com)
 * @deprecated 正则表达式存在问题, 在修正之前建议使用RegexReferee
 */
public class URLReferee extends AbstractReferee<URL> {

	@Override
	public State check(Object data) {
		// FIXME 正则有问题
		return regexMatch("^[a-zA-z]+://(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*(\\?\\S*)?$",
				data, "The data is not a url(start with http:// or https://)");
	}
}
