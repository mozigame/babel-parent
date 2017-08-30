package com.babel.common.web.loader;



import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.babel.common.core.data.RetResult;
import com.babel.common.core.exception.RetException;
import com.babel.common.web.context.AppContext;



/** 
 * 捕获异常统一处理 
 * @description TODO  
 * @create date 2016年4月28日 
 * @modified by  
 * @modify date 
 * @version v1.0 
 */  
@ControllerAdvice  
public class GlobalExceptionHandler {  
      
	private final static Logger log4 = Logger.getLogger(GlobalExceptionHandler.class);
      
    private final static String EXPTION_MSG_KEY = "message";  
    private RetResult ret=new RetResult();
    @ExceptionHandler(Exception.class)  
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)  
    @ResponseBody
    public RetResult handleBizExp(HttpServletRequest request, Exception ex){  
    	log4.error("---handleBizExp--req="+this.getRequestMap(request)+"\n error=" + ex.getMessage(), ex);  
//        request.getSession(true).setAttribute(EXPTION_MSG_KEY, ex.getMessage());  
//        ModelAndView mv = new ModelAndView();  
//        mv.addObject("message", ex.getMessage());  
//        mv.setViewName("error");
    	ret.initError(RetResult.msg_codes.ERR_UNKNOWN, ex.getMessage(), ex);
    	ret.setFlag(9);//controll异常拦截9
        return ret;  
    }  
    
    @ExceptionHandler(RetException.class)  
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)  
    @ResponseBody
    public RetResult handleRetExp(HttpServletRequest request, RetException ex){  
    	log4.error("---handleRetExp--req="+this.getRequestMap(request)+"\n error=" + ex.getMessage(), ex);
    	ex.getRetResult().setFlag(9);//controll异常拦截9
//        request.getSession(true).setAttribute(EXPTION_MSG_KEY, ex.getMessage());  
//        ModelAndView mv = new ModelAndView();  
//        mv.addObject("message", ex.getMessage());
//        RetResult ret=ex.getRetResult();
//        mv.addObject("flag", ret.getFlag());
//        mv.addObject("msgCode", ret.getMsgCode());
//        mv.addObject("msgBody", ret.getMsgBody());
//        mv.addObject("data", ret.getFirstData());
//        mv.setViewName("errorRet");  
        return ex.getRetResult();  
    }  
//      
//    @ExceptionHandler(SQLException.class)  
//    public ModelAndView handSql(Exception ex){  
//    	log4.info("SQL Exception " + ex.getMessage());  
//        ModelAndView mv = new ModelAndView();  
//        mv.addObject("message", ex.getMessage());  
//        mv.setViewName("sql_error");  
//        return mv;  
//    }  
    
    private Map<String, Object> getRequestMap(HttpServletRequest req){
    	Map<String, Object> reqMap=new HashMap<String, Object>();
    	if(req==null){
    		reqMap.put("request", null);
    		return reqMap;
    	}
    	reqMap.put("sessionId", req.getSession().getId());
    	 //获得request 相关信息  
        String contextpath=req.getContextPath();
        reqMap.put("contextpath", contextpath);
        String characterEncoding = req.getCharacterEncoding();
        reqMap.put("characterEncoding", characterEncoding);
        String contentType = req.getContentType();
        reqMap.put("contentType", contentType);
        String method = req.getMethod();  
        reqMap.put("method", method);
        reqMap.put("parameterMap", req.getParameterMap());
        String protocol = req.getProtocol();  
        reqMap.put("protocol", protocol);
        String serverName = req.getServerName();
        reqMap.put("serverName", serverName);
//        Cookie[] cookies = req.getCookies();  
//        reqMap.put("cookies", cookies);
        String servletPath = req.getServletPath();
        reqMap.put("servletPath", servletPath);
        reqMap.put("remoteAddr", AppContext.getIpAddr(req));
        String requestURI = req.getRequestURI();
        reqMap.put("requestURI", requestURI);
       
        return reqMap;
        
    }
  
}  