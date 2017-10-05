package com.babel.common.core.util;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.babel.common.core.data.RetData;


/**
 * 保存最近异常信息
 * @author Justin
 *
 */
public class RecentlyErrors {
	private static Map<String, ErrorInfo> errorInfoMap=new ConcurrentHashMap<>();
	public static void logExp(Class clazz, String key, String errorInfo){
		String keys=clazz.getSimpleName()+"."+key;
		ErrorInfo error=errorInfoMap.get(keys);
		if(error==null){
			error=new ErrorInfo();
			error.setCount(0);
		}
		error.setCount(error.getCount()+1);
		error.setLastTime(System.currentTimeMillis());
		error.setErrorInfo(errorInfo);
		
		errorInfoMap.put(keys, error);
	}
	
	public static void logExp(Class clazz, String key, RetData retData){
		String errorInfo="err:"+retData.getErr()+",msg:"+retData.getMsg();
		logExp(clazz, key, errorInfo);
	}
	
	public static String getErrorInfos(){
		Set<String> keys=errorInfoMap.keySet();
		
		String info="";
		ErrorInfo errorInfo=null;
		for(String key:keys){
			errorInfo=errorInfoMap.get(key);
			info+="key:"+key+", count:"+errorInfo.getCount()+", lastTime:"+errorInfo.getLastTime()+", errorInfo:"+errorInfo.getErrorInfo()+"\n";
		}
		return info;
	}
}

class ErrorInfo{
	private Integer count;
	private Long lastTime;
	private String errorInfo;
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Long getLastTime() {
		return lastTime;
	}
	public void setLastTime(Long lastTime) {
		this.lastTime = lastTime;
	}
	public String getErrorInfo() {
		return errorInfo;
	}
	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}
	
}
