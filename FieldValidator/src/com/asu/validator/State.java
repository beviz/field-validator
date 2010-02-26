package com.asu.validator;

/**
 * 校验器检查状态结果类
 * 
 * @author Beviz
 */
public class State {
	public State(boolean success) {
		this.success = success;
	}
	
	public State(boolean success, String message) {
		this.success = success;
		this.message = message;
	}
	
	public State(boolean success, String message, int status) {
		this.success = success;
		this.message = message;
		this.status = status;
	}

	private boolean success;
	private String message;
	private Integer status;

	public Integer status() {
		return status;
	}

	public boolean success() {
		return success;
	}
	
	public boolean failure(){
		return !success;
	}

	public String message() {
		return message;
	}
	
	@Override
	public String toString(){
		return String.format("State > success : %s, message : %s, status : %s", success, message, status);
	}
}
