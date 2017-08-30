package com.babel.common.core.logger;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface ILogManager {
	public void addLogInfoAsync(String method, Map<String, Object> pointMap, long runTime, Object retObj, HttpServletRequest request);
	public void addLogErrorAsync(String method, Map<String, Object> pointMap, long runTime, Throwable exp, HttpServletRequest request);
	
}
