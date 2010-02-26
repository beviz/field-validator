package com.asu.validator.referee;

import com.asu.validator.AbstractReferee;
import com.asu.validator.State;
import com.asu.validator.Rule.Email;

/**
 * Email地址校验
 * 
 * @author Bevis.Zhao(avengerbevis@gmail.com)
 * @deprecated 正则表达式存在问题, 再修正之前建议使用RegexReferee
 */
public class EmailReferee extends AbstractReferee<Email> {

	@Override
	public State check(Object data) {
		// FIXME 正则有问题
		return regexMatch("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*",
				data, "The data is not an E-mail address.");
	}

}
