package com.babel.common.core.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;

import com.babel.common.core.entity.AccessToken;
import com.babel.common.core.util.CommUtil;
import com.babel.common.core.util.DateUtils;
import com.babel.common.core.util.RedisUtil;
import com.babel.common.core.util.SpringContextUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.sf.json.JSONObject;


public class WeixinUtil {
	private static final Logger logger = Logger.getLogger(WeixinUtil.class);
	public static String GET_ACCESSTOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	public static String GET_JSAPI_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
	public static final String REDIS_KEY_TOKEN_MAP="_tokenMap";
	public static final String REDIS_KEY_TOKEN_JS_MAP="_tokenJsMap";
	private static Map<String, AccessToken> tokenMap=new HashMap<>();
	private static Map<String, AccessToken> tokenJsMap=new HashMap<>();
	public static AccessToken getAccessTokenFromCache(String appid , String appsecret) throws Exception, IOException {
		Date now = new Date();
		AccessToken token=(AccessToken)RedisUtil.getRedis(REDIS_KEY_TOKEN_MAP, appid);
		if(token==null || now.getTime() - token.getCreateDate().getTime() > 3600*1000) {
			token = WeixinUtil.getAccessToken(appid, appsecret);
			RedisUtil.putRedis(REDIS_KEY_TOKEN_MAP, appid, token);
		}
		return token;
	}

	public static String getJsapiTicketFromCache(String accessToken) {
		AccessToken tokenJs=getJsapiTicketCache(accessToken);
		return tokenJs.getAccessToken();
	}
	
	public static AccessToken getJsapiTicketCache(String accessToken){
		AccessToken tokenJs=(AccessToken)RedisUtil.getRedis(REDIS_KEY_TOKEN_JS_MAP, accessToken);
		Date now = new Date();
		if(tokenJs==null||now.getTime()-tokenJs.getCreateDate().getTime() > 3600*1000){
			tokenJs=new AccessToken();
			tokenJs.setAccessToken(getJsapiTicket(accessToken));
			tokenJs.setCreateDate(new Date());
			tokenJs.setExpiresIn(7200);
			RedisUtil.putRedis(REDIS_KEY_TOKEN_JS_MAP, accessToken, tokenJs);;
		}
		return tokenJs;
	}
	
	public static String getJsapiTicket(String accessToken) {
		String jsapiTicket = null;
		try {
			String requestUrl = GET_JSAPI_TICKET_URL.replace("ACCESS_TOKEN" , accessToken);
			// 创建HttpClientBuilder
			HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
			// HttpClient
			CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
			HttpGet httpGet = new HttpGet(requestUrl);
			HttpResponse httpResponse;
			httpResponse = closeableHttpClient.execute(httpGet);
			String jsonStr = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
			JsonParser jsonparer = new JsonParser();
			JsonObject json = jsonparer.parse(jsonStr).getAsJsonObject();
			if("0".equals(json.get("errcode").getAsString())){
				jsapiTicket = json.get("ticket").getAsString();
			}
			// 释放资源
			closeableHttpClient.close();
		} catch (Exception e) {
			logger.error("getOpenId exception:"+e.getMessage(), e);
		} 
		logger.info("------getJsapiTicket---");
		return jsapiTicket;
    }
    public static AccessToken getAccessToken(String appid , String appsecret) throws Exception, IOException {
        AccessToken accessToken = null;
        JsonParser jsonparer = new JsonParser();// 初始化解析json格式的对�?
        String requestUrl = GET_ACCESSTOKEN_URL.replace("APPID" , appid).replace("APPSECRET" , appsecret);
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(requestUrl);
        HttpResponse res = client.execute(get);
        String responseContent = null; // 响应内容
        HttpEntity entity = res.getEntity();
        responseContent = EntityUtils.toString(entity, "UTF-8");
        JsonObject json = jsonparer.parse(responseContent).getAsJsonObject();
        //JSONObject jsonObject = JSONObject.fromObject(json);
        // 如果请求成功
        if (null != json) {
            try {
                accessToken = new AccessToken();
                accessToken.setAccessToken(json.get("access_token").getAsString());
                accessToken.setExpiresIn(json.get("expires_in").getAsInt());
                accessToken.setCreateDate(new Date());
            } catch (Exception e) {
                accessToken = null;
                // 获取token失败
                System.out.println("获取token失败 errcode:{} errmsg:{}");
            }
        }
        logger.info("------getAccessToken--appid="+appid);
        return accessToken;
    }
    
