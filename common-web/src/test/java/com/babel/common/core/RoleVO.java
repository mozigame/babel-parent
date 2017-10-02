package com.babel.common.core;

public class RoleVO implements Cloneable {
	public RoleVO() {

	}

	public RoleVO(Long id, String name, String descs) {
		this.id = id;
		this.name = name;
		this.descs = descs;
	}

	private String name;
	private Long id;
	private String descs;
	private Character status;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescs() {
		return descs;
	}

	public void setDescs(String descs) {
		this.descs = descs;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
