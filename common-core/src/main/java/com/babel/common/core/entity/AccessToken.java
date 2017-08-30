package com.babel.common.core.entity;

import java.io.Serializable;
import java.util.Date;

/** 
 * 微信通用接口凭证 
 *  
 * @author zhanglei 
 */  
public class AccessToken implements Serializable {
	private static final long serialVersionUID = 1L; 
	
    // 获取到的凭证  
    private String accessToken;  
    // 凭证有效时间，单位：s 
    private int expiresIn;  
    private Date createDate;
    
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getExpiresIn() {  
        return expiresIn;  
    }  
  
    public void setExpiresIn(int expiresIn) {  
        this.expiresIn = expiresIn;  
    }

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}  
    
}  