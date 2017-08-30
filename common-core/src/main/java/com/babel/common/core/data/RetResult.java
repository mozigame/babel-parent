package com.babel.common.core.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@XmlRootElement(name="retResult")
@XmlType
@JsonInclude(Include.NON_NULL)
public class RetResult<T> implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RetResult(){
		this.success();
	}
	public RetResult(RetResult ret){
		this.dataList=ret.getDataList();
		this.msgBody=ret.getMsgBody();
		this.msgCode=ret.getMsgCode();
		this.flag=ret.getFlag();
		this.exception=ret.takeException();
	}
	public static enum msg_codes{
		ERR_DATA_INPUT            ("ERR_DATA_INPUT", "输入数据有误", "error input"),
		ERR_DATA_INVALID_ID       ("ERR_DATA_INVALID_ID", "无数数据id", "data invalid id"),
		ERR_DATA_REPEAT           ("ERR_DATA_REPEAT", "数据重复", "data repeat"),
		ERR_DATA_UNEXISTED        ("ERR_DATA_UNEXISTED", "数据不存在", "data unexisted"),
		ERR_DATA_EXISTED          ("ERR_DATA_EXISTED", "数据已经存在", "data existed"),
		ERR_DATA_INVALID         ("ERR_DATA_INVALID", "数据无效", "data invalid"),
		ERR_DATA_NOT_FOUND          ("ERR_DATA_NOT_FOUND", "数据未找到", "data not found"),
		ERR_DATA_MAX_LENGTH          ("ERR_DATA_MAX_LENGTH", "超出数据最大长度", "data out max length"),
		ERR_DATA_OPERATE          ("ERR_DATA_OPERATE", "无效操作", "Error operate"),
		ERR_DELETE_ERROR          ("ERR_DELETE_ERROR", "删除数据失败,请先删除其相关的数据", "delete error"),
		ERR_LOGIN_INVALID_ESB ("ERR_LOGIN_INVALID_ESB", "ESB帐户或密码无效", "invalid ESB login"),
		ERR_LOGIN_INVALID_ACCOUNT ("ERR_LOGIN_INVALID_ACCOUNT", "帐号不存在", "invalid account"),
		ERR_LOGIN_INVALID_PASSWORD("ERR_LOGIN_INVALID_PASSWORD", "密码错误", "invalid password"),
		ERR_PERMISSION_DENIED     ("ERR_PERMISSION_DENIED", "权限不足", "permission denied"),
		ERR_SERVER_CONNECTED      ("ERR_SERVER_CONNECTED", "系统连接失败，请联系管理员", "server connect error!"),
		ERR_UNKNOWN                ("ERR_UNKNOWN", "未知错误", "unknown error!"),
		SUCC_SUCCESSFUL           ("SUCCESS", "操作成功", "success");
		private String code;
		private String name;
		private String nameEn;
		
		msg_codes(String code, String name, String nameEn){
			this.code=code;
			this.name=name;
			this.nameEn=nameEn;
		}
		public String getCode(){
			return this.code;
		}
		public String getName(){
			return this.name;
		}
		public String getNameEn(){
			return this.nameEn;
		}
		public static String getName(String code){
			msg_codes[] list=msg_codes.values();
			for(msg_codes msg:list){
				if(msg.code.equals(code)){
					return msg.name;
				}
			}
			return null;
		}
	}
	public void success(){
		this.flag=0;
		this.msgCode=msg_codes.SUCC_SUCCESSFUL.getCode();
		this.msgBody = msg_codes.SUCC_SUCCESSFUL.getName();
	}
	private Collection<T> dataList;
	private int size;
	protected int flag;
	protected String msgCode;
	protected String msgBody;
	protected Exception exception;
	
	public RetResult<T> initError(String msgCode, String msgBody, Exception e){
		this.flag=1;
		this.msgCode=msgCode;
		this.msgBody=msgBody;
		if(e!=null){
			this.exception=e;
			this.flag=2;
			this.msgBody+=",error:"+e.getMessage();
		}
		return this;
	}
	
	public RetResult<T> initError(msg_codes msg_code, String msgBody, Exception e){
		this.flag=1;
		this.msgCode=msg_code.getCode();
		
		if(StringUtils.isEmpty(msgBody)) {
			this.msgBody=msg_code.getName();
		} else { 
			this.msgBody = msgBody;
		}
		return initError(msgCode, msgBody, e);
	}
	
	@JsonIgnore
	public T getFirstData(){
		if(!CollectionUtils.isEmpty(dataList)){
			return dataList.iterator().next();
		}
		return null;
	}
	
	
	public Collection<T> getDataList() {
		return dataList;
	}
	public void setDataList(Collection<T> dataList) {
		this.dataList = dataList;
	}
	
	public void setData(T data){
		if(this.dataList==null){
			this.dataList=new ArrayList<T>();
		}
		this.dataList.add(data);
	}
	
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
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
	public int getSize() {
		if(dataList!=null){
			size= dataList.size();
		}
		return size;
	}
//	public void setSize(int size) {
//		this.size = size;
//	}
	
	public boolean isSuccess(){
		return this.flag==0;
	}
	
	public Exception takeException(){
		return this.exception;
	}
	
	public String toJson(){
		String json="";
		json+="flag:"+this.flag+",size:"+this.getSize()+",msgCode:"+this.getMsgCode();
		if(this.getMsgBody()!=null)
			json+=",msgBody="+this.getMsgBody();
		
		if(this.takeException()!=null){
			String expInfo=ExceptionUtils.getFullStackTrace(exception);
			if(expInfo.length()>100){
				expInfo=expInfo.substring(0, 100);
			}
			json+=",exception:"+this.takeException().getMessage()+","+expInfo;
		}
		json="{"+json+"}";
		return json;
	}
}
