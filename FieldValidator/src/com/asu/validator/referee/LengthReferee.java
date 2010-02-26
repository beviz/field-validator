package com.asu.validator.referee;

import com.asu.validator.AbstractReferee;
import com.asu.validator.State;
import com.asu.validator.Rule.Length;

/**
 * �ַ�������У����
 * 
 * ���ֵ�Ƿ�Ϊ����
 * 
 * @author Bevis.Zhao(avengerbevis@gmail.com)
 * 
 */
public class LengthReferee extends AbstractReferee<Length> {

	@Override
	public State check(Object data) {
		String value = (String) data;
		int length = length(value);
		if (length >= rule.min() && length <= rule.max())
			return simpleSuccess();
		else
			return failure(String.format(
					"The length of string data should be between %d and %d, but %d.",
					rule.min(), rule.max(), length));
	}

	/**
	 * �õ�һ���ַ����ĳ���,��ʾ�ĳ���,һ�����ֻ��պ��ĳ���Ϊ2,Ӣ���ַ�����Ϊ1
	 * 
	 * @param s
	 *            ��Ҫ�õ����ȵ��ַ���
	 * @return i�õ����ַ�������
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