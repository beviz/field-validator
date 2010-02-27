package com.asu.validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.asu.validator.referee.ChineseReferee;
import com.asu.validator.referee.DateTimeReferee;
import com.asu.validator.referee.EarlierThanReferee;
import com.asu.validator.referee.EnglishReferee;
import com.asu.validator.referee.EqualsReferee;
import com.asu.validator.referee.GreaterThanReferee;
import com.asu.validator.referee.LengthReferee;
import com.asu.validator.referee.LessThanReferee;
import com.asu.validator.referee.NonEqualsReferee;
import com.asu.validator.referee.NonNullReferee;
import com.asu.validator.referee.NumberReferee;
import com.asu.validator.referee.RegexReferee;
/**
 * 
 * 主校验器
 *
 * @Singleton
 * @author Bevis.Zhao(avengerbevis@gmail.com)
 * 
 */
@SuppressWarnings("deprecation")
public class FieldValidator {
	// Singleton
	protected FieldValidator() {
	}
	// 规则与校验器映射
	private final static Map<Class<? extends Annotation>, Referee> rule2RefereeMap = new ConcurrentHashMap<Class<? extends Annotation>, Referee>();
	// 规则缓存
	private final static Map<Class<?>, Map<Field, Annotation[]>> cachedRulesMap = new ConcurrentHashMap<Class<?>, Map<Field,Annotation[]>>();
	
	// 初始运行模式为产品模式运行模式
	private static MODE currentMode = MODE.PRODUCT;
	
	static {
		// 初始化默认规则校验器
		registerReferee(Rule.NonNull.class, NonNullReferee.class);
		registerReferee(Rule.Equals.class, EqualsReferee.class);
		registerReferee(Rule.NonEquals.class, NonEqualsReferee.class);
		registerReferee(Rule.Length.class, LengthReferee.class);
		registerReferee(Rule.Chinese.class, ChineseReferee.class);
		registerReferee(Rule.English.class, EnglishReferee.class);
		registerReferee(Rule.Num.class, NumberReferee.class);
		registerReferee(Rule.Regex.class, RegexReferee.class);
		registerReferee(Rule.DateTime.class, DateTimeReferee.class);
		registerReferee(Rule.GreaterThan.class, GreaterThanReferee.class);
		registerReferee(Rule.LessThan.class, LessThanReferee.class);
		registerReferee(Rule.EarlierThan.class, EarlierThanReferee.class);
	}
	
	private static enum MODE{
		DEBUG, PRODUCT
	}
	
	/**
	 * 调整校验模式为调试模式
	 * 
	 * 此模式下效率低于产品运行模式
	 * 但可以在服务运行中对字段的注解进行修改以便调试
	 */
	public final static void debugMode(){
		cachedRulesMap.clear();
		currentMode = MODE.DEBUG;
	}
	
	/**
	 * 调整模式为产品运行模式
	 * 
	 * 此模式运行效率高于调试模式
	 * 但在服务运行期间对注解的修改不会被重新载入(除非使用{@link cachedRules(Class<?>)}重新缓存)
	 * 
	 */
	public final static void productMode(){
		currentMode = MODE.PRODUCT;
	}
	
	
	
	/**
	 * 对单个字段进行校验 等于{@code validate(bean, String, false)}
	 * 
	 * @param bean 实例
	 * @param fieldName 需要检验的字段名称
	 * @return
	 */
	public final static List<Failure> validate(Object bean, String fieldName) {
		return validate(bean, fieldName, false);
	}

	/**
	 * 对单个字段进行校验
	 * 
	 * @param fieldName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public final static List<Failure> validate(Object bean, String fieldName,
			boolean full) {
		Field field = getField(bean, fieldName);
		List<Failure> result = validateField(bean, field, full);
		return result == null ? Collections.EMPTY_LIST : result;
	}

	/**
	 * 读取字段
	 * @return
	 */
	private final static Field getField(Object bean, String fieldName){
		try {
			return bean.getClass().getDeclaredField(fieldName);
		} catch (Exception e) {
			throw new IllegalArgumentException(String.format(
					"The field %s cannot read from %s", fieldName, bean
							.getClass().getName()));
		}
	}
	
	/**
	 * 
	 * 对Bean中的所有字段进行校验 等于{@code validate(bean, false)}
	 * 
	 * @see {@code validate(Object, boolean}
	 * @param bean
	 * @return
	 */
	public final static Map<String, List<Failure>> validateAll(Object bean) {
		return validateAll(bean, false);
	}

	/**
	 * 对Bean中的所有字段进行校验
	 * 
	 * @param bean 实例
	 * @param full 是否校验所有的注解(false则最多返回一个失败结果)
	 * 
	 * @return 包含错误信息的Map<错误字段名, 对应错误信息描述>
	 * 
	 */
	public final static Map<String, List<Failure>> validateAll(Object bean,
			boolean full) {

		// 处理结果, fieldName 2 List<errorMessage>
		Map<String, List<Failure>> validateResult = new HashMap<String, List<Failure>>();

		Field[] fields = bean.getClass().getDeclaredFields();

		// 逐个读取字段
		for (Field field : fields) {
			List<Failure> failures = validateField(bean, field, full);
			if (failures != null && failures.size() != 0) {
				validateResult.put(field.getName(), failures);
			}
		}
		return validateResult;
	}

