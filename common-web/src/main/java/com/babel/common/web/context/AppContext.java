package com.babel.common.web.context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import com.babel.common.core.util.CommUtil;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;

import com.babel.common.core.data.IUser;
import com.babel.common.core.entity.UserPermitVO;
import com.babel.common.core.util.RedisUtil;

public class AppContext {
	private static final Logger logger = Logger.getLogger(AppContext.class);
	public static final String SESSION_SECURITY_CODE = "sessionSecCode";//验证码
    public static final String SESSION_USER = "sessionUser";			//session用的用户
    public static final String SESSION_USERID = "sessionUserId";			//session用的用户
    public static final String SESSION_USERNAME = "sessionUserName";			//session用的用户
    public static final String SESSION_USERIP = "sessionUserIp";			//session用的用户
    public static final String SESSION_USERROL = "USERROL";				//用户对象
    public static final String SESSION_SAVED_REQUEST_KEY="shiroSavedRequest";//shiro在跳转前会把跳转过来的页面链接
    
    
    private static Map<String, Map<String, Object>> contextMap=new HashMap<>();
    private static Map<String,Object> envMap=new HashMap<>();
	public static Map<String, Object> getEnvMap(){
		return envMap;
	}
	public static void putEnvMap(Map<String, Object> envMap){
		envMap.putAll(envMap);
	}
	

	public Map<String, Object> getUserEnvMap(Long userId){
//		return SysconfigUsers.getUserEnvMap(userId);
		return null;
	}
//    public static void set(String key, Object obj){
//    	String mapKey="trd_"+Thread.currentThread().getId();
//    	Map<String, Object> map=contextMap.get(mapKey);
//    	if(map==null){
//    		map=new HashMap<String, Object>();
//    		contextMap.put(mapKey, map);
//    	}
////    	System.out.println("-----set--mapKey="+mapKey+" key="+key+" obj="+obj);
//    	map.put(key, obj);
//    	
//    }
//    
//    public static Object get(String key){
//    	String mapKey="trd_"+Thread.currentThread().getId();
//    	
//    	Map<String, Object> map=contextMap.get(mapKey);
////    	System.out.println("-----get--"+mapKey+" map="+map);
//    	if(map!=null){
//    		return map.get(key);
//    	}
//    	return null;
//    }
    
    
    /**获取当前登录的用户名
	 * @return
	 */
	public static String getUsername(){
		String userName=null;
		
		try {
			userName= (String)SecurityUtils.getSubject().getSession().getAttribute(SESSION_USERNAME);
		} catch (InvalidSessionException e) {
			logger.warn("----session--getUsername:"+e.getMessage());
		}
		return userName;
	}
	
	public static Long getCurrentUserId(){
		Long userId=null;
		try {
			userId=(Long)SecurityUtils.getSubject().getSession().getAttribute(SESSION_USERID);
		} catch (InvalidSessionException e) {
			logger.warn("----session--getCurrentUserId:"+e.getMessage());
		}
		return userId;
	}
	
	public static IUser getCurrentUser(){
		return (IUser)SecurityUtils.getSubject().getSession().getAttribute(SESSION_USER);
	}
	
	public static String getPreBasePath(HttpServletRequest request) {
		String serverName = request.getServerName();
		String port = "";
		port = ":"+request.getServerPort();
		return request.getScheme()+"://"+serverName+port;
	}
	
