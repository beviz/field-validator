package com.asu.validator.test;

import java.util.Date;

import com.asu.validator.Rule.Chinese;
import com.asu.validator.Rule.DateTime;
import com.asu.validator.Rule.EarlierThan;
import com.asu.validator.Rule.English;
import com.asu.validator.Rule.Equals;
import com.asu.validator.Rule.GreaterThan;
import com.asu.validator.Rule.LaterThan;
import com.asu.validator.Rule.Length;
import com.asu.validator.Rule.LessThan;
import com.asu.validator.Rule.NonEquals;
import com.asu.validator.Rule.NonNull;
import com.asu.validator.Rule.Num;
import com.asu.validator.Rule.NumberRange;
import com.asu.validator.Rule.NumberType;
import com.asu.validator.Rule.Regex;

public class SimpleBean {
	@English
	private String english;
	@Chinese
	private String chinese;
	@NonNull
	private String none;
	@Length(max = 10)
	private String string;

	@Num(NumberType.LONG)
	private String number;

	@Equals("repassword")
	private String password;
	private String repassword;

	@Equals("repassword")
	private int high;
	@Equals("high")
	private int samehigh;

	@GreaterThan("number2")
	@NonEquals("number2")
	private int number1;

	@LessThan("number1")
	private int number2;

	@Realtime.Author
	private String realtime;

	@Num(NumberType.SHORT)
	@NumberRange(max = 150)
	private String age;

	@Regex("^[a-zA-Z]*$")
	private String regex;

	@DateTime("yyyy-MM-dd")
	private String datetime;

	@EarlierThan("end")
	private Date start;
	
	@LaterThan("start")
	private Date end;
	
	@English
	@Chinese
	@Length(min = 10, max = 20)
	@NonNull
	private String data;

	public String getRealtime() {
		return realtime;
	}

	public void setRealtime(String realtime) {
		this.realtime = realtime;
	}

	public String getNone() {
		return none;
	}

	public String getEnglish() {
		return english;
	}

	public void setEnglish(String english) {
		this.english = english;
	}

	public String getChinese() {
		return chinese;
	}

	public void setChinese(String chinese) {
		this.chinese = chinese;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRepassword() {
		return repassword;
	}

	public void setRepassword(String repassword) {
		this.repassword = repassword;
	}

	public int getHigh() {
		return high;
	}

	public void setHigh(int high) {
		this.high = high;
	}

	public int getSamehigh() {
		return samehigh;
	}

	public void setSamehigh(int samehigh) {
		this.samehigh = samehigh;
	}

	public int getNumber1() {
		return number1;
	}

	public void setNumber1(int number1) {
		this.number1 = number1;
	}

	public int getNumber2() {
		return number2;
	}

	public void setNumber2(int number2) {
		this.number2 = number2;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

}
