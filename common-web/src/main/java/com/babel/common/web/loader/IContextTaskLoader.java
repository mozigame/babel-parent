package com.babel.common.web.loader;

import javax.servlet.ServletContextEvent;

import com.babel.common.core.data.RetResult;

public interface IContextTaskLoader {
	public RetResult<String> execute(ServletContextEvent event);
}
