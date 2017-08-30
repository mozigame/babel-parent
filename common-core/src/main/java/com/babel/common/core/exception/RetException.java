package com.babel.common.core.exception;

import com.babel.common.core.data.RetResult;

public class RetException  extends Exception {
	private RetResult ret;
	public RetException(RetResult ret){
		super(ret.getMsgCode()+":"+ret.getMsgBody());
		this.ret=ret;
	}
	
	public RetException(RetResult ret, Throwable cause){
		super(ret.getMsgCode()+":"+ret.getMsgBody(), cause);
		this.ret=ret;
	}
	
	public RetResult getRetResult(){
		return ret;
	}
	
	
}
