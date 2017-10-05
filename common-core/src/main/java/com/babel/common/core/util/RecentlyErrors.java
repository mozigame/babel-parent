package com.babel.common.core.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.babel.common.core.data.RetData;

/**
 * 保存最近异常信息
 * @author Justin
 *
 */
public class RecentlyErrors {
	private static Map<String, Integer> errorCountMap=new ConcurrentHashMap<>();
	private static Map<String, Long> errorLastTimeMap=new ConcurrentHashMap<>();
	private static Map<String, String> errorInfoMap=new ConcurrentHashMap<>();
	public static void logExp(Class clazz, String key, String errorInfo){
		String keys=clazz.getSimpleName()+"."+key;
		Integer count=errorCountMap.get(keys);
		if(count==null){
			count=0;
		}
		count++;
		errorCountMap.put(keys, count);
		errorInfoMap.put(keys, errorInfo);
		errorLastTimeMap.put(keys, System.currentTimeMillis());
	}
	
	public static void logExp(Class clazz, String key, RetData retData){
		String errorInfo="err:"+retData.getErr()+",msg:"+retData.getMsg();
		logExp(clazz, key, errorInfo);
	}
}
