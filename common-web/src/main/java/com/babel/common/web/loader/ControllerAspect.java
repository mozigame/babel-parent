package com.babel.common.web.loader;

import java.lang.reflect.Method;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.babel.common.core.logger.LogAudit;
import com.babel.common.web.context.AppContext;
import com.babel.common.web.context.JobContext;

/**
 * 
 * @author jinhe.chen
 *
 */
@Aspect
@Component
public class ControllerAspect {
	private static final Logger logger = Logger.getLogger(ControllerAspect.class);
	


	 //只对Controller进行pointcut  
    @Pointcut("within(@org.springframework.stereotype.Controller *)")  
    public void controllerPointcut() {  
  
    }
	/**
	 * 在所有标注@Log的地方切入
	 * 
	 * @param joinPoint
	 */
	@Before("controllerPointcut()")
	public void beforeExec(JoinPoint joinPoint) {
		String userName=AppContext.getUsername();
		if(userName==null){
			return;
		}
		
		MethodSignature ms = (MethodSignature) joinPoint.getSignature();
		Method method = ms.getMethod();
		//排除已加过@LogAudit注解的方法，因为@LogAudit有同样的处理
		LogAudit logAudit = method.getAnnotation(LogAudit.class);
		if(logAudit!=null){
			return;
		}
		
		//每次请求前向dubbo服务隐式传用户的userName,userId
//		RpcContext.getContext().setAttachment("userName", userName);
//		RpcContext.getContext().setAttachment("uuid", UUID.randomUUID().toString());
//		Long userId=AppContext.getCurrentUserId();
//		if(userId!=null){
//			RpcContext.getContext().setAttachment("userId", ""+userId);
//		}
		
		Long userId=AppContext.getCurrentUserId();
		JobContext.getContext().setAttachment("jobType", JobContext.JOB_TYPE_CONTROLLER);
		JobContext.getContext().setAttachment("userName", userName);
		JobContext.getContext().setAttachment("uuid", UUID.randomUUID().toString());
		JobContext.getContext().setAttachment("remoteHost", SecurityUtils.getSubject().getSession().getHost());
		if(userId!=null){
			JobContext.getContext().setAttachment("userId", userId);
		}
		
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		if(request!=null){
			JobContext.getContext().setAttachment("remoteIp", AppContext.getIpAddr(request));
		}
	}
	
	@After("controllerPointcut()")
	public void afterExec(JoinPoint joinPoint) {
		JobContext.removeContext();
	}
	



}
