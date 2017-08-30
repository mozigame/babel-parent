package com.babel.common.core.data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 发送时，取userId,roleId,tos对应的邮箱的并集
 * @author jinhe.chen
 *
 */
public interface ISmsVO {
	public List<Long> getUserIdList();
	public List<Long> getRoleIdList();
	public String getTos();
	public String getContent();
	public Date getSendDate();//为空表示立即发送，否则sendDate>当前时间就发送
	public String getTemplate();//短信模板
	public Map<String, Object> getParamMap();
	public Long getSenderId();
	
}
