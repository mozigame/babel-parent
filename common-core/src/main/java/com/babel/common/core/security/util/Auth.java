package com.babel.common.core.security.util;

import java.util.Hashtable;
import java.util.Random;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

/**
 * 密码验证算法
 * 加密方式基于md5，并在里面指定的位置插入随机数字，也在前面加上一个5以内的随机数字，并且于在后面生成随机字符
 * @author jinhe.chen
 *
 */
public class Auth {
	public static final String SYS_LOGIN_DOMAIN_LDAP="SYS_LOGIN_DOMAIN_LDAP";
	public static final String ENCRYPT_MD5_SALT="yc";
	public static final int[] ENCRYPT_MD5_RANDOM={1,4,11,20};
	public static final String ENCRYPT_BASE="abcdefghijklmn#o%pqrstuvwxyz0123456789";
	private static Logger logger = Logger.getLogger(Auth.class);
	
	private String instString(String a,String b,int t){
		return a.substring(0,t)+b+a.substring(t, a.length());
	}
	private String removeString(String a, int t){
		return a.substring(0, t)+a.substring(t+1, a.length());
	}
	
	private String getRandomString(int length) { //length表示生成字符串的长度
	    Random random = new Random();   
	    StringBuffer sb = new StringBuffer();   
	    for (int i = 0; i < length; i++) {   
	        int number = random.nextInt(ENCRYPT_BASE.length());   
	        sb.append(ENCRYPT_BASE.charAt(number));   
	    }   
	    return sb.toString();   
	 }  
	private String encode(String str){
		int v=0;
		int length=str.length();
		int b=0;
		int first=0;
		Random rnd = new Random();
		for(int i=0; i<ENCRYPT_MD5_RANDOM.length; i++){
			v=ENCRYPT_MD5_RANDOM[i];
			if(v<length){
				b=rnd.nextInt(9);
				if(first==0){
					b=rnd.nextInt(5);
					if(b==0){
						b=1;
					}
					first=b;
				}
				str=this.instString(str, ""+b, v+i);
			}
		}
		str=first+str+getRandomString(first);
		return str;
	}
	
	private String decode(String str){
		int v=0;
		String first=str.substring(0, 1);
		int b=Integer.parseInt(first);
		str=str.substring(1);
		for(int i=0; i<ENCRYPT_MD5_RANDOM.length; i++){
			v=ENCRYPT_MD5_RANDOM[i];
			if(v<str.length()){
				str=this.removeString(str, v);
			}
		}
		str=str.substring(0, str.length()-b);
		return str;
	}
	public String encrypt(String username, String password){
		String encrypt_pwd= new Md5PasswordEncoder().encodePassword(password, ENCRYPT_MD5_SALT+username);
		encrypt_pwd=this.encode(encrypt_pwd);
		return encrypt_pwd;
	}
	
	public boolean checkPassword(String passwd1, String passwd2){
		passwd1=this.decode(passwd1);
		passwd2=this.decode(passwd2);
		return passwd1.equals(passwd2);
	}
	
	public boolean authUser(String userName, String passWd) {
		return authUserLdap(userName, passWd);
	}

	public boolean authUserLdap(String userName, String passWd) {
		Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		// login the ldap server
		env.put(Context.PROVIDER_URL, SYS_LOGIN_DOMAIN_LDAP); 
		String domainUserName = "yc\\" + userName; // 注意用户名的写法：domain\User 或
													// User@domain.com
		env.put(Context.SECURITY_PRINCIPAL, domainUserName);
		env.put(Context.SECURITY_CREDENTIALS, passWd);

		try {
			DirContext ctx = new InitialDirContext(env);
			ctx.close();
		} catch (NamingException ex) {
			logger.error("loginAuthDomain error " + userName + ":" + ex.getExplanation());
			return false;
		}
		return true;
	}
}
