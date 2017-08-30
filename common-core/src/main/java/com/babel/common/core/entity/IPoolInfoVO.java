package com.babel.common.core.entity;

public interface IPoolInfoVO {
	public String getCode();

	public String getName();

	public Integer getCorePoolSize();

	public Integer getMaxPoolSize();

	public Integer getQueueCapacity();

	public Integer getKeepAliveSeconds();

	public Integer getAllowCoreThreadTimeOut();
	
	public Integer getLimitTime();
}
