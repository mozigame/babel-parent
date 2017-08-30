package com.babel.common.web.loader;

import java.util.LinkedHashMap;

import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.babel.common.core.data.RetResult;
import com.babel.common.core.util.ServerUtil;
import com.babel.common.core.util.XmlUtil;

/**
 * 用于将spring-security的shiroFilter.filterChainDefinitions读入内存
 * @author 金和
 *
 */
@Service("shiroFilterChainLoader")
public class ShiroFilterChainLoader  implements IContextTaskLoader {
	private static Logger log = Logger.getLogger(ShiroFilterChainLoader.class);
	@Override
	public RetResult<String> execute(ServletContextEvent event) {
		// TODO Auto-generated method stub
		RetResult<String> ret = new RetResult<String>();
		this.initFilterChain();
		return ret;
	}
	

	private void initFilterChain(){
		String fileName = "/spring/spring-security.xml";
		String beanId="shiroFilter";
		String propertyName="filterChainDefinitions";
		if(ServerUtil.filterChainMap==null){
			this.log.info("-----load initFilterChain--start--");
			long time=System.currentTimeMillis();
			ServerUtil.filterChainMap=new LinkedHashMap<>();
			String shiroFilter=XmlUtil.loadSpringXml(fileName, beanId, propertyName);
			if(shiroFilter!=null){
				shiroFilter=shiroFilter.trim();
				String[] filters=shiroFilter.split("\n");
				String key=null;
				String value=null;
				for(String filter:filters){
					filter=filter.trim();
					int index=filter.indexOf("=");
					if(index>0){
						key=filter.substring(0, index);
						key=key.trim();
						value=filter.substring(index+1);
						value=value.trim();
						ServerUtil.filterChainMap.put(key, value);
					}
				}
			}
			this.log.info("-----load initFilterChain--filterChainMapSize="+ServerUtil.filterChainMap.size()+" time="+(System.currentTimeMillis()-time));
		}
	}
}
