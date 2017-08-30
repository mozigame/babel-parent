package com.babel.common.web.constants;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



public class ConfigWebUtil {
	 private static final Log log4 = LogFactory.getLog(ConfigWebUtil.class);
	 
	protected static Properties config;//配置信息
	public void setConfig(Properties config) {
		ConfigWebUtil.config = config;
		Object obj = null;
		String code=null;
		if(config!=null){
			code=ConfigWebUtil.config_key.INTF_URL_ADDRESS.code;
			obj = config.get(code);
			if(obj!=null){
				log4.info("------config."+code+"="+obj);
			}

			code=ConfigWebUtil.config_key.SYS_LOGIN_DOMAIN.code;
			obj = config.get(code);
			if(obj!=null){
				log4.info("------config."+code+"="+obj);
			}
		}
	}
	private final static Map initMap=new HashMap();
	
	public final static boolean isSupportTheardPool=true;
	
	public static enum config_key{
		INTF_URL_ADDRESS("intfUrlAddress", "https://vps.kpifa.cn/jcc_pws/http/soap/", "接口服务地址"),
		INTF_REGIST_WSDL("intfRegistWsdl", "http://vps.kpifa.cn:8070/jcc_idp/service/esbManageService", "接口自动注册wsdl地址"),
		INTF_REGIST_TYPE("intfRegistType", "esb", "接口自动注册方式(ws/esb)"),
		INTF_REGIST_SERVICE("intfRegistService", "false", "是否注册服务"),
		INTF_UNREGIST_SERVICE("intfUnRegistService", "false", "是否可取消服务"),
		INTF_REGIST_DELAY_TIME("intfRegistDelayTime", "30000", "接口启动延迟注册时间(单位毫秒)"),
		INTF_BSC_ESB_CHECK("intfBscEsbCheck", "true", "是否对esb的返回值进行检查"),
		
		SYS_PASSWORD("sysPassword", "123456", "系统设置密码"),
		SYS_WEB_SERVICE("sysWebService", "false", "是否只是接口服务,(true时加载WEB模块功能)"),
		SYS_RESET_CODE_TABLE_JS("sysResetCodeTableJs", "false", "是否重新生成comm.dict.js"),
		SYS_LOGIN_DOMAIN("sysLoginDomain", "false", "是否以域方式登入"),
		SYS_LOGIN_VALID_CODE("sysLoginValidCode", "false", "是否支持登入验证码"),
		
		SYS_LOGIN_DOMAIN_LDAP("sysLoginDomainLdap", "ldap://10.0.15.2:389/", "域认证地址"),
		SYS_ACTION_SUPPORT_IE8("sysActionSupportIE8", "是否支持IE8", ""),
		SYS_ENABLE_CACHE("sysEnableCache", "true", "是否启用用户数据缓存功能，如ws服务查询结果缓存"),
		SYS_FIND_IP_MAC("sysFindIpMac", "false", "是否反向查用户IP的MAC地址"),
		SYS_FIND_IP_MAC_THREAD_COUNT("sysFindIpMacThreadCount", "10", "多线程udp查客户端mac地址的线程数"),
		SYS_WEB_REAL_PATH("sysWebRealPath", "/", "系统实际目录"),
		SYS_IF_TEST("sysIfTest", "false", "是否测试系统");
		private String code;
		private String defaultValue;
		private String remark;
		config_key(String code, String defaultValue, String remark){
			this.code=code;
			this.defaultValue=defaultValue;
			this.remark=remark;
		}
		
		public String getCode() {
			return code;
		}

		public static config_key getConfigKey(String code){
			config_key[] keys = config_key.values();
			config_key rKey=null;
			for(config_key key:keys){
				if(key.code.equals(code)){
					rKey=key;
					break;
				}
			}
			return rKey;
		}
		
	}
	
	public static String getConfigValue(String code, String defaultValue){
		config_key key = config_key.getConfigKey(code);
		if(key==null){
			if(config!=null){
				return ""+config.get(code);
			}
			else{
//				log4.error("-----Invalid config code:"+code);
				return defaultValue;
			}
		}
		return getConfigValue(key);
	}
	
	public static String getConfigValue(String code){
		config_key key = config_key.getConfigKey(code);
		if(key==null){
			if(config!=null){
				Object v=config.get(code);
				if(v!=null){
					return ""+v;
				}
				else{
					log4.error("-----Invalid config code:"+code);
					return null;
				}
			}
			else{
				log4.error("-----Invalid config code:"+code);
				return null;
			}
		}
		return getConfigValue(key);
	}
	
	public static Integer getConfigValueInt(String code){
		if(code==null){
			return 0;
		}
		
		String value=getConfigValue(code);
//		return CommMethod.getIntegerValue(value);
		if(value==null||"".equals(value)||"null".equals(value)){
			return 0;
		}
		else{
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
				log4.warn("---getConfigValueInt--code="+code+" value="+value+" error:"+e.getMessage());
				return 0;
			}
		}
	}
	
	public static Boolean getConfigValueBool(String code){
		String value = getConfigValue(code);
		return "true".equalsIgnoreCase(value);
	}
	
	public static String getConfigValue(config_key cKey){
		Object obj = null;
		if(config!=null){
			obj=config.get(cKey.code);
		}
		if(initMap.get(cKey.code)!=null){
			return ""+initMap.get(cKey.code);
		}
		if(obj==null){
			return cKey.defaultValue;
		}
		else{
			return ""+obj;
		}
	}
	
	public static void setConfigValue(config_key cKey, String value){
		if(config!=null){
			config.setProperty(cKey.code, value);
		}
		else{
			initMap.put(cKey.code, value);
		}
	}
}
