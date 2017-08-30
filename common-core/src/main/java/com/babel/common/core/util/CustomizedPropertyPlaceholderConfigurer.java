package com.babel.common.core.util;

import java.util.Map;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class CustomizedPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
//    private static Map ctxPropertiesMap;
 
    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess,
                                     Properties props) throws BeansException {
        super.processProperties(beanFactoryToProcess, props);
        ConfigUtils.setProperties(props);
//        ctxPropertiesMap = new HashMap();
        for (Object key : props.keySet()) {
            String keyStr = key.toString();
            String value = props.getProperty(keyStr);
//            ctxPropertiesMap.put(keyStr, value);
        }
//        System.out.println("------ctxPropertiesMap="+ctxPropertiesMap);
    }
//    public static Object getContextProperty(String name) {
//        return ctxPropertiesMap.get(name);
//    }
    
}