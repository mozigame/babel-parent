package com.babel.common.core.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import com.babel.common.core.data.RetResult;

public class CommThreadManage<T> {
	public CommThreadManage(int threadCount){
		this.latch=new CountDownLatch(threadCount);
	}
	private Map<String, RetResult<T>> returnMap=new HashMap<String, RetResult<T>>();
	private CountDownLatch latch;
	public CountDownLatch getLatch() {
		return latch;
	}
	public RetResult<T> getRetResult(String key) {
		return returnMap.get(key);
	}
	
	public void putRetResult(String key, RetResult<T> returnResult){
		this.returnMap.put(key, returnResult);
	}
	
	public Map<String, RetResult<T>> getReturnMap(){
		return this.returnMap;
	}
	
	
}
