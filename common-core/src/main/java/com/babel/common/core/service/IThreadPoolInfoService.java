package com.babel.common.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.core.task.TaskExecutor;

import com.babel.common.core.entity.IPoolInfoVO;

public interface IThreadPoolInfoService {
	public Map<String, Map<String, Object>> getPoolInfoMap();
	
	public int savePoolInfo(String sysType, TaskExecutor taskExecutor, IPoolInfoVO poolInfoVO);
	
	/**
	 * 按系统类型及修改时间查线程池数据
	 * @param sysType
	 * @param modifyDate 条件为：>=modifyDate
	 * @return
	 */
	public List<IPoolInfoVO> findThreadPoolList(String sysType, Date modifyDate);
}
