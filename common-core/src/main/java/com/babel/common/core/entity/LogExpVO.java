package com.babel.common.core.entity;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.exception.ExceptionUtils;

import com.babel.common.core.data.RetData;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.exception.RetException;

/**
 * 异常日志VO
 * @author jinhe.chen
 *
 */
public class LogExpVO implements Serializable {
	private static final long serialVersionUID = 1L;
	public static int ERROR_INFO_MAX_LENGTH=3500;
	private String expType;//异常类型
	private String msgCode;//异常编码
	private String msgBody;//异常信息
	private int flag;//异常状态
	private String errorInfo;//异常详情
	private Date date;//异常时间
	private String className;//异常发生的类名
	public LogExpVO(){
		
	}
	public LogExpVO(Object obj, Throwable cause){
		if(obj!=null){
			this.className=obj.getClass().getSimpleName();
		}
		msgCode="ERR_UNKOWN";
		if(cause!=null){
			expType=cause.getClass().getSimpleName();
			msgBody=cause.getMessage();
			errorInfo=ExceptionUtils.getFullStackTrace(cause);
			if(errorInfo.length()>ERROR_INFO_MAX_LENGTH){
				errorInfo=errorInfo.substring(0,  ERROR_INFO_MAX_LENGTH)+"...";
			}
		}
		flag=3;
		date=new Date();
		
	}
	public LogExpVO(Object obj, RetException retExp){
		this(obj, retExp.getCause());
		if(retExp.getRetResult()!=null){
			this.msgBody=retExp.getRetResult().getMsgBody();
			this.msgCode=retExp.getRetResult().getMsgCode();
			this.flag=retExp.getRetResult().getFlag();
		}
		
	}
	public LogExpVO(Object obj, RetData retExp, Throwable cause){
		this(obj, cause);
		this.msgBody=retExp.getMsg();
		this.msgCode=retExp.getErr();
		this.flag=0;
	}
	public LogExpVO(Object obj, RetResult retExp, Throwable cause){
		this(obj, cause);
		this.msgBody=retExp.getMsgBody();
		this.msgCode=retExp.getMsgCode();
		this.flag=retExp.getFlag();
	}
	
	public String getExpType() {
		return expType;
	}
	public void setExpType(String expType) {
		this.expType = expType;
	}
	public String getMsgCode() {
		return msgCode;
	}
	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}
	public String getMsgBody() {
		return msgBody;
	}
	public void setMsgBody(String msgBody) {
		this.msgBody = msgBody;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getErrorInfo() {
		return errorInfo;
	}
	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	
}
