package com.asu.validator.test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Test;

import com.asu.validator.Failure;
import com.asu.validator.FieldValidator;
import com.asu.validator.Rule.EarlierThan;
import com.asu.validator.Rule.English;
import com.asu.validator.Rule.GreaterThan;
import com.asu.validator.Rule.Length;
import com.asu.validator.Rule.NonEquals;
import com.asu.validator.Rule.NonNull;

public class Tester extends TestCase {
	@SuppressWarnings("unused")
	private Map<String, List<String>> result = null;
	private SimpleBean bean = new SimpleBean();

	static {
		// debug模式运行性能较product模式低
		// FieldValidator.debugMode();
		// product模式性能较高
		FieldValidator.productMode();
		// 只可在product模式执行
		// FieldValidator.cachedRules(SimpleBean.class);
	}

	@Test
	public void testNonNull() {
		assertEquals("Field start should be null", FieldValidator.validate(
				bean, "none").size() > 0, true);
	}

	@Test
	public void testLength() {
		bean.setString("共和国");
		assertFalse(FieldValidator.validate(bean, "string").size() > 0);
		bean.setString("中华人民共和国");
		assertTrue(FieldValidator.validate(bean, "string").size() > 0);
	}

	@Test
	public void testEnglish() {
		bean.setEnglish("中国人");
		assertEquals("English error.", FieldValidator.validate(bean, "english")
				.size() > 0, true);

		bean.setEnglish("english here");
		assertEquals("English error.", FieldValidator.validate(bean, "english")
				.size() > 0, false);
	}

	@Test
	public void testChinese() {
		bean.setChinese("中 \n国\t人");
		assertEquals("Chinese error.", FieldValidator.validate(bean, "chinese")
				.size() > 0, false);

		bean.setChinese("中 \n国\t人chinese");
		assertEquals("Chinese error.", FieldValidator.validate(bean, "chinese")
				.size() > 0, true);
	}

	@Test
	public void testRealtime() {
		// 测试实时注册校验器
		// Lover 应为Nancy
		bean.setRealtime("Nancz");
		FieldValidator.registerReferee(Realtime.Author.class,
				Realtime.LoverReferee.class);
		assertEquals("Lover is not Nancy", FieldValidator.validate(bean,
				"realtime").size() > 0, true);
	}

	@Test
	public void testNumber() {
		bean.setNumber("200000000000000000");
		assertEquals("number is not long", FieldValidator.validate(bean,
				"number").size() > 0, false);

		bean.setAge("950");
		assertEquals("number is not short", FieldValidator
				.validate(bean, "age").size() > 0, false);
	}

	@Test
	public void testRegex() {
		bean.setRegex("hello");
		assertEquals("regex is not match", FieldValidator.validate(bean,
				"regex").size() > 0, false);

		bean.setRegex("passw0rd");
		assertEquals("regex is not match", FieldValidator.validate(bean,
				"regex").size() > 0, true);
	}

	@Test
	public void testFullValidate() {
		bean.setData(null);
		FieldValidator.validate(bean, "data", true);
	}

	@Test
	public void testDateTime() {
		bean.setDatetime("2009-12-17 12:34:25");
		assertEquals("the string dose not matched any format", FieldValidator
				.validate(bean, "datetime").size() > 0, false);
	}

	@Test
	public void testEquals() {
		bean.setPassword("hello");
		bean.setRepassword("world");
		assertEquals("Password not equals with repassword", FieldValidator
				.validate(bean, "password").size() > 0, true);

		// 基本类型
		bean.setHigh(187);
		bean.setSamehigh(187);
		assertEquals("High not equals samehigh", FieldValidator.validate(bean,
				"high").size() > 0, true);
		assertEquals("High not equals samehigh", FieldValidator.validate(bean,
				"samehigh").size() > 0, false);
	}

