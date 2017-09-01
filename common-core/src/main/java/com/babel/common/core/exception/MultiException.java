package com.babel.common.core.exception;

import org.apache.commons.lang3.StringUtils;

public class MultiException extends BaseException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String ERR = "MULTI";
	private static final String DEFAULT_MSG = "数据不唯一";

	public MultiException() {
		super(DEFAULT_MSG);
	}
	/**
	 * 自定义的错误信息构造方法 MULTI
	 * @param msg
	 */
	public MultiException(String msg) {
		super(msg);
	}
	/**
	 * 自定义的错误信息构造方法，带具体栏位 MULTI
	 * @param msg
	 */
	public MultiException(String msg, String field) {
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
