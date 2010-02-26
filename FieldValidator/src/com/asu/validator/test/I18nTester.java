package com.asu.validator.test;

import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.Test;

public class I18nTester {

	@Test
	public void testLocale(){
		Locale currentLocale = Locale.getDefault();
		ResourceBundle bundle = ResourceBundle.getBundle("fieldvalidator", currentLocale);
		System.out.println(bundle.getString("x"));
	}
}