	@Test
	public void testGreaterThan() {
		// number1 GreaterThan number2
		bean.setNumber1(187);

		// 大于 正确
		bean.setNumber2(187);
		assertEquals("The data is not greater than target", FieldValidator
				.validate(bean, "number1", GreaterThan.class).failure(), false);

		// 相等 正确
		bean.setNumber2(187);
		assertEquals("The data is not greater than target", FieldValidator
				.validate(bean, "number1", GreaterThan.class).failure(), false);

		// 小于 错误
		bean.setNumber2(189);
		assertEquals("The data is not greater than target", FieldValidator
				.validate(bean, "number1", GreaterThan.class).failure(), true);
	}

	@Test
	public void testLessThan() {
		// number2 LessThan number1
		bean.setNumber2(187);

		// 小于 正确
		bean.setNumber1(189);
		assertEquals("The data is not less than target", FieldValidator
				.validate(bean, "number2").size() > 0, false);

		// 相等 正确
		bean.setNumber1(187);
		assertEquals("The data is not less than target", FieldValidator
				.validate(bean, "number2").size() > 0, false);

		// 大于 错误
		bean.setNumber1(185);
		assertEquals("The data is not less than target", FieldValidator
				.validate(bean, "number2").size() > 0, true);
	}

	/**
	 * 单个字段的单个注解测试
	 */
	@Test
	public void testSingleValidate() {
		/**
		 * @English @Chinese @Length(min = 10, max = 20) @NonNull
		 */
		// 英文验证
		bean.setData("简体中文");
		assertEquals("The data is english", FieldValidator.validate(bean,
				"data", English.class).success(), false);

		// 长度验证
		bean.setData("简体中文长度>10");
		assertEquals("The data is english", FieldValidator.validate(bean,
				"data", Length.class).success(), true);

		// 空验证
		bean.setData(null);
		assertFalse(FieldValidator.validate(bean, "data", NonNull.class)
				.success());

		// 未注册验证器
		try {
			// 未注册此验证器 应抛出异常
			FieldValidator.validate(bean, "data", Override.class);
			assertTrue(false);
		} catch (IllegalArgumentException ex) {
			assertTrue(true);
		}

		// 未应用规则
		try {
			// 未注册此验证器 应抛出异常
			FieldValidator.validate(bean, "data", GreaterThan.class);
			assertTrue(false);
		} catch (IllegalArgumentException ex) {
			assertTrue(true);
		}

	}

	@Test
	public void testNonEquals() {
		bean.setNumber2(187);
		bean.setNumber1(189);
		assertTrue(FieldValidator.validate(bean, "number1", NonEquals.class)
				.success());

		bean.setNumber2(187);
		bean.setNumber1(187);
		assertFalse(FieldValidator.validate(bean, "number1", NonEquals.class)
				.success());

	}

	@Test
	public void testEarlierThan() {
		bean.setStart(new Date());
		bean.setEnd(new Date());

		assertTrue(FieldValidator.validate(bean, "start", EarlierThan.class)
				.success());

		Calendar later = Calendar.getInstance();
		later.add(Calendar.YEAR, 1);
		bean.setStart(later.getTime());
		assertFalse(FieldValidator.validate(bean, "start", EarlierThan.class)
				.success());

		try {
			FieldValidator.validate(bean, "end", EarlierThan.class);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testFailureList() {
		bean.setData("简体中文");
		List<Failure> result = FieldValidator.validate(bean, "data", true);
		assertTrue(Failure.containsRule(result, English.class));
	}

	@Test
	public void testSpeed() {
		// 长度验证
		int times = 10000;
		bean.setData("简体中文长度>10");
		bean.setStart(new Date());
		bean.setEnd(new Date());
		long start = System.currentTimeMillis();
		for (int i = 0; i < times; ++i) {
			FieldValidator.validateAll(bean);
			FieldValidator.validate(bean, "data");
		}
		System.out.println(times + " use " + (System.currentTimeMillis() - start));
	}
}
