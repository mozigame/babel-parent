package com.babel.common.core.exception;

import org.apache.commons.lang3.StringUtils;

public class DataHasDelException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String ERR = "DATA_HAS_DEL";
	private static final String DEFAULT_MSG = "数据不存在";
	
	public DataHasDelException() {
		super(DEFAULT_MSG);
	}
	/**
	 * 自定义的错误信息构造方法 DATA_NOT_EXSIT
	 * @param msg
	 */
	public DataHasDelException(String msg) {
		super(msg);
	}
	/**
	 * 自定义的错误信息构造方法，带具体栏位  DATA_HAS_DEL
	 * @param msg
	 */
	public DataHasDelException(String msg, String field) {
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
