package com.babel.common.core.exception;

import org.apache.commons.lang3.StringUtils;

public class InputNullException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String ERR = "INPUT_NULL";
	private static final String DEFAULT_MSG = "输入数据为空";

	public InputNullException() {
		super(DEFAULT_MSG);
	}
	/**
	 * 自定义的错误信息构造方法
	 * @param msg
	 */
	public InputNullException(String msg) {
		super(msg);
	}
	/**
	 * 自定义的错误信息构造方法，带具体栏位 INPUT_NULL
	 * @param msg
	 */
	public InputNullException(String msg, String field) {
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
