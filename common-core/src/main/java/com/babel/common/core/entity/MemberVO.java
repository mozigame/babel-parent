package com.babel.common.core.entity;

import java.io.Serializable;

public class MemberVO implements Serializable{
	

    /**
	 * 
	 */
	private static final long serialVersionUID = 8308405408665786722L;


	private Long cid;


    private Long platInfoId;

    private String ownerInfo;
    

    private String login;
    

    private String oddType;

    private Integer acType;

    private String curType;

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
    
    
  
}
