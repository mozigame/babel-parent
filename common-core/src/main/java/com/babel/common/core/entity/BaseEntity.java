package com.babel.common.core.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;


public abstract class BaseEntity extends BaseEntitySimple implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public void initCreate(){
		this.status=1;
		super.initCreate();
	}	
	
	
//	protected Long id;
	private String code;
	private String name;
	
	
	/** 状态(if_active)0无效，1有效 */
    private Integer status;

	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	

	public Integer getStatus() {
		return status;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}

    
   
	
}