    public static AccessToken getAccessToken2(String appid , String appsecret) {
        AccessToken accessToken = null;

        String requestUrl = GET_ACCESSTOKEN_URL.replace("APPID" , appid).replace("APPSECRET" , appsecret);
        String json = httpRequest(requestUrl , "GET" , null);
        JSONObject jsonObject = JSONObject.fromObject(json);
        // 如果请求成功
        if (null != jsonObject) {
            try {
                accessToken = new AccessToken();
                accessToken.setAccessToken(jsonObject.getString("access_token"));
                accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
                accessToken.setCreateDate(new Date());
            } catch (Exception e) {
                accessToken = null;
                // 获取token失败
                System.out.println("获取token失败 errcode:{} errmsg:{}");
            }
        }
        return accessToken;
    }
    
    private static String httpRequest(String requestUrl , String requestMethod , String outputStr) {
        JSONObject jsonObject = null;
        StringBuffer buffer = new StringBuffer();
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始�?
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL" , "SunJSSE");
            sslContext.init(null , tm , new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST�?
            httpUrlConn.setRequestMethod(requestMethod);

            if ("GET".equalsIgnoreCase(requestMethod)) httpUrlConn.connect();

            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱�?
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 将返回的输入流转换成字符�?
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream , "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
//          jsonObject = JSONObject.fromObject(buffer.toString());
            return buffer.toString();
        } catch (ConnectException ce) {
            System.out.println("Weixin server connection timed out.");
        } catch (Exception e) {
            System.out.println("error.");
        }
//      return jsonObject;
        return null;
    }
    
    
    /**
     * 生成模板消息
     *  
     * @param openId
     * @param tplId
     * @param url
     * @param paramMap
     * @return exp:{
           "touser":"OPENID",
           "template_id":"ngqIpbwh8bUfcSsECmogfXcV14J0tQlEpBO27izEYtY",
           "url":"http://weixin.qq.com/download",            
           "data":{
                   "first": {
                       "value":"恭喜你购买成功！",
                       "color":"#173177"
                   },
                   "keynote1":{
                       "value":"巧克力",
                       "color":"#173177"
                   }
           }
       }
     */
    public static String getTplMsg(String openId, String tplId, String url, Map<String, String> paramMap){
    	if(openId==null){
			openId="#(tos)";
		}
    	String jsons="{\n";
    	jsons+=addJsonKey("touser", openId);
    	jsons+=","+addJsonKey("template_id", tplId);
    	if(url!=null)
    		jsons+=","+addJsonKey("url", url);
    	String dataJson=getMsgJson(paramMap);
    	jsons+=","+addJsonKey("data", dataJson);
    	jsons+="}";
    	return jsons;
    }
    
//    private static SimpleDateFormat sdf_datetime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 赔付与退款异常消息
     * gU-9MEE2U5blAK8SCN2NQlK8OgL-e6LmxZj0Htgnov0
     * @param tplId
     * @param openId
     * @param url
     * @param first
     * @param title
     * @param source
     * @param dateTime为空表示当前时间
     * @param msgDesc
     * @param remark可以为空
     * @return
     */
    public static String getTplMsgWarn(String tplId, String openId,  String url, String first, String title, String source, Date dateTime, String msgDesc, String remark){
    	Map<String, String> paramMap=new HashMap<>();
    	if(first==null){
    		first="您有一条重要错误通知,请您及时处理";
    	}
    	if(dateTime==null){
    		dateTime=new Date();
    	}
    	paramMap.put("first", first);//标题
    	paramMap.put("keyword1", title);//标题
    	paramMap.put("keyword2", source);//来源
    	paramMap.put("keyword3", DateUtils.format(dateTime, DateUtils.SDF_FORMAT_DATETIME));//时间
    	paramMap.put("keyword4", msgDesc);//摘要
    	if(remark!=null){
    		paramMap.put("remark", "补天乐退款");
    	}
    	
    	return WeixinUtil.getTplMsg("#(tos)", tplId, url, paramMap);
    	
    }
    
   
    
