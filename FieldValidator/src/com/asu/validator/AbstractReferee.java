package com.asu.validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * 
 * 规则校验器抽象实现，提供基础功能
 * 
 *
 * @author Bevis.Zhao
 * @date 2010-6-3
 * @param <T>
 */
public abstract class AbstractReferee<T extends Annotation> implements Referee {
	
	// 在properties文件中读取的消息
	public final static Map<String, String> VALIDATE_MESSAGES = new HashMap<String, String>();
	
	static {
		initValidateMessages();
	}
	
	/**
	 * 初始化验证消息
	 */
	private static void initValidateMessages(){
		// 在product模式只会执行此方法一次，但debug模式中会不断读取。所以清理
		VALIDATE_MESSAGES.clear();
		// 初始化读取配置文件
		try {
			// 读取文件名为field-validator_messages.properties
			ResourceBundle messages = ResourceBundle.getBundle("field-validator_messages");
			for (String key : messages.keySet()) {
				String message = messages.getString(key);
				VALIDATE_MESSAGES.put(key, message);
			}
		} catch (Exception e) {
			// 读取失败，不报错
			return;
		}
	}
	/**
	 * 获取Message，优先从rule中读取，
	 * 
	 * 如果找不到则在查询properties读取数值
	 * 
	 * @param key
	 * @return
	 */
	protected String getMessageRuleFirst(String key, String def){
		
		// 优先读取ruleMessage
		String ruleMessage = (String)invokeMethod(rule, "message");
		return ! ruleMessage.trim().isEmpty() 
			? ruleMessage.trim()
			: getMessage(key, def);
	}
	
	/**
	 * 从properties文件中读取message，如果没有则使用def
	 * 
	 * @param key
	 * @param def
	 * @return
	 */
	protected String getMessage(String key, String def){
		if (FieldValidator.isDebugMode()){
			initValidateMessages();
		}
		String found = VALIDATE_MESSAGES.get(key);
		return found == null ? def : found;
	}
	
	// 规则
	protected T rule;
	// Bean实例
	protected Object instance;
	// 校验字段的字段名
	protected String fieldName;
	
	private final static State SIMPLE_SUCCESS =  new State(true, "");
	/**
	 * 创建简单的成功状态的State对象 
	 */
	protected final static State simpleSuccess(){
		return SIMPLE_SUCCESS;
	}
	
	/**
	 * 创建成功对象
	 * @param message
	 * @return
	 */
	protected final static State success(String message){
		return new State(true, message);
	}
	
	/**
	 * 创建失败对象
	 * @return
	 */
	protected final static State failure(String message){
		return new State(false, message);
	}
	
	/**
	 * 设置相关数据 
	 */
	@SuppressWarnings("unchecked")
	protected void setup(Object instance, Annotation annotation, String fieldName){
		this.instance = instance;
		this.rule = (T)annotation;
		this.fieldName = fieldName;
	}
	
	/**
	 * 自动处理Null检查
	 */
	public State check(Object instance, Object data, Annotation annotation, String fieldName) {
		setup(instance, annotation, fieldName);
		if(data == null)
			return simpleSuccess();
		return check(data);
	}
	/**
	 * 正则检查 
	 */
	protected final State regexMatch(String regex, Object data, String errorMessage){
		if(Pattern.matches(regex, (String)data))
			return simpleSuccess();
		else
			return failure(errorMessage);
	}

	/**
	 * 调用方法
	 *  
	 * @param object
	 * @param methodName
	 * @param parameters
	 * @return
	 */
	private static Object invokeMethod(Object object, String methodName, Object... parameters){
		Class<?>[] parameterTypes = new Class<?>[parameters.length];
		for(int i = 0; i < parameters.length; i++){
			parameterTypes[i] = parameters[i].getClass();
		}
		Method method = null;
		try {
			method = object.getClass().getMethod(methodName, parameterTypes);
			return method.invoke(object, parameters);
		} catch (Exception e) {
			throw new RuntimeException(String.format("调用类<%s>方法<%s>失败！", 
					object.getClass(), method));
		}
	}
	/**
	 * 
	 */
	public abstract State check(Object data) ;
}
