package com.babel.common.core.exception;

import org.apache.commons.lang3.StringUtils;

public class InputErrException extends BaseException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String ERR = "INPUT_ERR";
	private static final String DEFAULT_MSG = "输入数据有误";

	public InputErrException() {
		super(DEFAULT_MSG);
	}
	/**
	 * 自定义的错误信息构造方法 INPUT_ERR
	 * @param msg
	 */
	public InputErrException(String msg) {
		super(msg);
	}
	/**
	 * 自定义的错误信息构造方法，带具体栏位 INPUT_ERR
	 * @param msg
	 */
	public InputErrException(String msg, String field) {
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
