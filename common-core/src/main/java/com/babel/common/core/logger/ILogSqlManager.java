package com.babel.common.core.logger;

import java.util.Date;
import java.util.HashMap;

public interface ILogSqlManager {
	
	
	public void addLogSqlAsync(Date startTime, String sqlId, String sql, long runTime, Throwable exp, HashMap<String, Object> paramMap);
	
	public void addLogServiceAsync(Date startTime, String method, long runTime, Throwable exp, HashMap<String, Object> paramMap);
	
	
}
