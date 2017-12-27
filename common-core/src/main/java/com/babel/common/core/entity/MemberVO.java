package com.babel.common.core.entity;

import java.io.Serializable;

public class MemberVO implements Serializable{
	

    /**
	 * 
	 */
	private static final long serialVersionUID = 8308405408665786722L;


	private Long cid;


    private Long platInfoId;
    
    private Long agentId;
	private Long agentName;

    private String ownerInfo;
    

    private String login;
    

    private String oddType;

    private Integer acType;

    private String curType;
    
    private Integer levelId;
	private Integer levelName;

    private Integer status;

	public Long getCid() {
		return cid;
	}

	public void setCid(Long cid) {
		this.cid = cid;
	}

	public Long getPlatInfoId() {
		return platInfoId;
	}

	public void setPlatInfoId(Long platInfoId) {
		this.platInfoId = platInfoId;
	}

	public String getOwnerInfo() {
		return ownerInfo;
	}

	public void setOwnerInfo(String ownerInfo) {
		this.ownerInfo = ownerInfo;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getOddType() {
		return oddType;
	}

	public void setOddType(String oddType) {
		this.oddType = oddType;
	}

	public Integer getAcType() {
		return acType;
	}

	public void setAcType(Integer acType) {
		this.acType = acType;
	}

	public String getCurType() {
		return curType;
	}

	public void setCurType(String curType) {
		this.curType = curType;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * @return the levelId
	 */
	public Integer getLevelId() {
		return levelId;
	}

	/**
	 * @param levelId the levelId to set
	 */
	public void setLevelId(Integer levelId) {
		this.levelId = levelId;
	}

	public Long getAgentId() {
		return agentId;
	}

	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}

	public Long getAgentName() {
		return agentName;
	}

	public void setAgentName(Long agentName) {
		this.agentName = agentName;
	}

	public Integer getLevelName() {
		return levelName;
	}

	public void setLevelName(Integer levelName) {
		this.levelName = levelName;
	}
}