    private static String getMsgJson(Map<String, String> paramMap){
    	String json="\n	{\n";
    	Set<Map.Entry<String, String>> sets=paramMap.entrySet();
    	for(Map.Entry<String, String> entry:sets){
    		json+=addJsonKeyWx(entry.getKey(), entry.getValue(), null)+",\n";
    	}
    	if(json.endsWith(",\n")){
    		json=json.substring(0, json.length()-2)+"\n";
    	}
    	json+="	}";
    	return json;
    }
    private static String addJsonKeyWx(String key, String value, String color){
    	if(color==null){
    		color="#173177";
    	}
    	String json="	{\n";
    	json+="		"+addJsonKey("value", value);
    	json+="		,"+addJsonKey("color", color);
    	json+="	}";
    	json="	"+addJsonKey(key, json).trim();
    	return json;
    }
    private static String addJsonKey(String key, String value){
    	if(value==null){
    		logger.warn(" key="+key+" isEmpty");
    		return "";
    	}
    	value=value.trim();
    	if(!(value.startsWith("{") && value.endsWith("}"))){
    		value="\""+value+"\"";
    	}
    	return "\""+key+"\":"+value+"\n";
    }
    
    /** 
     * 微信支付签名算法sign 
     * @param 微信支付Key
     * @param characterEncoding 
     * @param parameters 
     * @return 
     */  
    @SuppressWarnings("unchecked")  
    public static String createWxSign(String Key, String characterEncoding,SortedMap<String,Object> parameters){  
        StringBuffer sb = new StringBuffer();  
        Set es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序）  
        Iterator it = es.iterator();  
        while(it.hasNext()) {  
            Map.Entry entry = (Map.Entry)it.next();  
            String k = (String)entry.getKey();  
            Object v = entry.getValue();  
            if(null != v && !"".equals(v)   
                    && !"sign".equals(k) && !"key".equals(k)) {  
                sb.append(k + "=" + v + "&");  
            }  
        }  
        sb.append("key=" + Key);  
        String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();  
        return sign;  
    }
    
    /**
     * 参数转成wx的xml格式
     * @param parameters
     * @return
     */
    public static String toWxXml(Map<String,Object> parameters){
    	String xmls="";
    	Set<String> keys=parameters.keySet();
    	for(String key:keys){
    		xmls+="	"+xmlAppend(key, parameters.get(key));
    	}
    	xmls=xmlAppend("xml", xmls);
    	return xmls;
    	
    }
    private static String xmlAppend(String key, Object value){
    	key=key.trim();
    	return "<"+key+">"+value+"</"+key+">\n";
    }
    
    public static String getMsgUrl(String msgType, String ToUserName, String FromUserName,  String url, String content){
		if(msgType==null){
			msgType="text";
		}
		if(url!=null){
			content="<a href=\""+url+"\">"+content+"</a>";
		}
		String returnTime=""+new Date().getTime();
		StringBuffer str = new StringBuffer(); 
		str.append("<xml>");  
        str.append("<ToUserName><![CDATA[" + FromUserName + "]]></ToUserName>");  
        str.append("<FromUserName><![CDATA[" + ToUserName + "]]></FromUserName>");  
        str.append("<CreateTime>" + returnTime + "</CreateTime>");  
        str.append("<MsgType><![CDATA[" + msgType + "]]></MsgType>");  
        str.append("<Content><![CDATA["+content+"]]></Content>");  
        str.append("</xml>");  
        return str.toString();
	}
    
