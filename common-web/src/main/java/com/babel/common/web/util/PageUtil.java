package com.babel.common.web.util;

import javax.servlet.http.HttpServletRequest;

public class PageUtil {
	public static String getPreBasePath(HttpServletRequest request) {
		String serverName = request.getServerName();
		String port = "";
		port = ":"+request.getServerPort();
		return request.getScheme()+"://"+serverName+port;
	}
}
