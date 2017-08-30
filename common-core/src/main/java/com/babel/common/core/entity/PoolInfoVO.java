package com.babel.common.core.entity;


import java.io.Serializable;
import java.util.Properties;

public class PoolInfoVO implements IPoolInfoVO, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;
	private String name;
	private Integer corePoolSize;
	private Integer maxPoolSize;
	private Integer queueCapacity;
	private Integer keepAliveSeconds;
	private Integer allowCoreThreadTimeOut;
	private Integer limitTime;
	
	public void load(Properties properties){
		this.setAllowCoreThreadTimeOut(0);
		this.setCorePoolSize(getInt(properties.get("corePoolSize"), 2));
		this.setMaxPoolSize(getInt(properties.get("maxPoolSize"), 50));
		this.setQueueCapacity(getInt(properties.get("queueCapacity"), 100));
		this.setKeepAliveSeconds(getInt(properties.get("keepAliveSeconds"), 300));
		this.setLimitTime(getInt(properties.get("limitTime"), 0));
		
	}
	private static Integer getInt(Object obj, Integer defaultValue){
		if(obj==null||"".equals(obj)){
			return defaultValue;
		}
		else if(obj instanceof Integer){
			return (Integer)obj;
		}
		return Integer.parseInt(""+obj);
	}
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the corePoolSize
	 */
	public Integer getCorePoolSize() {
		return corePoolSize;
	}
	/**
	 * @param corePoolSize the corePoolSize to set
	 */
	public void setCorePoolSize(Integer corePoolSize) {
		this.corePoolSize = corePoolSize;
	}
	/**
	 * @return the maxPoolSize
	 */
	public Integer getMaxPoolSize() {
		return maxPoolSize;
	}
	/**
	 * @param maxPoolSize the maxPoolSize to set
	 */
	public void setMaxPoolSize(Integer maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}
	/**
	 * @return the queueCapacity
	 */
	public Integer getQueueCapacity() {
		return queueCapacity;
	}
	/**
	 * @param queueCapacity the queueCapacity to set
	 */
	public void setQueueCapacity(Integer queueCapacity) {
		this.queueCapacity = queueCapacity;
	}
	/**
	 * @return the keepAliveSeconds
	 */
	public Integer getKeepAliveSeconds() {
		return keepAliveSeconds;
	}
	/**
	 * @param keepAliveSeconds the keepAliveSeconds to set
	 */
	public void setKeepAliveSeconds(Integer keepAliveSeconds) {
		this.keepAliveSeconds = keepAliveSeconds;
	}
	/**
	 * @return the allowCoreThreadTimeOut
	 */
	public Integer getAllowCoreThreadTimeOut() {
		return allowCoreThreadTimeOut;
	}
	/**
	 * @param allowCoreThreadTimeOut the allowCoreThreadTimeOut to set
	 */
	public void setAllowCoreThreadTimeOut(Integer allowCoreThreadTimeOut) {
		this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
	}
	/**
	 * @return the limitTime
	 */
	public Integer getLimitTime() {
		return limitTime;
	}
	/**
	 * @param limitTime the limitTime to set
	 */
	public void setLimitTime(Integer limitTime) {
		this.limitTime = limitTime;
	}

	
}
