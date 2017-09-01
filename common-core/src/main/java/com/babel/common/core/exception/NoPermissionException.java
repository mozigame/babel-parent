package com.babel.common.core.exception;

import org.apache.commons.lang3.StringUtils;

public class NoPermissionException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String ERR = "NO_PERMISSION";
	private static final String DEFAULT_MSG = "没有权限";
	
	public NoPermissionException() {
		super(DEFAULT_MSG);
	}
	/**
	 * 自定义的错误信息构造方法 NO_PERMISSION
	 * @param msg
	 */
	public NoPermissionException(String msg) {
		super(msg);
	}
	/**
	 * 自定义的错误信息构造方法，带具体栏位  NO_PERMISSION
	 * @param msg
	 */
	public NoPermissionException(String msg, String field) {
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
