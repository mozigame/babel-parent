package com.babel.common.core.exception;

import org.apache.commons.lang3.StringUtils;

public class DataNotExsitException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String ERR = "DATA_NOT_EXSIT";
	private static final String DEFAULT_MSG = "数据不存在";
	
	public DataNotExsitException() {
		super(DEFAULT_MSG);
	}
	/**
	 * 自定义的错误信息构造方法 DATA_NOT_EXSIT
	 * @param msg
	 */
	public DataNotExsitException(String msg) {
		super(msg);
	}
	/**
	 * 自定义的错误信息构造方法，带具体栏位  DATA_NOT_EXSIT
	 * @param msg
	 */
	public DataNotExsitException(String msg, String field) {
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
