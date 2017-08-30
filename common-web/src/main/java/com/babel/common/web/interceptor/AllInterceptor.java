package com.babel.common.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
  
public class AllInterceptor extends HandlerInterceptorAdapter implements HandlerInterceptor {

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object obj, Exception e)
			throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object obj, ModelAndView mv)
			throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
//		Object customerObj = request.getSession().getAttribute("customerSession");
//		if(customerObj == null) {
//			response.sendRedirect("/index");
//			return false;
//		} else {
//			return true;
//		}
		System.out.println("----request="+request.getContextPath()+" ip="+request.getRemoteHost());
		return true;
	}  
      
} 