	public static String getRemoteIp(HttpServletRequest request){
		String ip = "";
		if (request.getHeader("x-forwarded-for") == null) {  
			ip = request.getRemoteAddr();  
	    }else{
	    	ip = request.getHeader("x-forwarded-for");  
	    }
		return ip;
	}
	
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if("127.0.0.1".equals(ip) ||"0:0:0:0:0:0:0:1".equals(ip)){
			ip="localhost";
		}
		String[] ipStrings=ip.split(",");
		if(ipStrings.length>1){
			ip=ipStrings[0];
		}
		return ip;
	}
	
	/**
	 * 获取shiro在跳转前会把跳转过来的页面链接
	 * @param request
	 * @return
	 */
	public static SavedRequest getSavedRequest(ServletRequest request) {  
	    SavedRequest savedRequest = null;  
	    Subject subject = SecurityUtils.getSubject();  
	    Session session = subject.getSession(false);  
	    if (session != null) {  
	        savedRequest = (SavedRequest) session.getAttribute(SESSION_SAVED_REQUEST_KEY);
	    }  
	    return savedRequest;  
	}
	
	/**
	 * {sessionId, userPermit}
	 */
	private static Map<String, UserPermitVO> sessionUserMap=new ConcurrentHashMap<>();
	
	/**
	 * {roleIds, moduleSet}
	 * 特例：allModuleUrl,moduleSet
	 */
	private static Map<String, Set<String>> roleModuleMap=new ConcurrentHashMap<>();
	private static Map<String, List> roleModuleListMap=new ConcurrentHashMap<>();
	private static final String REDIS_KEY_SESSION_PERMIT="redisSessionPermit";
	private static final String REDIS_KEY_ROLE_MODULE_URL="redisRoleModuleUrl";
	private static final String REDIS_KEY_ROLE_MODULE_LIST="redisRoleModuleList";
	public static final String ROLE_ALL_MODULE_URL="allModuleUrl";
	public static void putUserInfo(String sessionId, UserPermitVO user){
//		sessionUserMap.put(sessionId, user);
//		RedisTemplate redisTemplate=RedisUtil.getRedisTemplate();
//		if(redisTemplate!=null){
//			redisTemplate.opsForHash().put(REDIS_KEY_SESSION_PERMIT, sessionId, user);
//		}
		RedisUtil.putRedis(sessionUserMap, REDIS_KEY_SESSION_PERMIT, sessionId, user);
	}
	public static UserPermitVO getUserInfo(String sessionId){
//		if(StringUtils.isEmpty(sessionId)){
//			return null;
//		}
//		
//		UserPermitVO user= sessionUserMap.get(sessionId);
//		if(user==null){
//			RedisTemplate redisTemplate=RedisUtil.getRedisTemplate();
//			if(redisTemplate!=null){
//				user=(UserPermitVO)redisTemplate.opsForHash().get(REDIS_KEY_SESSION_PERMIT, sessionId);
//				if(user!=null){
//					sessionUserMap.put(sessionId, user);
//				}
//			}
//		}
		UserPermitVO user=(UserPermitVO)RedisUtil.getRedis(sessionUserMap, REDIS_KEY_SESSION_PERMIT, sessionId);
		return user;
	}
	
	
	
	
	
	public static void putRoleModule(String roleIdStr, Set<String> moduleSet){
//		roleModuleMap.put(roleIdStr, moduleSet);
//		RedisTemplate redisTemplate=RedisUtil.getRedisTemplate();
//		if(redisTemplate!=null){
//			redisTemplate.opsForHash().put(REDIS_KEY_ROLE_MODULE_URL, roleIdStr, moduleSet);
//		}
		RedisUtil.putRedis(roleModuleMap, REDIS_KEY_ROLE_MODULE_URL, roleIdStr, moduleSet);
	}
	
	public static void putRoleModuleList(String roleIdStr, String mPidStr, List moduleList){
		String key=roleIdStr+"_"+mPidStr;
//		roleModuleListMap.put(key, moduleList);
//		RedisTemplate redisTemplate=RedisUtil.getRedisTemplate();
//		if(redisTemplate!=null){
//			redisTemplate.opsForHash().put(REDIS_KEY_ROLE_MODULE_LIST, key, moduleList);
//		}
		RedisUtil.putRedis(roleModuleListMap, REDIS_KEY_ROLE_MODULE_LIST, key, moduleList);
	}
	
	public static List getRoleModuleList(String roleIdStr, String mPidStr){
		String key=roleIdStr+"_"+mPidStr;
//		List moduleList= roleModuleListMap.get(key);
//		if(moduleList==null){
//			RedisTemplate redisTemplate=RedisUtil.getRedisTemplate();
//			if(redisTemplate!=null){
//				moduleList=(List)redisTemplate.opsForHash().get(REDIS_KEY_ROLE_MODULE_LIST, key);
//				if(moduleList!=null){
//					roleModuleListMap.put(key, moduleList);
//				}
//			}
//		}
		List moduleList=(List)RedisUtil.getRedis(roleModuleListMap, REDIS_KEY_ROLE_MODULE_LIST, key);
		return moduleList;
	}
	
	public static Set<String> getRoleModule(String roleIdStr){
//		Set<String> moduleUrlSet= roleModuleMap.get(roleIdStr);
//		if(moduleUrlSet==null){
//			RedisTemplate redisTemplate=RedisUtil.getRedisTemplate();
//			if(redisTemplate!=null){
//				moduleUrlSet=(Set<String>)redisTemplate.opsForHash().get(REDIS_KEY_ROLE_MODULE_URL, roleIdStr);
//				if(moduleUrlSet!=null){
//					roleModuleMap.put(roleIdStr, moduleUrlSet);
//				}
//			}
//		}
//		return moduleUrlSet;
		return (Set<String>)RedisUtil.getRedis(roleModuleMap, REDIS_KEY_ROLE_MODULE_URL, roleIdStr);
	}

}
