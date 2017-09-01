package com.babel.common.core.exception;

import org.apache.commons.lang3.StringUtils;

public class SessionNullException extends BaseException {
	private static final String ERR = "SESSION_NULL";
	private static final String DEFAULT_MSG = "没有用户缓存信息";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static SessionNullException excepton = new SessionNullException();
	
	private SessionNullException() {
		super(DEFAULT_MSG);
	}
	
	public static SessionNullException getException() {
		return excepton;
	}
	
	@Override
	public String getErr() {
		if(StringUtils.isEmpty(this.getField())) {
			return ERR;
		}
		return ERR + "_" + this.getField();
	}
}
