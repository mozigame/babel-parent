package com.babel.common.core.exception;

import org.apache.commons.lang3.StringUtils;

public class PasswordInvalidException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String ERR = "PASSWORD_INVALID";
	private static final String DEFAULT_MSG = "密码错误";
	
	public PasswordInvalidException() {
		super(DEFAULT_MSG);
	}
	/**
	 * 自定义的错误信息构造方法 DATA_EXIST
	 * @param msg
	 */
	public PasswordInvalidException(String msg) {
		super(msg);
	}
	/**
	 * 自定义的错误信息构造方法，带具体栏位  DATA_EXIST
	 * @param msg
	 */
	public PasswordInvalidException(String msg, String field) {
		super(msg, field);
	}
	@Override
	public String getErr() {
		if(StringUtils.isEmpty(this.getField())) {
			return ERR;
		}
		return ERR + "_" + this.getField();
	}
}
