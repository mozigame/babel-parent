package com.babel.common.core.service;

import com.babel.common.core.data.RetResult;

/**
 * 白名单检查
 * @author 金和
 *
 */
public interface IWhiteListCheckService {
	/**
	 * 检查数据是否允许使用
	 * @param whiteType 1白名单，2黑名单
	 * @param dataType 
	 * @param data
	 * @return
	 */
	public RetResult<Boolean> check(int dataType, String data);
}
