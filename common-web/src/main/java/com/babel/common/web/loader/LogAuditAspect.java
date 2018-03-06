package com.babel.common.web.loader;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.babel.common.core.logger.ILogManager;
import com.babel.common.core.logger.LogAudit;
import com.babel.common.core.util.ObjectToMapUtil;
import com.babel.common.web.context.AppContext;
import com.babel.common.web.context.JobContext;
import com.google.gson.Gson;

@Aspect
@Component
public class LogAuditAspect {
	private static final Logger logger = Logger.getLogger(LogAuditAspect.class);
	
	@Autowired
	private ILogManager logManager;
	

	ThreadLocal<Long> time = new ThreadLocal<Long>();
	ThreadLocal<String> tag = new ThreadLocal<String>();

	// @Pointcut("execution(* com.babel..*.*(..))")
	/**
	 * 对有@LogAudit标记的方法,记录其执行参数及返回结果.
	 */
	@Pointcut("@annotation(com.babel.common.core.logger.LogAudit)")
	public void log() {
		System.out.println("我是一个切入点");
	}

	/**
	 * 在所有标注@Log的地方切入
	 * 
	 * @param joinPoint
	 */
	@Before("log()")
	public void beforeExec(JoinPoint joinPoint) {
		time.set(System.currentTimeMillis());
		tag.set(UUID.randomUUID().toString());
//		logger.info("-----tag="+tag.get());
	}
	
	
	@After("log()")
	public void afterExec(JoinPoint joinPoint) {
		MethodSignature ms = (MethodSignature) joinPoint.getSignature();
		Method method = ms.getMethod();
//		Map<String, Object> pointMap=getPointMap(joinPoint);
//		
//		logger.info("-----time="+(System.currentTimeMillis() - time.get())+"ms  pointMap="+pointMap);
		
		String jobType=(String)JobContext.getContext().getAttachment("jobType");
		String jobMethod=(String)JobContext.getContext().getAttachment("jobMethod");
		if(!JobContext.JOB_TYPE_LOG_AUDIT.equals(jobType)){
			if(method.getName().equals(jobMethod)){
				JobContext.removeContext();
			}
		}
	}
	
	@AfterThrowing(pointcut = "log()", throwing = "e")  
    public  void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
//		long time = System.currentTimeMillis();
//		MethodSignature ms = (MethodSignature) joinPoint.getSignature();
//		Method method = ms.getMethod();
//		
//		Map<String, String> pointMap = getPointMap(joinPoint);
//		LogAudit logAudit = method.getAnnotation(LogAudit.class);
//		
//
//		long runTime=System.currentTimeMillis() - time;
//		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
//				.getRequest();
//		this.logManager.addLogErrorAsync(logAudit, pointMap, runTime, e, request);
	}

	@Around("log()")
	public Object aroundExec(ProceedingJoinPoint pjp) throws Throwable {
		long time = System.currentTimeMillis();

		MethodSignature ms = (MethodSignature) pjp.getSignature();
		Method method = ms.getMethod();
		
		Map<String, Object> pointMap = getPointMap(pjp);
		pointMap.put("threadId",Thread.currentThread().getId());
		LogAudit logAudit = method.getAnnotation(LogAudit.class);
		
		String jobType=(String)JobContext.getContext().getAttachment("jobType");
		boolean isAudit=false;
		if(!JobContext.JOB_TYPE_CONTROLLER.equals(jobType)){//如果在controller已初始化过，不用再初始化了
			JobContext.getContext().setAttachment("jobType", JobContext.JOB_TYPE_LOG_AUDIT);
			JobContext.getContext().setAttachment("jobMethod", method.getName());
			System.out.println("----method.name="+method.getName());
			isAudit=true;
		}
		String userName=AppContext.getUsername();
		if(userName!=null){
			Long userId=AppContext.getCurrentUserId();
			pointMap.put("userName", userName);
			if(isAudit && userId!=null){
				JobContext.getContext().setAttachment("userId", userId);
			}
		}
		
		//logAudit
		pointMap.put("logAudit.title", logAudit.title());
		pointMap.put("logAudit.author", logAudit.author());
		pointMap.put("logAudit.calls", logAudit.calls());
		pointMap.put("logAudit.descs", logAudit.descs());
		
		//每次请求前向dubbo服务隐式传用户的userName,userId
		String uuid=(String)JobContext.getContext().getAttachment("uuid");
		HttpServletRequest request=null;
		if(isAudit){
			uuid=UUID.randomUUID().toString();
			JobContext.getContext().setAttachment("userName", userName);
			JobContext.getContext().setAttachment("uuid", uuid);
			request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
					.getRequest();
			JobContext.getContext().setAttachment("remoteIp", AppContext.getIpAddr(request));
		}
		pointMap.put("remoteIp", (String)JobContext.getContext().getAttachment("remoteIp"));
		pointMap.put("uuid", uuid);
		
		Object retObj=null;
		String methodCode=(String)pointMap.get("method");
		
		try {
			retObj = pjp.proceed();
			long runTime=System.currentTimeMillis() - time;
			logger.debug("----addLogInfoAsync----method="+pointMap.get("method")+" runTime="+runTime);
//			this.logManager.addLogInfoAsync(methodCode, pointMap, runTime, retObj, request);
		} catch (Exception e) {
			long runTime=System.currentTimeMillis() - time;
//			this.logManager.addLogErrorAsync(methodCode, pointMap, runTime, e, request);
			throw e;
		}
		return retObj;
	}

	private Map<String, Object> getPointMap(JoinPoint joinPoint) {
		Map<String, Object> infoMap = new HashMap<>();
		infoMap.put("kind", joinPoint.getKind());
		infoMap.put("target", joinPoint.getTarget().toString());
		infoMap.put("args", this.getArgs(joinPoint.getArgs()));
		infoMap.put("signature", ""+joinPoint.getSignature());
		infoMap.put("method", ""+joinPoint.getSignature().getName());
		infoMap.put("sourceLocation", ""+joinPoint.getSourceLocation());
		infoMap.put("staticPart", ""+joinPoint.getStaticPart());
		return infoMap;
	}
	
	private static Gson gson = new Gson();

	private Map<String, Object> getArgs(Object[] args) {
		Map<String, Object> map=new HashMap<String, Object>();
//		String info = "";
		Object arg=null;
		for (int i = 0; i < args.length; i++) {
			arg=args[i];
			if(arg!=null){
				if(!(arg instanceof String || arg instanceof Number 
						|| arg instanceof Date)){
					try {
						arg=ObjectToMapUtil.objectToMap(arg);
					} catch (Exception e) {
						logger.warn("----getArgs--"+arg+","+e.getMessage());
//						arg=gson.toJson(arg);
					}
				}
				
			}
			map.put(""+i, arg);
		}
		return map;
	}

}
