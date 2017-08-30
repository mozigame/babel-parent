package com.babel.common.core.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.babel.common.core.security.util.MD5;

/**
 * 生成令牌工具类
 * 
 * @description 描述： 用于生成进行身份认证的唯一令牌，存入到指定时间的对象中。<br/>
 * @author 九亲－姚臣伟
 * @version 1.0
 * @since jdk1.6+
 */
public class TokenUtil {
	public static long MAX_TOKEN_TIME=120;
	/**
	 * 生成令牌方法
	 * 
	 * @return 返回令牌字符串
	 */
	public static String buildToken() {
		String token = UUID.randomUUID().toString();
		return token.replace("-", "").toUpperCase();
	}

	/**
	 * 获取Token令牌，Map结构的数据，包含未加密的令牌、加密后的令牌、创建令牌时间
	 * 
	 * @return 返回生成的令牌数据
	 */
	public static Map<String, String> getToken() {
		Map<String, String> tokenMap = new HashMap<String, String>(0);

		String token = buildToken();
		tokenMap.put("srcToken", token);
		MD5 md5 =new MD5();
		
		Random ra =new Random();
		Integer rnd=ra.nextInt(10000);
		String tokenEncrypt=md5.getMD5ofStr(token).toUpperCase();
		if(tokenEncrypt.length()>30){
			tokenEncrypt=tokenEncrypt.substring(0, 30);
		}
		tokenEncrypt+="_"+rnd;
		tokenMap.put("encryptToken", tokenEncrypt);
		tokenMap.put("dateString",DateUtils.UDateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));

		return tokenMap;
	}
	
	/**
	 * 验证Token令牌数据是否有效
	 * 
	 * @param request         请求对象
	 * @param tokenKey        用户登录名，作为令牌的Key
	 * @param inSrcToken      用户请求时的令牌字符串
	 * @param inEncrytpToken  用户请求时的加密令牌字符串
	 * @return 返回true或false
	 */
	@SuppressWarnings("unchecked")
	public static boolean validateToken(HttpServletRequest request,
			String tokenKey, String inSrcToken, String inEncrytpToken) {
		HttpSession session = request.getSession();

		Map<String, String> tokenMap = new HashMap<String, String>(0);

		if (session.getAttribute(tokenKey) != null) {
			tokenMap = (Map<String, String>) session.getAttribute(tokenKey);
		} else {
			return false;
		}

		if (tokenMap.isEmpty()) {
			return false;
		}

		String srcToken     = tokenMap.get("srcToken");     // 未加密的Token
		String encryptToken = tokenMap.get("encryptToken"); // 加密后的Token
		
		if (srcToken == null || "".equals(srcToken.trim())) {
			return false;
		}
		
		if (encryptToken == null || "".equals(encryptToken.trim())) {
			return false;
		}
		
		if (srcToken.equals(inSrcToken) && encryptToken.equals(inEncrytpToken)) {
			Date now = new Date();
			String dateStr=(String)tokenMap.get("dateString");
			Date date = DateUtils.convert(dateStr);
			
			long minutes = (now.getTime()-date.getTime())/60000;
			
			if (minutes > MAX_TOKEN_TIME) {
				return false;
			} else {
				return true;
			}
		}

		return false;
	}

}