	/**
	 * 校验Field
	 */
	private final static List<Failure> validateField(Object bean, Field field,
			boolean full) {
		List<Failure> failures = null;
		// 读取注解
		Annotation[] annotations = getRules(bean.getClass(), field);
		if (annotations.length == 0)
			// 为减少在validateAll时的创建空集合的消耗, 在这里返回null, 在外部判断
			return null;
		// 循环注解, 进行Rule判断
		for (Annotation annotation : annotations) {
			Referee referee = rule2RefereeMap.get(annotation.annotationType());
			if (referee != null) {
				State result = validate(bean, field, annotation, referee);
				// 处理检查结果
				if (result.success()) {
					continue;
				} else {
					if(failures == null)
						failures = new ArrayList<Failure>();
					// 暂不使用错误信息国际化处理
					failures.add(Failure.valueOf(annotation.annotationType(), result.message(), result.status()));
					// 只处理一个错误
					if (full == false)
						break;
					else
						continue;
				}
			}
		}

		return failures;
	}

	/**
	 * 对单个字段的单个注解进行校验
	 * 
	 * @param bean 实例
	 * @param fieldName 字段名称
	 * @param rule 应用于规则的注解
	 * @throws IllegalArgumentException 如果 规则没有被注册 / 字段没有添加此注解
	 * @return
	 */
	public final static State validate(Object bean, String fieldName, Class<? extends Annotation> rule){
		Referee referee = rule2RefereeMap.get(rule);
		// 如果没有注册此规则, 则抛出异常
		if(referee == null)
			throw new IllegalArgumentException(String.format("The rule referee <%s> does not be registered.", rule.getName()));
		
		Field field = getField(bean, fieldName);
		Annotation annotationInstance = getRule(bean.getClass(), field, rule);
		
		// 如果字段没有应用此规则, 则抛出异常
		if(annotationInstance == null)
			throw new IllegalArgumentException(String.format("The annotation <%s> dose not be annotated on field<%s>", rule.getName(), fieldName));
		
		return validate(bean, field, annotationInstance, referee);
	}
	
	/**
	 * 核心校验 
	 */
	private final static State validate(Object bean, Field field,
			Annotation annotation, Referee referee) {
		// 允许访问私有成员
		field.setAccessible(true);
		Object fieldValue = null;
		try {
			fieldValue = field.get(bean);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		// 进行校验  暂不try-catch抛出{@code ValidateException}
		return referee.check(bean, fieldValue, annotation, field
				.getName());
	}

	/**
	 * 获取规则内容
	 * 
	 * 由于{@code Field.getAnnotations()}的效率问题, 会将规则缓存起来
	 * 
	 * @param clazz
	 * @param field
	 * @return
	 */
	private static final Annotation[] getRules(Class<?> clazz, Field field){
		if(currentMode == MODE.DEBUG)
			return field.getAnnotations();
		Map<Field, Annotation[]> field2Annotations = cachedRulesMap.get(clazz);
		if(field2Annotations == null){
			field2Annotations = new ConcurrentHashMap<Field, Annotation[]>();
			cachedRulesMap.put(clazz, field2Annotations);
			return cachedRules(field, field2Annotations);
		} 
		Annotation[] annotations = field2Annotations.get(field);
		if(annotations == null){
			annotations = cachedRules(field, field2Annotations);
		}
		return annotations;
	}

	private static final Annotation[] cachedRules(Field field, Map<Field, Annotation[]> field2Annotations){
		Annotation[] annotations = field.getAnnotations();
		field2Annotations.put(field, annotations);
		return annotations;
	}
	
	/**
	 * 获取单个规则
	 * 
	 * @param clazz
	 * @param field
	 * @param rule
	 * @return
	 */
	private static final Annotation getRule(Class<?> clazz, Field field, Class<? extends Annotation> rule){
		if(currentMode == MODE.DEBUG)
			return field.getAnnotation(rule);
		Annotation[] annotations = getRules(clazz, field);
		for(Annotation annotation : annotations)
			if(annotation.annotationType() == rule)
				return annotation;
		return null;
	}
	
	/**
	 * 手动将Bean中所有字段的规则缓存起来
	 * 只可在{@code MODE.PRODUCT}模式下执行
	 * 
	 * @param clazz 所需缓存的JavaBean
	 * @throws IllegalStateException 如果运行模式为{@code MODE.DEBUG} 
	 */
	public static final void cachedRules(Class<?> clazz){
		if(currentMode == MODE.DEBUG)
			throw new IllegalStateException("Current run mode<" + currentMode + "> not support to cached rules.");
		Field[] fields = clazz.getDeclaredFields();
		for(Field field : fields){
			field.setAccessible(true);
			getRules(clazz, field);
		}
	}
	
	/**
	 * 注册规则校验器
	 * 
	 * @param <T>
	 * @param <R>
	 * @param rule
	 * @param referee
	 */
	public final synchronized static void registerReferee(Class<? extends Annotation> rule,
			Class<? extends Referee> referee) {
		try {
			rule2RefereeMap.put(rule, referee.newInstance());
		} catch (Exception e) {
			throw new IllegalArgumentException("Found exception when initializing referee<" + referee.getName() + ">", e);
		}
	}
	
	/**
	 * 校验异常
	 * 
	 * 在校验时发生异常时抛出
	 *  
	 */
	@SuppressWarnings("unused")
	private static class ValidateException extends RuntimeException{
		private static final long serialVersionUID = 1L;
		public ValidateException(String message, Exception e) {
			super(message, e);
		}
	}
}
