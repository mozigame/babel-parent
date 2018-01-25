package com.babel.common.core.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.babel.common.config.IMsgApi;
import com.google.gson.Gson;

public class MsgSender {
	private static final Logger logger = Logger.getLogger(MsgSender.class);
	private static Gson gson=new Gson();
	/**
	 * post body 範例
{
"Uid":"xxxxxxx",                            //帳號
"Pwd":"xxxxxxxx",                         //密碼
"SendFrom":"ABCDEFG",            //發送者名稱
"SendTo":[],                                   //發送對象 （Slack 不用帶）
"Channel":"channelName",           //發送 slack channel 群組名稱
"SendType":[1],                             // 1: Slack 2:手機短信（未開放） 
"MsgSource":"Test Msg"               //內文
}
	 * @param msgApi
	 */
	public static String sendMessage(IMsgApi msgApi, String sendFrom, String msgSource) throws Exception{
		
		Map<String, Object> map=new HashMap<>();
		map.put("Uid", msgApi.getUid());
		map.put("Pwd", msgApi.getPwd());
		map.put("SendFrom", sendFrom);
		map.put("SendTo", new ArrayList<>());
		map.put("SendType", Arrays.asList(1));
		map.put("Channel", msgApi.getChannel());
		map.put("MsgSource", msgSource);
		String json=gson.toJson(map);
		
		String characterEncoding="UTF-8";
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String url=msgApi.getUrl()+"/SendMessage";
		HttpPost httpost = new HttpPost(url); // post请求
		httpost.addHeader("Connection", "keep-alive");
		httpost.addHeader("Accept", "*/*");
		httpost.addHeader("Content-Type", "application/json");
//		httpost.addHeader("Host", "api.mch.weixin.qq.com");
		httpost.addHeader("Cache-Control", "max-age=0");
//		httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
		httpost.setEntity(new StringEntity(json, characterEncoding));
		CloseableHttpResponse response = httpclient.execute(httpost);
		HttpEntity entity = response.getEntity();
		String jsonStr = EntityUtils.toString(response.getEntity(), characterEncoding);
//		System.out.println("----------------------------------------");
//		System.out.println("-----jsonStr="+jsonStr);
		if(response.getStatusLine().getStatusCode()!=200){
			logger.error("----wxApi--url="+msgApi.getUrl()+" \ndata="+json+"\njsonStr="+jsonStr);
		}
//		System.out.println(response.getStatusLine());
		EntityUtils.consume(entity);
		return jsonStr;
	}
	
	public static String sendMessageTelegram(IMsgApi msgApi, String sendFrom, String msgSource) throws Exception{
		String botId=msgApi.getUid();
		String chatId= msgApi.getChannel();
		
		Map<String, Object> map=new HashMap<>();
		map.put("chat_id", chatId);
		map.put("text", sendFrom+":"+msgSource);
		String data=gson.toJson(map);
		
		String characterEncoding="UTF-8";
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String url=msgApi.getUrl()+"/bot"+botId+"/sendMessage";
		HttpPost httpost = new HttpPost(url); // post请求
		httpost.addHeader("Connection", "keep-alive");
		httpost.addHeader("Accept", "*/*");
		httpost.addHeader("Content-Type", "application/json");
//		httpost.addHeader("Host", "api.mch.weixin.qq.com");
		httpost.addHeader("Cache-Control", "max-age=0");
//		httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
		httpost.setEntity(new StringEntity(data, characterEncoding));
		CloseableHttpResponse response = httpclient.execute(httpost);
		HttpEntity entity = response.getEntity();
		String jsonStr = EntityUtils.toString(response.getEntity(), characterEncoding);
//		System.out.println("----------------------------------------");
//		System.out.println("-----jsonStr="+jsonStr);
		if(response.getStatusLine().getStatusCode()!=200){
			logger.error("----wxApi--url="+msgApi.getUrl()+" \ndata="+data+"\njsonStr="+jsonStr);
		}
//		System.out.println(response.getStatusLine());
		EntityUtils.consume(entity);
		return jsonStr;
	}
}
