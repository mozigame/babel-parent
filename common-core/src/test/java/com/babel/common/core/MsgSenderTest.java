package com.babel.common.core;

import org.junit.Test;

import com.babel.common.config.MsgApiVO;
import com.babel.common.core.tools.MsgSender;

public class MsgSenderTest {
	@Test
	public void testSend() throws Exception{
		MsgApiVO msgApiVO=new MsgApiVO();
		msgApiVO.setUid("javauser");
		msgApiVO.setUrl("http://202.153.207.181:8813");
		msgApiVO.setPwd("oicjslmvn");
		msgApiVO.setChannel("bccp_test");
		String retInfo=MsgSender.sendMessage(msgApiVO, "bccp", "test info2");
		System.out.println("---retInfo="+retInfo);
	}
}