    /**
	 * 图文消息
	 * @param msgType
	 * @param ToUserName
	 * @param FromUserName
	 * @param url
	 * @param imageUrl
	 * @param content
	 * @return
	 */
	public static String getMsgImageUrl(String msgType, String ToUserName, String FromUserName, String articleItem){
		if(msgType==null){
			msgType="news";
		}
		int count=CommUtil.findStrCount(articleItem, "<item>");
		String returnTime=""+new Date().getTime();
		StringBuffer str = new StringBuffer(); 
		str.append("<xml>");  
        str.append("<ToUserName><![CDATA[" + FromUserName + "]]></ToUserName>");  
        str.append("<FromUserName><![CDATA[" + ToUserName + "]]></FromUserName>");  
        str.append("<CreateTime>" + returnTime + "</CreateTime>");  
        str.append("<MsgType><![CDATA[" + msgType + "]]></MsgType>");
        str.append("<ArticleCount><![CDATA[" + count + "]]></ArticleCount>");
        str.append("<Articles>");
        str.append(articleItem);
        str.append("</Articles>");
        str.append("</xml>");  
        return str.toString();
	}
	
	public static String getMsgArticleItem(String url, String imageUrl, String title, String content){
		StringBuffer str = new StringBuffer(); 
		str.append("<item>");
    	str.append("<Title><![CDATA["+title+"]]></Title> ");
    	str.append("<Description><![CDATA["+content+"]]></Description>");
    	str.append("<PicUrl><![CDATA["+imageUrl+"]]></PicUrl>");
    	str.append("<Url><![CDATA["+url+"]]></Url>");
    	str.append("</item>");
    	return str.toString();
	}
//    public static void main(String[] args) {
//        WeixinUtil util = new WeixinUtil();
//        try {
//            //获取accessToken之前判断文件里有没有，没有重新获取，填写过期时间，有判断时间有没有过�?
//            PropertiesUtil putil=new PropertiesUtil(Constants.CONF_PROPERTIES_PATH);
//            Long overduetime = Long.parseLong(putil.getProperty("OVERDUE_ACCESS_TOKEN_TIME"));
//            Timestamp s = new Timestamp();
//            AccessToken token = new AccessToken() ;
//            if(s.getDateTime()>overduetime){
//                //超时从新获取accessToken
//                token = util.getAccessToken2(Constants.APPID, Constants.SECRET);
//                putil.writeProperties("OVERDUE_ACCESS_TOKEN_TIME", String.valueOf(s.getDateTime()+Constants.EFFECTIVE_TIME));
//                putil.writeProperties("ACCESS_TOKEN", token.getAccessToken());
//            }else{
//                System.out.println("token:"+putil.getProperty("ACCESS_TOKEN")+",time:"+token.getExpiresIn());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    
    /**
     * post提交数据请求处理，如果有token会加上token参数
     * (不带证书)
     * @param url
     * @param data
     * @param accessToken
     * @return 接口返回内容
     * 
     */
    public static String wxApiPost(String url, String data, String accessToken) throws Exception{
		logger.debug("----data--"+data+" data="+data);
		

		if(!StringUtils.isEmpty(accessToken)){
			if(url.indexOf("?")>0){
				url+="&access_token="+accessToken;
			}
			else{
				url+="?access_token="+accessToken;
			}
		}
		String characterEncoding="UTF-8";
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpost = new HttpPost(url); // post请求
		httpost.addHeader("Connection", "keep-alive");
		httpost.addHeader("Accept", "*/*");
		httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//		httpost.addHeader("Host", "api.mch.weixin.qq.com");
//		httpost.addHeader("X-Requested-With", "XMLHttpRequest");
		httpost.addHeader("Cache-Control", "max-age=0");
//		httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
		httpost.setEntity(new StringEntity(data, characterEncoding));
		CloseableHttpResponse response = httpclient.execute(httpost);
		HttpEntity entity = response.getEntity();
		String jsonStr = EntityUtils.toString(response.getEntity(), characterEncoding);
//		System.out.println("----------------------------------------");
//		System.out.println("-----jsonStr="+jsonStr);
		if(response.getStatusLine().getStatusCode()!=200){
			logger.error("----wxApi--url="+url+" \ndata="+data+"\njsonStr="+jsonStr);
		}
//		System.out.println(response.getStatusLine());
		EntityUtils.consume(entity);
		return jsonStr;
	}
    
