package com.babel.common.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.babel.common.core.data.IUser;
import com.babel.common.core.entity.BaseEntity;
import com.babel.common.core.security.util.UuidUtil;
import com.babel.common.web.context.AppContext;

/**
 * 
 * @author cjh
 *
 */
public abstract class BaseController {

	private static Logger logger = Logger.getLogger(BaseController.class);

	private static final long serialVersionUID = 6357869213649815390L;
	

	/**
	 * 得到ModelAndView
	 * 
	 * @return
	 */
	public ModelAndView getModelAndView() {
		return new ModelAndView();
	}

	/**
	 * 得到request对象
	 * 
	 * @return
	 */
	public HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		return request;
	}
	

	/**
	 * 得到32位的uuid
	 * 
	 * @return
	 */
	public String get32UUID() {
		return UuidUtil.get32UUID();
	}

	public static void logBefore(Logger logger, String interfaceName) {
		logger.info("");
		logger.info("start");
		logger.info(interfaceName);
	}
	
	protected void initCreate(BaseEntity obj){
		obj.initCreate();
		obj.setCreateUser(AppContext.getCurrentUserId());
		obj.setModifyUser(AppContext.getCurrentUserId());
	}
	

	
	protected void initUpdate(BaseEntity obj){
		obj.initUpdate();
		obj.setModifyUser(AppContext.getCurrentUserId());
	}

	public static void logAfter(Logger logger) {
		logger.info("end");
		logger.info("");
	}
	
	public Long getCurrentUserId(){
		return AppContext.getCurrentUserId();
	}
	
	
	public IUser getCurrentUser(){
		return AppContext.getCurrentUser();
	}

}
