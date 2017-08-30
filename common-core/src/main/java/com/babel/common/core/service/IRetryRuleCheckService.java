package com.babel.common.core.service;

import com.babel.common.core.data.RetResult;

/**
 * 重试规则检查
 * @author 金和
 *
 */
public interface IRetryRuleCheckService {

	/**
	 * 检查重试次数
	 * @param ruleCode
	 * @param redisTemplate
	 * @param userName
	 * @param ip
	 * @return
	 */
	public RetResult<String> checkLoginRetryCount(String funcRetryCode
			, String userName, String ip);
	
	/**
	 * 清除重试的缓存，用于重试成功后清理
	 * @param ruleCode
	 * @param redisTemplate
	 */
	public void cleanRetry(String funcRetryCode,String userName, String ip) ;

	

}