    /**
     * get数据请求处理，如果有token会加上token参数
     * (不带证书)
     * @param url
     * @param data
     * @param accessToken
     * @return 接口返回内容
     * 
     */
    public static String wxApiGet(String url, String accessToken) throws Exception{
//		logger.debug("----data--"+data+" data="+data);
		
    	
		if(!StringUtils.isEmpty(accessToken)){
			if(url.indexOf("?")>0){
				url+="&access_token="+accessToken;
			}
			else{
				url+="?access_token="+accessToken;
			}
		}
		String characterEncoding="UTF-8";
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpost = new HttpGet(url); // post请求
		httpost.addHeader("Connection", "keep-alive");
		httpost.addHeader("Accept", "*/*");
		httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//		httpost.addHeader("Host", "api.mch.weixin.qq.com");
//		httpost.addHeader("X-Requested-With", "XMLHttpRequest");
		httpost.addHeader("Cache-Control", "max-age=0");
//		httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
//		httpost.setEntity(new StringEntity(data, characterEncoding));
		CloseableHttpResponse response = httpclient.execute(httpost);
		HttpEntity entity = response.getEntity();
		String jsonStr = EntityUtils.toString(response.getEntity(), characterEncoding);
//		System.out.println("----------------------------------------");
//		System.out.println("-----jsonStr="+jsonStr);
		if(response.getStatusLine().getStatusCode()!=200){
			logger.error("----wxApi--url="+url+"\njsonStr="+jsonStr);
		}
//		System.out.println(response.getStatusLine());
		EntityUtils.consume(entity);
		return jsonStr;
	}
    
    /**
     * 检测是否有emoji字符
     * @param source
     * @return 一旦含有就抛出
     */
    public static boolean containsEmoji(String source) {
        if (StringUtils.isBlank(source)) {
            return false;
        }
        
        int len = source.length();
        
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            
            if (isEmojiCharacter(codePoint)) {
                //do nothing，判断到了这里表明，确认有表情字符
                return true;
            }
        }
        
        return false;
    }


    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || 
                (codePoint == 0x9) ||                            
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }
    
    /**
     * 过滤emoji 或者 其他非文字类型的字符
     * @param source
     * @return
     */
    public static String filterEmoji(String source){
    	
    	 if (!containsEmoji(source)) {
             return source;//如果不包含，直接返回
         }
         //到这里铁定包含
         StringBuilder buf = null;
         
         int len = source.length();
         
         for (int i = 0; i < len; i++) {
             char codePoint = source.charAt(i);
             
             if (isEmojiCharacter(codePoint)) {
                 if (buf == null) {
                     buf = new StringBuilder(source.length());
                 }
                 
                 buf.append(codePoint);
             } else {
             }
         }
         
         if (buf == null) {
             return source;//如果没有找到 emoji表情，则返回源字符串
         } else {
             if (buf.length() == len) {//这里的意义在于尽可能少的toString，因为会重新生成字符串
                 buf = null;
                 return source;
             } else {
                 return buf.toString();
             }
         }
         
     }
    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private static String createNonceStr() {
        return UUID.randomUUID().toString();
    }

    private static String createTimestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
    public static Map<String, String> getJsJDKSign(String jsapi_ticket, String url) {
        Map<String, String> ret = new HashMap<String, String>();
        String nonce_str = createNonceStr();
        String timestamp = createTimestamp();
        String string1;
        String signature = "";

        //注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapi_ticket +
                  "&noncestr=" + nonce_str +
                  "&timestamp=" + timestamp +
                  "&url=" + url;
        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        ret.put("url", url);
        ret.put("jsapi_ticket", jsapi_ticket);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);

        return ret;
    }
    public static String APPID = null;
    public static String APPSECRET = null;
    public void setAppID(String appID) {
    	APPID = appID;
    }
    public void setAppSecret(String appSecret) {
    	APPSECRET = appSecret;
    }
	public static AccessToken getAccessTokenFromCache() throws Exception {
		if(APPID==null || APPSECRET==null) {
			throw new Exception("APPID OR APPSECRET NULL");
		}
		return getAccessTokenFromCache(APPID, APPSECRET);
	}
	
	private static RedisTemplate redisTemplate=null;
	private static void initRedis(){
		if(SpringContextUtil.containsBean("redisTemplate")){
			redisTemplate=(RedisTemplate)SpringContextUtil.getBean("redisTemplate");
		}
	}
	
	private static RedisTemplate getRedisTemplate(){
		initRedis();
		return redisTemplate;
	}
}