package com.babel.common.core.entity;

import java.util.Date;

/**
 * 用于调用次数统计
 * @author 金和
 *
 */
public class RunCountVO {
	private int count;
	private Date firstDate;
	private Date newDate;
	//数据+1
	public void increase(){
		this.count=count+1;
		if(this.firstDate==null){
			this.firstDate=new Date();
		}
		this.newDate=new Date();
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public Date getFirstDate() {
		return firstDate;
	}
	public void setFirstDate(Date firstDate) {
		this.firstDate = firstDate;
	}
	public Date getNewDate() {
		return newDate;
	}
	public void setNewDate(Date newDate) {
		this.newDate = newDate;
	}
	
	
}
