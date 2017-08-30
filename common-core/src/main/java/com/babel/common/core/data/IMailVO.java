package com.babel.common.core.data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 发送时，取userId,roleId,tos对应的邮箱的并集
 * @author jinhe.chen
 *
 */
public interface IMailVO {
	public List<Long> getUserIdList();
	public List<Long> getRoleIdList();//即支持角色组
	public String getTos();
	public String getCcs();
	public String getBccs();
	public String getTitle();
	public String getContent();
	public Date getSendDate();//为空表示立即发送，否则sendDate>当前时间就发送
	public List<String> getInlineList();//inline附件发送
	public List<String> getAttachList();//附件发送
	public String getTemplate();//邮件模板
	public Map<String, Object> getParamMap();
	public Long getSenderId();
}
