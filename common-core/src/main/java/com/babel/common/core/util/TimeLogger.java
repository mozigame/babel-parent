package com.babel.common.core.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

/**
 * 方法内时间日志处理工具
 * 用于性能分析处理
 * @author 金和
 *
 */
public class TimeLogger {
	private static final Logger log4 = Logger.getLogger(TimeLogger.class);
	private Logger logger;
	private String methodCode;
	private long time;
	private Map<String, Long> timeMap=new TreeMap<String, Long>();
	
	public TimeLogger(Logger logger, String methodCode){
		this.logger=logger;
		this.methodCode=methodCode;
		this.time=System.currentTimeMillis();
	}
	
	public void time(String key){
		this.timeMap.put(key, (System.currentTimeMillis()-time));
	}
	
	public void printTime(Object obj, String code){
		if(this.logger==null){
			this.logger=log4;
		}
		
		String info="";
		if(obj!=null){
			String className=obj.getClass().getSimpleName();
			info+=" "+className;
		}
		
		if(methodCode!=null){
			info+=" "+methodCode;
		}

		Date date = new Date(this.time);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
		info+=" "+code+"="+timeMap;
		String logInfo="-----timeLogger-"+info;
		logInfo+="\n=====["+sdf.format(date)+"]-timeLogger="+info;
		logger.info(logInfo);
//		System.out.println(logInfo);
	}
}
