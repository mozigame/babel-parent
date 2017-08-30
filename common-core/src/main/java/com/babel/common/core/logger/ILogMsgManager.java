package com.babel.common.core.logger;

import java.util.List;

import com.babel.common.core.data.IMailVO;
import com.babel.common.core.data.ISmsVO;

public interface ILogMsgManager {
	
	/**
	 * 
	 * @param mail
	 * @param saveType 发送类型，1整体发送，2分开发送，3不发送
	 */
	public void saveMail(String msgCode, IMailVO mail, int sendType, boolean isAsync);
	
	public void saveMailBatch(String msgCode, List<IMailVO> mailList, int sendType, boolean isAsync);
	/**
	 * 发送短信
	 * @param sms
	 */
	public void saveSms(String msgCode, ISmsVO sms, boolean isAsync);
	
	public void saveSmsBatch(String msgCode, List<ISmsVO> smsList, boolean isAsync);
	
}
