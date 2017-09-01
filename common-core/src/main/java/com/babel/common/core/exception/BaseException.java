package com.babel.common.core.exception;

import org.apache.commons.lang3.StringUtils;

public abstract class BaseException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	private String field = "";
	
	public BaseException() {
		
	}
	/**
	 * 自定义的错误信息构造方法
	 * @param msg
	 */
	public BaseException(String msg) {
		super(msg);
	}
	/**
	 * 自定义的错误信息构造方法，带具体栏位
	 * @param msg
	 */
	public BaseException(String msg, String field) {
		super(msg);
		if(!StringUtils.isEmpty(field)) {
			this.field = field;
		}
	}
	public abstract String getErr();

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}
	
	
}
