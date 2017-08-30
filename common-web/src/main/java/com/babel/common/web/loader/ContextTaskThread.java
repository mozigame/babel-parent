package com.babel.common.web.loader;


import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;

import com.babel.common.core.data.RetResult;
import com.babel.common.core.util.SpringContextUtil;
import com.babel.common.web.constants.ConfigWebUtil;

public class ContextTaskThread implements Runnable{
	private Logger log4 = Logger.getLogger(ContextTaskThread.class);
	private String moduleCode="contextTaskThread";
	private String modelCode = getClass().getSimpleName();

	private String taskBeanName;
//	private String contextPath;
	private ServletContextEvent event;
	public ContextTaskThread(String taskBeanName, ServletContextEvent event){
		this.taskBeanName=taskBeanName;
		this.event=event;
		
	}
	public ContextTaskThread(){
		
	}
	public void run(){
		boolean isTest="true".equals(ConfigWebUtil.getConfigValue(ConfigWebUtil.config_key.SYS_IF_TEST));
		log4.info("-------contextTask-------"+taskBeanName+" isTest="+isTest);
		
		IContextTaskLoader contextTask=(IContextTaskLoader)SpringContextUtil.getBean(taskBeanName);
		RetResult<String> ret=contextTask.execute(this.event);
		log4.info("-------contextTask-------"+ret.isSuccess()+" msgBody="+ret.getMsgBody());
	}
	
	
}
