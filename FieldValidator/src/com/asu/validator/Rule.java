package com.asu.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 规则类, 所有内置规则存在于此. 进行Bean配置时建议使用import static 进行配置
 * 
 * @author Beviz
 */
public interface Rule {
	// 数字类型
	public static enum NumberType {
		SHORT, INTEGER, LONG, FLOAT, DOUBLE, NUMBER
	}

	// 禁止为空
	@Retention(RetentionPolicy.RUNTIME)
	@Target( { ElementType.FIELD })
	public static @interface NonNull {

	}
	
	// equals比较
	@Retention(RetentionPolicy.RUNTIME)
	@Target( { ElementType.FIELD })
	public static @interface Equals {
		// 比较的字段名, 只能与当前实体内字段比较
		String value();
	}
	
	// 值不同比较
	@Retention(RetentionPolicy.RUNTIME)
	@Target( { ElementType.FIELD })
	public static @interface NonEquals {
		// 比较的字段名, 只能与当前实体内字段比较
		String value();
	}
	
	// 字符串长度
	@Retention(RetentionPolicy.RUNTIME)
	@Target( { ElementType.FIELD })
	public static @interface Length {
		// 最小长度, 默认为0
		long min() default 0;

		// 最大长度
		long max();
	}


	// 纯英文
	@Retention(RetentionPolicy.RUNTIME)
	@Target( { ElementType.FIELD })
	public static @interface English {
	}

	// 纯中文
	@Retention(RetentionPolicy.RUNTIME)
	@Target( { ElementType.FIELD })
	public static @interface Chinese {
	}


	// 正则表达式匹配
	@Retention(RetentionPolicy.RUNTIME)
	@Target( { ElementType.FIELD })
	public static @interface Regex {
		// 正则表达式
		String value();
	}

	// 字符串的数字类型
	@Retention(RetentionPolicy.RUNTIME)
	@Target( { ElementType.FIELD })
	public static @interface Num {
		NumberType value();
	}

	// 数字区间
	@Retention(RetentionPolicy.RUNTIME)
	@Target( { ElementType.FIELD })
	public static @interface NumberRange {
		// 最小值 默认为0
		long min() default 0;

		// 最大值 如果小于{@code min}将会抛出IllegalArgumentException
		long max();
	}

	// 日期格式
	@Retention(RetentionPolicy.RUNTIME)
	@Target( { ElementType.FIELD })
	public static @interface DateTime {
		// 日期格式字符串
		String[] value();
	}
	
	/**
	 * 针对数字类型的大于等于比较
	 *
	 * @author Bevis.Zhao(avengerbevis@gmail.com)
	 * @throw IllegalArgumentException 如果当前对象或目标对象不为{@code Number}类型
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target( { ElementType.FIELD })
	public static @interface GreaterThan {
		String value();
	}
	
	/**
	 * 针对数字类型的小于等于比较
	 *
	 * @author Bevis.Zhao(avengerbevis@gmail.com)
	 * @throw IllegalArgumentException 如果当前对象或目标对象不为{@code Number}类型
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target( { ElementType.FIELD })
	public static @interface LessThan {
		String value();
	}

	// 时间早于
	@Retention(RetentionPolicy.RUNTIME)
	@Target( { ElementType.FIELD })
	public static @interface EarlierThan {
		// OGNl表达式, 与某个字段进行比较
		// 如果受比较字段为Null则抛出NullPointerException
		// 如果格式不为Date 则抛出IllegalArgumentException
		String value();
	}

	// 时间晚于
	@Retention(RetentionPolicy.RUNTIME)
	@Target( { ElementType.FIELD })
	public static @interface LaterThan {
		// OGNl表达式, 与某个字段进行比较
		// 如果受比较字段为Null则抛出NullPointerException
		// 如果格式不为Date 则抛出IllegalArgumentException
		String value();
	}
}
