package com.babel.common.core.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class ObjectToMapUtil {
	private static  Logger logger=Logger.getLogger(ObjectToMapUtil.class);
	/**
	 * 对象转map
	 * @param bean
	 * @return
	 * @throws IntrospectionException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static Map<String, Object> objectToMap(Object bean) 
            throws IntrospectionException, IllegalAccessException, InvocationTargetException { 
        Class type = bean.getClass(); 
        Map<String, Object> returnMap = new HashMap<>(); 
        BeanInfo beanInfo = Introspector.getBeanInfo(type); 

        PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors(); 
        for (int i = 0; i< propertyDescriptors.length; i++) { 
            PropertyDescriptor descriptor = propertyDescriptors[i]; 
            String propertyName = descriptor.getName(); 
            if (!propertyName.equals("class")) { 
                Method readMethod = descriptor.getReadMethod(); 
                Object result = readMethod.invoke(bean, new Object[0]); 
                if (result != null) { 
                    returnMap.put(propertyName, result); 
                } else { 
//                    returnMap.put(propertyName, ""); 
                } 
            } 
        } 
        return returnMap; 
    } 
	
	
	/**
	 * map转对象
	 * @param map
	 * @param beanClass
	 * @return
	 * @throws Exception
	 */
	 public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {    
	        if (map == null)   
	            return null;    
	  
	        Object obj = beanClass.newInstance();  
	  
	        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());    
	        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();    
	        Object value=null;
	        for (PropertyDescriptor property : propertyDescriptors) {  
	            Method setter = property.getWriteMethod();    
	            if (setter != null) {
	            	value=map.get(property.getName());
	            	if(value!=null){
	            		setter.invoke(obj, value);
	            	}
	            }  
	        }
	  
	        return obj;  
	    }
	 
	 /**
	  * 自动给对象从map参数传值
	  * @param data
	  * @param params
	  * @throws Exception
	  */
	 public static void loadMap(Object data, Map<String, Object> params) throws Exception {
	    	BeanInfo beanInfo = Introspector.getBeanInfo(data.getClass());
			PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors(); 
			Object value=null;
			String propType=null;
	        for (int i = 0; i< propertyDescriptors.length; i++) { 
	            PropertyDescriptor descriptor = propertyDescriptors[i]; 
	            String propertyName = descriptor.getName();
	            value=params.get(propertyName);
	            if(value!=null && descriptor.getWriteMethod()!=null){
	            	try {
						propType=descriptor.getPropertyType().getName();
						if("java.lang.String".equals(propType)){
							descriptor.getWriteMethod().invoke(data, value);
						}
						else if("java.lang.Integer".equals(propType)){
							descriptor.getWriteMethod().invoke(data, Integer.valueOf(""+value));
						}
						else if("java.lang.Long".equals(propType)){
							descriptor.getWriteMethod().invoke(data, Long.valueOf(""+value));
						}
						else if("java.lang.Float".equals(propType)){
							descriptor.getWriteMethod().invoke(data, Float.valueOf(""+value));
						}
						else if("java.lang.Double".equals(propType)){
							descriptor.getWriteMethod().invoke(data, Double.valueOf(""+value));
						}
						else if("java.util.Date".equals(propType)){
							if(value instanceof Date){
								descriptor.getWriteMethod().invoke(data, value);
							}
							else{
								descriptor.getWriteMethod().invoke(data, DateUtils.parse(""+value, DateUtils.SDF_FORMAT_DATETIME));
							}
						}
						else{
							logger.info("-----"+beanInfo.getBeanDescriptor().getDisplayName()+" properName="+propertyName+" propType="+propType+" not found");;
							descriptor.getWriteMethod().invoke(data, value);
						}
					} catch (Exception e) {
						logger.warn("----loadMap--propertyName="+propertyName+" propType="+propType+" value="+value, e);
					}
	            }
	        }
		}
	 
	 /**
	  * 从对象中获取指定的属性
	  * @param bean
	  * @param property
	  * @return
	  * @throws IntrospectionException
	  * @throws IllegalAccessException
	  * @throws InvocationTargetException
	  */
	public static Object getObjectByProperty(Object bean, String property)
			throws IntrospectionException, IllegalAccessException, InvocationTargetException {
		if (StringUtils.isEmpty(property)) {
			return null;
		}
		property = property.trim();

		Class type = bean.getClass();
		BeanInfo beanInfo = Introspector.getBeanInfo(type);

		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (int i = 0; i < propertyDescriptors.length; i++) {
			PropertyDescriptor descriptor = propertyDescriptors[i];
			String propertyName = descriptor.getName();
			if (!propertyName.equals("class") && propertyName.equals(property)) {
				Method readMethod = descriptor.getReadMethod();
				Object result = readMethod.invoke(bean, new Object[0]);
				return result;
			}
		}
		return null;
	}

	/**
	 * 
	 * ��vo����getType���ͣ���vo����ֵ���ҳ�propNames��Ӧ�����Լ�����ֵ����map����
	 * @param obj
	 * @param propNames
	 * @param getType include/ignore
	 * @return
	 */
	public static Map<String, Object> getDataMapByPropName(Object data, String propNames, String getType) throws Exception{
		Map<String, Object> map=new HashMap<String, Object>();
		if(data==null){
			return map;
		}
		
		if(propNames==null){
			propNames="";
		}
		if(getType==null){
			getType="ignore";
		}
		String[] props=propNames.split(",");
		for(int i=0; i<props.length; i++){
			props[i]=props[i].trim();
		}
		
		BeanInfo beanInfo = Introspector.getBeanInfo(data.getClass()); 

		boolean isIgnore=false;
		
		PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors(); 
        for (int i = 0; i< propertyDescriptors.length; i++) { 
            PropertyDescriptor descriptor = propertyDescriptors[i]; 
            String propertyName = descriptor.getName();
            if (!propertyName.equals("class")) { 
                Method readMethod = descriptor.getReadMethod(); 
                Object result = readMethod.invoke(data, new Object[0]);
                if (result != null) {
                	//忽略模式
                	if("ignore".equals(getType)){
    					isIgnore=false;
    					for(String prop:props){
    						if(propertyName.equals(prop)){
    							isIgnore=true;
    							break;
    						}
    					}
    					if(!isIgnore){
    						map.put(propertyName, result); 
    					}
                	}
                	//包含模式
                	else if("include".equals(getType)){
                		for(String prop:props){
                			if(propertyName.equals(prop)){
                				map.put(propertyName, result);
                				break;
                			}
                		}
                	}
                    
                } else { 
//                    returnMap.put(propertyName, ""); 
                } 
            } 
        } 
		return map;
	}
	
	/**
	 * ��voList�и���ָ����propName���ɸ�propName��Ӧֵ��list
	 * @param list
	 * @param propName
	 * @return
	 */
	public static <T> List<T> getDataPropListByName(Collection list, String propName) throws Exception{
		List<T> propDataList=new ArrayList<T>();
		
		if(list==null||list.size()==0){
			return propDataList;
		}
		
		for(Object obj:list){
			propDataList.add((T)getObjectByProperty(obj, propName));
		}
		return propDataList;
	}
	
	/**
	 * ��voList�и���getType����ȡָ����propNames��Ӧ�����Լ�����ֵ����mapList
	 * @param dataList
	 * @param propNames
	 * @param getType include/ingore
	 * @return
	 */
	public static List<Map<String, Object>> getDataMapListByPropName(Collection dataList
			, String propNames, String getType) throws Exception{
		List<Map<String, Object>> mapList=new ArrayList<Map<String, Object>>();
		Map<String, Object> map=null;
		for(Object data:dataList){
			map=getDataMapByPropName(data, propNames, getType);
			if(map!=null){
				mapList.add(map);
			}
		}
		return mapList;
	}
	
	/**
	 * ��mapList�е�ÿ��map��ȡһ��key��ֵ��Ϊlist����
	 * @param mapList
	 * @param key
	 * @return
	 */
	public static List getDataMapListByKey(Collection<Map> mapList, Object key){
		List list=new ArrayList();
		for(Map map:mapList){
			if(map.containsKey(key)){
				list.add(map.get(key));
			}
		}
		return list;
	}
	
	/**
	 * ��mapList�е�ÿ��map��ȡ����key��Ϊ�µ�map��key��value����
	 * @param mapList
	 * @param propKey
	 * @param propValue
	 * @return
	 */
	public static Map getDataMapListByKey(Collection<Map<String, Object>> mapList, String propKey, String propValue){
		Map dataMap= new HashMap();
		for(Map<String, Object> map:mapList){
			if(map.containsKey(propKey) && map.containsKey(propValue)){
				dataMap.put(map.get(propKey), map.get(propValue));
			}
		}
		return dataMap;
	}
	
	/**
	 * 清空指定属性值，多个用","隔开
	 * @param collection
	 * @param propKey
	 * @throws Exception
	 */
	public static void clearFields(Collection collection, String propKey)throws Exception{
		if(collection==null || collection.size()==0){
			return;
		}
		String[] props=propKey.split(",");
		for(int i=0; i<props.length; i++){
			props[i]=props[i].trim();
		}
		
		MethodDescriptor[] methods = getMethodList(collection);
		Map<String, MethodDescriptor> methodMap = new HashMap<String, MethodDescriptor>();
		for (MethodDescriptor method : methods) {
			methodMap.put(method.getName().toLowerCase(), method);
		}
		
		List<String> hideProps = new ArrayList<String>();
		for (MethodDescriptor method : methods) {
			for (String prop : props) {
				if(method.getName().toLowerCase().equals("set"+prop.toLowerCase())) {
					hideProps.add(prop);
				}
			}
		}
		clearFields(collection, hideProps, methodMap);
	}
	
	/**
	 * 清空指定的属性除外的其它属性的值，多个用","隔开
	 * @param collection
	 * @param propKey
	 * @throws Exception
	 */
	public static void clearFieldsWithout(Collection collection, String propKey)throws Exception{
		if(collection==null || collection.size()==0){
			return;
		}
		String[] props=propKey.split(",");
		for(int i=0; i<props.length; i++){
			props[i]=props[i].trim();
		}
		
		MethodDescriptor[] methods = getMethodList(collection);
		Map<String, MethodDescriptor> methodMap = new HashMap<String, MethodDescriptor>();
		for (MethodDescriptor method : methods) {
			methodMap.put(method.getName().toLowerCase(), method);
		}
		
		List<String> hideProps = new ArrayList<String>();
		boolean setNull;
		for (MethodDescriptor method : methods) {
			setNull = true;
			for (String prop : props) {
				if(method.getName().toLowerCase().equals("set"+prop.toLowerCase())) {
					setNull = false;
					break;
				}
			}
			if(setNull && method.getName().startsWith("set")) {
				hideProps.add(method.getName().replaceFirst("set", "").toLowerCase());
			}
		}
		clearFields(collection, hideProps, methodMap);
	}
	
	/**
	 * 清除Collection指定属性的值
	 * @param collection
	 * @param hideProps 要设为null的属性值
	 * @param methodMap 方法集合
	 * @throws Exception
	 */
	private static void clearFields(Collection collection, List<String> hideProps, Map<String, MethodDescriptor> methodMap)throws Exception{
		MethodDescriptor setmethod = null;
		for(Object obj:collection){
			for (String prop : hideProps) {
				setmethod = (MethodDescriptor) methodMap.get("set" + prop.toLowerCase());
				Method readMethod = setmethod.getMethod();
				readMethod.invoke(obj, new Object[]{null});
			}
		}
	}
	
	/**
	 * 获取对像的所有方法，以map方式返回
	 * @param obj
	 * @return
	 * @throws IntrospectionException 
	 */
	private static MethodDescriptor[] getMethodList(Collection collection) throws IntrospectionException {
		Object obj = null;
		for(Object o:collection){
			obj = o;
			break;
		}
		if(obj==null){
			return null;
		}
		BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
		return beanInfo.getMethodDescriptors();
	}
}
