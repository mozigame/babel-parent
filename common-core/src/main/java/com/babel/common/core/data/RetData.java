package com.babel.common.core.data;

import com.babel.common.core.exception.BaseException;
import com.babel.common.core.exception.UnknownException;

/**
 * msg为""、"null"、"SUCCESS"的时候，代表调用成功
 * @author shihui.cai
 */
public class RetData<T> implements java.io.Serializable{

	private static final String SUCCESS = "SUCCESS";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public RetData(){
		
	}
	public RetData(T data) {
		this.data = data;
	}
	
	public RetData(String err, String msg) {
		this.err = err;
		this.msg = msg;
	}
	
	public static <T> RetData<T> unknowError(String msg) {
		return createByBaseException(new UnknownException(msg)); 
	}
	@SuppressWarnings("rawtypes")
	static RetData successRetData = new RetData();
	@SuppressWarnings("unchecked")
	public static <T> RetData<T> success() {
		return successRetData;
	}
	
	public static <T> RetData<T> createByBaseException(BaseException e) {
		return new RetData<>(e.getErr(), e.getMessage());
	}

	private T data;
	private String err = "SUCCESS";
	private String msg = "";
	/**
	 * 数据最大修改时间，用于比较之前数据拉取后有没有发生变化，如果有变化就返回，没有变化，就返回空数据
	 */
	private Long maxUpdateTime;
	/**
	 * @return the data
	 */
	public T getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(T data) {
		this.data = data;
	}
	/**
	 * @return the err
	 */
	public String getErr() {
		return err;
	}
	/**
	 * @param err the err to set
	 */
	public void setErr(String err) {
		this.err = err;
	}
	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}
	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Long getMaxUpdateTime() {
		return maxUpdateTime;
	}
	public void setMaxUpdateTime(Long maxUpdateTime) {
		this.maxUpdateTime = maxUpdateTime;
	}
	
	
}
