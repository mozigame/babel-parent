package com.babel.common.core;

public class RoleDTO extends RoleVO{
	public RoleDTO(){
	}
	public RoleDTO(RoleVO data){
		this.setName(data.getName());
		this.setId(data.getId());
		this.setStatus(data.getStatus());
		this.setDescs(data.getDescs());
	}
	private Integer level;

	public Integer getLevel() {
		return level;
	}
}
