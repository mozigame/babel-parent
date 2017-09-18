package com.babel.common.core.exception;

import org.apache.commons.lang3.StringUtils;

public class InvalidOperateException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String ERR = "ERR_INVALID_OPERATE";
	private static final String DEFAULT_MSG = "无效操作";

	public InvalidOperateException() {
		super(DEFAULT_MSG);
	}
	/**
	 * 自定义的错误信息构造方法 ERR_REPEAT,重复操作
	 * @param msg
	 */
	public InvalidOperateException(String msg) {
		super(msg);
	}
	/**
	 * 自定义的错误信息构造方法，带具体栏位 ERR_REPEAT
	 * @param msg
	 */
	public InvalidOperateException(String msg, String field) {
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
