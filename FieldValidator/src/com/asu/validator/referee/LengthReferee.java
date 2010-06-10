package com.asu.validator.referee;

import java.lang.annotation.Annotation;

import com.asu.validator.AbstractReferee;
import com.asu.validator.State;
import com.asu.validator.Rule.Length;

/**
 * 字符串长度校验器
 * 
 * 检查值是否为中文
 * 
 * @author Bevis.Zhao(avengerbevis@gmail.com)
 * 
 */
public class LengthReferee extends AbstractReferee<Length> {


	/**
	 * 不进行null验证
	 */
	@Override
	public State check(Object instance, Object data, Annotation annotation,
			String fieldName) {
		setup(instance, annotation, fieldName);
		return check(data);
	}
	
	@Override
	public State check(Object data) {
		
		int length = data == null ? 0 : length(String.valueOf(data));
		
		if (length >= rule.min() && length <= rule.max())
			return simpleSuccess();
		else
			return failure(String.format(getMessageRuleFirst("string.length",
						"The length of string data should be between %d and %d, but %d."),
					rule.min(), rule.max(), length));
	}

	/**
	 * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为2,英文字符长度为1
	 * 
	 * @param s
	 *            需要得到长度的字符串
	 * @return i得到的字符串长度
	 */
	public static int length(String s) {
		if (s == null)
			return 0;
		char[] c = s.toCharArray();
		int len = 0;
		for (int i = 0; i < c.length; i++) {
			len++;
			if (c[i] / 0x80 != 0)
				len++;
		}
		return len;
	}
}
