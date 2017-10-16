package com.babel.common.web.loader;

import com.alibaba.fastjson.JSON;
import com.babel.common.core.util.*;
import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * Created by zjh on 2017/1/10.
 */
@Aspect
@Component
public class AresLogAspect {

    private static final Logger logger = Logger.getLogger(AresLogAspect.class);
    private static final String SPLIT = "\t";
    private final RequestIDGenerator reqId = DefaultRequestIdGenerator.getInstance();
    private RequestLogRecord record;
    private long spendTime = 0;
    @Autowired
    private HttpServletResponse response;

    @Pointcut("(execution(public * com.babel.*.web.rest.order.*Resource.*(..))) " +
        "|| (execution(public * com.babel.*.web.rest.*Resource.*(..))) " +
        "|| (execution(public * com.babel.*.web.rest.feign.*Resource.*(..)))" )
    public void webLog() {
    }

    @Before("webLog()")
    public void doBeforeReq() {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        spendTime = System.currentTimeMillis();
        record = new RequestLogRecord();
        record.setDate(new Date());
        record.setRequestId(reqId.nextId());
        record.setReqUri(request.getRequestURI());
        record.setMethod(request.getMethod());
        record.setIp(IPUtil.getRealIpAddr(request));
        record.setParameters(request.getParameterMap());
    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReq(Object ret) throws Throwable {
        // 记录下请求内容
        StringBuffer buf = new StringBuffer()
                .append(DateUtils.format(record.getDate(), "yyyy-MM-dd HH:mm:ss,S"))
                .append(SPLIT)
                .append(record.getRequestId())
                .append(SPLIT)
                .append(record.getReqUri())
                .append(SPLIT)
                .append(record.getMethod())
                .append(SPLIT)
                .append(System.currentTimeMillis() - spendTime)
                .append(SPLIT)
                .append(response.getStatus())
                .append(SPLIT)
                .append(buildParameterString(record.getParameters()))
                .append(SPLIT)
                .append(record.getIp())
                .append(SPLIT)
                .append(JSON.toJSONString(ret));
        // 处理完请求，返回内容
        logger.info(buf.toString());
        clearParam();
    }

    private void clearParam() {
        record = null;
        spendTime = 0;
    }


//    @Around("execution(public * com.youyun.customer.controller.*.*(..))")
//    public void check(ProceedingJoinPoint point) throws Throwable {
//        System.out.println(response.getStatus());
//        System.out.println(response.getBufferSize());
//        System.out.println(response.getOutputStream().toString());
//        System.out.println(">>>>>>>>>>>>>>>>>>>>> around");
//        MethodSignature signature = (MethodSignature) point.getSignature();
//        //获取被代理方法的类名
//        String className = signature.getDeclaringTypeName();
//        //获取被代理的方法名
//        Method method = signature.getMethod();
//        String methodName = method.getName();
//        //通过方法参数获取到session，拿到用户信息
//        for (Object arg : point.getArgs()) {
//            if (arg instanceof HttpServletResponse) {
////                HttpServletResponse response = point.get;
////                System.out.println(response.getStatus());
////                break;
//            } else if (arg instanceof HttpServletRequest) {
//                // 接收到请求，记录请求内容
//                HttpServletRequest request = (HttpServletRequest) arg;
//                spendTime = System.currentTimeMillis();
//                record = new RequestLogRecord();
//                record.setDate(new Date());
//                record.setRequestId(reqId.nextId());
//                record.setReqUri(request.getRequestURI());
//                record.setMethod(request.getMethod());
//                record.setIp(IPUtils.getRealIpAddr(request));
//                record.setParameters(request.getParameterMap());
//            }
//        }
//    }

    private String buildParameterString(Map<String, String[]> parameters) {
        if (parameters != null) {
            StringBuilder paramBuf = new StringBuilder();
            for (Map.Entry<String, String[]> e : parameters.entrySet()) {
                String key = e.getKey();
                String[] values = e.getValue();
                for (String value : values) {
                    paramBuf.append(key).append("=").append(URLEncodeUtils.encodeURL(value));
                    paramBuf.append("&");
                }
            }
            if (paramBuf.length() > 0 && paramBuf.charAt(paramBuf.length() - 1) == '&') {
                paramBuf.deleteCharAt(paramBuf.length() - 1);
            }
            return paramBuf.toString();
        }
        return null;
    }

}
