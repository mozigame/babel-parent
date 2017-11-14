package com.babel.common.web.loader;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.babel.common.core.data.RetData;
import com.babel.common.core.util.CommUtil;

/**
 * 
 * @author jinhe.chen
 *
 */
@Aspect
@Component
public class RestControllerAspect {
	private static final Logger logger = Logger.getLogger(RestControllerAspect.class);

	@Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
	public void restPointcut() {
	}

	@Pointcut("execution(* *(..))")
	public void anyMethodPointcut() {

	}

	private static List<String> ignoreClassList=new ArrayList(); 
	private static Map<String, Object> retDataMap=new HashMap<>();
	static{
		retDataMap.put("err", "SUCCESS");
		retDataMap.put("msg", "");
		ignoreClassList=CommUtil.newList("LogsResource","ProfileInfoResource");
	}
	private synchronized Map<String, Object> getRetDataMap(){
		return retDataMap;
	}
//	@AfterReturning(value = "restPointcut() && anyMethodPointcut()", returning = "response")
//	public Object wrapResponse(Object response) {
//		System.out.println("---response class="+response.getClass());
//		if(response!=null && response instanceof ResponseEntity){
//			ResponseEntity entity=(ResponseEntity)response;
//			Object body=entity.getBody();
//			System.out.println("---body="+body.getClass());
//			if(body instanceof RetData){
//				return response;
//			}
//			else{
////				retData.setData(body);
//				getRetDataMap().put("data", body);
//				return new ResponseEntity<>(getRetDataMap(), entity.getStatusCode());
//			}
//		}
//		else if(!(response instanceof RetData)){
////			retData.setData(response);
//			getRetDataMap().put("data", response);
//			System.out.println("---retDataMap="+getRetDataMap());
//			return getRetDataMap();
//		}
//		return response;
//	}

	@Around(value = "restPointcut() && anyMethodPointcut()")
	public Object wrapResponse(ProceedingJoinPoint pjp) throws Throwable{
		Object response=pjp.proceed();
		String className=pjp.getTarget().getClass().getSimpleName();
		if(ignoreClassList.contains(className)){
			return response;
		}
//		System.out.println("---response class="+response.getClass());
		if(response!=null && response instanceof ResponseEntity){
			ResponseEntity entity=(ResponseEntity)response;
			Object body=entity.getBody();
//			System.out.println("---body="+body.getClass());
			if(body instanceof RetData){
				return response;
			}
			else{
//				retData.setData(body);
				getRetDataMap().put("data", body);
				return new ResponseEntity<>(getRetDataMap(), entity.getStatusCode());
			}
		}
		else if(!(response instanceof RetData)){
			MethodSignature ms = (MethodSignature) pjp.getSignature();
			Method method = ms.getMethod();
			
			logger.warn("-----please return with:ResponseEntity("+pjp.getTarget().getClass().getSimpleName()+"."+method.getName()+"), exp:public ResponseEntity<OddsItemPO> getOdds(){}");
			return response;
		}
		return response;
	}

}
