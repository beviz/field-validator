package com.asu.validator.test;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.asu.validator.Referee;
import com.asu.validator.State;

public class Realtime {

	@Retention(RetentionPolicy.RUNTIME)
	@Target( { ElementType.FIELD })
	public static @interface Author{}
	
	public static class LoverReferee implements Referee{

		public State check(Object instance, Object data, Annotation annotation, String fieldName) {
			if(null == data || data.toString().equals("Beviz"))
				return new State(true, "");
			return new State(false, "It look likes not our hero.");
		}
	}
}
