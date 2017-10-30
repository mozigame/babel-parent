package com.babel.common.core.exception;

import com.babel.common.core.data.RetData;

public class RetDataException  extends BaseException {
	private final RetData retData;
	public RetDataException(RetData retData){
		super(retData.getMsg());
		this.retData=retData;
	}

	@Override
	public String getErr() {
		// TODO Auto-generated method stub
		return retData.getErr();
	}

}
