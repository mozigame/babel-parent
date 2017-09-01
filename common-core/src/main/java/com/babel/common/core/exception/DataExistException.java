package com.babel.common.core.exception;

import org.apache.commons.lang3.StringUtils;

public class DataExistException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String ERR = "DATA_EXIST";
	private static final String DEFAULT_MSG = "数据已存在";
	
	public DataExistException() {
		super(DEFAULT_MSG);
	}
	/**
	 * 自定义的错误信息构造方法 DATA_EXIST
	 * @param msg
	 */
	public DataExistException(String msg) {
		super(msg);
	}
	/**
	 * 自定义的错误信息构造方法，带具体栏位  DATA_EXIST
	 * @param msg
	 */
	public DataExistException(String msg, String field) {
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
