package com.babel.common.core.entity;

/**
 * 客户session信息.
 * 
 * @author  chenjh
 * @version 1.0.0
 */
public final class OnlineSession implements  java.io.Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * 客户计算机的ip.
     */
    private String ip = null;
    /**
     * 客户登录名.
     */
    private String loginId = null;
    /**
     * 客户登录系统时间.
     */
    private String lastAccessTime = null;
    private String startTimestamp = null;
    

    /**
     * 构造器.
     * @param ip
     * @param loginId
     * @param onlineTime
     */
    public OnlineSession(String ip,String loginId,String lastAccessTime){
        this.ip=ip;
        this.loginId=loginId;
        this.lastAccessTime=lastAccessTime;
    }

    /**
     * @return Returns the ip.
     */
    public String getIp() {
        return ip;
    }
    /**
     * @param ip The ip to set.
     */
    public void setIp(String ip) {
        this.ip = ip;
    }
    /**
     * @return Returns the loginId.
     */
    public String getLoginId() {
        return loginId;
    }
    /**
     * @param loginId The loginId to set.
     */
    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }
    

	public String getLastAccessTime() {
		return lastAccessTime;
	}

	public void setLastAccessTime(String lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}

	public String getStartTimestamp() {
		return startTimestamp;
	}

	public void setStartTimestamp(String startTimestamp) {
		this.startTimestamp = startTimestamp;
	}
}