package com.babel.common.core.exception;

import org.apache.commons.lang3.StringUtils;

public class UnknownException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String ERR = "UNKNOWN";
	private static final String DEFAULT_MSG = "系统级未知错误，请联系系统管理员";
	
	public UnknownException() {
		super(DEFAULT_MSG);
	}
	/**
	 * 自定义的错误信息构造方法 UNKNOWN
	 * @param msg
	 */
	public UnknownException(String msg) {
		super(msg);
	}
	/**
	 * 自定义的错误信息构造方法，带具体栏位 UNKNOWN
	 * @param msg
	 */
	public UnknownException(String msg, String field) {
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
