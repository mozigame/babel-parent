package com.babel.common.core.entity;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class UserPermitVO implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long userId;
	private Long defaultRoleId;
	private Integer loginType;
	private List<Long> roleIdList;
	private String roleIdStr;
	private Date loginDate;
	private int filterCount;
	private int visitCount;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public List<Long> getRoleIdList() {
		return roleIdList;
	}
	public void setRoleIdList(List<Long> roleIdList) {
		this.roleIdList = roleIdList;
	}
	public Date getLoginDate() {
		return loginDate;
	}
	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}
	public Long getDefaultRoleId() {
		return defaultRoleId;
	}
	public void setDefaultRoleId(Long defaultRoleId) {
		this.defaultRoleId = defaultRoleId;
	}
	public int getFilterCount() {
		return filterCount;
	}
	public void setFilterCount(int filterCount) {
		this.filterCount = filterCount;
	}
	public int getVisitCount() {
		return visitCount;
	}
	public void setVisitCount(int visitCount) {
		this.visitCount = visitCount;
	}
	public String getRoleIdStr() {
		return roleIdStr;
	}
	public void setRoleIdStr(String roleIdStr) {
		this.roleIdStr = roleIdStr;
	}
	public Integer getLoginType() {
		return loginType;
	}
	public void setLoginType(Integer loginType) {
		this.loginType = loginType;
	}
	
	
}
