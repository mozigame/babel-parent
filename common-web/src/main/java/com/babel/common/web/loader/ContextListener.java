package com.babel.common.web.loader;



import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.babel.common.core.util.CommMethod;
import com.babel.common.core.util.TimeLogger;
import com.babel.common.web.constants.ConfigWebUtil;







/**
 * 启动显示数据库信息，并做连接测试
 * @author chenjh 239928
 *
 */  

public class ContextListener implements ServletContextListener {
	private static Logger log4 = Logger.getLogger(ContextListener.class);
	public void contextInitialized(ServletContextEvent event) {
		TimeLogger timeLogger = new TimeLogger(log4, "contextInitialized");
//		String protocol=ServletActionContext.getRequest().getProtocol();
		String contextTaskBeanName=ConfigWebUtil.getConfigValue("contextTaskBeanName");
		log4.info("-------serverInfo="+event.getServletContext().getServerInfo()+" contextTaskBeanName="+contextTaskBeanName);
//		String contextPath=event.getServletContext().getContextPath();
//		log4.info("-------contextPath="+contextPath);
		
		if(!CommMethod.isEmpty(contextTaskBeanName)) {
			
			String[] taskNames = contextTaskBeanName.split(",");
			Thread t1 = new Thread(new ContextTaskThread("shiroFilterChainLoader", event));t1.start();
			for(String task:taskNames) {
				t1=new Thread(new ContextTaskThread(task, event));t1.start();
				timeLogger.time(task);
			}
		}
		
		timeLogger.printTime(this, "initital finished");
		
	}	
	
	
	

	public void contextDestroyed(ServletContextEvent event) {
//		Thread t1 = null;
//		t1=new Thread(new ContextTaskThread("wsUnRegist", null));
//		t1.start();
		log4.info("----application stop--");
	}
	
	
	

	
}