package com.babel.common.core.util;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

public class CommUtil {
	private static  Logger logger=Logger.getLogger(CommUtil.class); 
	public static boolean isObjectEqual(Object obj1, Object obj2){
		if(obj1==null && obj2==null){
			return true;
		}
		else if(obj1!=null && obj2==null){
			return false;
		}
		else if(obj1!=null){
			if(obj1 instanceof Number){
				if(obj1 instanceof Integer||obj1 instanceof Long){
					long v1=((Number)obj1).longValue();
					long v2=Long.parseLong(""+obj2);
					return v1==v2;
				}
				else if(obj1 instanceof Float || obj1 instanceof Double){
					double v1=((Number)obj1).doubleValue();
					double v2=Double.parseDouble(""+obj2);
					return v1==v2;
				}
				else if(obj1 instanceof Boolean){
					Boolean v1=(Boolean)obj1;
					Boolean v2=Boolean.valueOf(""+obj2);
					return v1==v2;
				}
				else{
//					System.out.println("----");
					return obj1.equals(obj2);
				}
			
			}
			else{
//				System.out.println("----"+obj1);
				return obj1.equals(obj2);
			}
			
			
		}
//		System.out.println("---end-");
		return false;
	}
	
	public static Object nvl(Object ...objects ){
		for(Object obj:objects){
			if(obj!=null){
				return obj;
			}
		}
		return null;
	}
	
	public static Object decode(Object ...objects){
		Object dataFirst=null;
		Object data=null;
		int i=0;
		for(Object cur:objects){
			if(dataFirst==null){
				dataFirst=cur;
			}
			else if(i%2==1&& isObjectEqual(dataFirst, cur)){
				data=cur;
				return data;
			}
			i++;
		}
		return data;
	}
	
	public static String concat(Collection list, String splitChar){
		splitChar=(String)nvl(splitChar, "");
		
		String str="";
		for(Object obj:list){
			str+=obj+splitChar;
		}
		
		str=str.trim();
		if(str.endsWith(splitChar)){
			str=str.substring(0, str.length()-splitChar.length());
		}
		return str;
	}
	
	public static String concat(Object[] list, String splitChar){
		splitChar=(String)nvl(splitChar, "");
		
		String str="";
		for(Object obj:list){
			str+=obj+splitChar;
		}
		
		str=str.trim();
		if(str.endsWith(splitChar)){
			str=str.substring(0, str.length()-splitChar.length());
		}
		return str;
	}
	
	public static String concat(Set sets, String splitChar){
		splitChar=(String)nvl(splitChar, "");
		
		String str="";
		Iterator itors=sets.iterator();
		while(itors.hasNext()){
			str+=itors.next()+splitChar;
		}
		if(str.endsWith(splitChar)){
			str=str.substring(0, str.length()-splitChar.length());
		}
		return str;
	}

	
	public static boolean isAllEmpty(Object ... objs){
		boolean isAllEmpty=true;
		for(Object obj:objs){
			if(obj!=null && !"".equals(obj)){
				isAllEmpty=false;
				return isAllEmpty;
			}
		}
		return isAllEmpty;
	}
	public static boolean isAllEmpty(List ... lists){
		boolean isAllEmpty=true;
		for(List list:lists){
			if(list!=null && list.size()>0){
				isAllEmpty=false;
				return isAllEmpty;
			}
		}
		return isAllEmpty;
	}
	public static boolean isAllEmpty(Object[] ... arrays){
		boolean isAllEmpty=true;
		for(Object[] objs:arrays){
			if(objs!=null && objs.length>0){
				isAllEmpty=false;
				return isAllEmpty;
			}
		}
		return isAllEmpty;
	}
	
	/**
	 * 字符个数查找
	 * @param src
	 * @param find
	 * @return
	 */
	public static int findStrCount(String src, String find) {
		int count = 0;
		int index = -1;
		if(src==null || find==null){
			return count;
		}
		while ((index = src.indexOf(find, index)) > -1) {
			++index;
			++count;
		}
		
//		int counter = 0;
//        for (int i = 0; i <= src.length() - find.length(); i++) {
//            if (src.substring(i, i + find.length()).equalsIgnoreCase(find)) {
//                counter++;
//            }
//        }
		return count;
	}
	
	public static boolean isExist(List list, Object obj){
		for(Object cur:list){
			if(isObjectEqual(cur, obj)){
				return true;
			}
		}
		return false;
	}
	
	public static boolean isNotExist(List list, Object obj){
		for(Object cur:list){
			if(isObjectEqual(cur, obj)){
				return false;
			}
		}
		return true;
	}
	
	public static boolean isAllExist(Object key, Object value, List<Map<Object, Object>> ...lists){
		boolean isAllExist=true;
		boolean isExist=false;
		for(List<Map<Object, Object>> mapList: lists){
			isExist=false;
			for(Map<Object, Object> map:mapList){
				if(map.containsKey(key) && isObjectEqual(map.get(key), value)){
					isExist=true;
					break;
				}
			}
			if(!isExist){
				return false;
			}
		}
		
		return isAllExist;
	}
	
	public static boolean isAllNotExist(Object key, Object value, List<Map<Object, Object>> ...lists){
		boolean isAllNotExist=true;
		boolean isExist=false;
		for(List<Map<Object, Object>> mapList: lists){
			isExist=false;
			for(Map<Object, Object> map:mapList){
				if(map.containsKey(key) && isObjectEqual(map.get(key), value)){
					isExist=true;
					break;
				}
			}
			if(isExist){
				return false;
			}
		}
		
		return isAllNotExist;
	}
	
	
	public static List newList(Object ...objects){
		List list = new ArrayList();
		for(Object obj:objects){
			list.add(obj);
		}
		return list;
	}
	
	public static Set newSet(Object ...objects ){
		Set sets = new HashSet();
		for(Object obj:objects){
			sets.add(obj);
		}
		return sets;
	}
	
//	public static Object newVo(String propName, Class clazz) throws Exception{
//		Object obj=null;
//		obj=clazz.newInstance();
//		
//		return obj;
//	}
	
	public static Object[] newArray(Object ...objects){
		Object[] array=new Object[objects.length];
		for(int i=0; i<objects.length; i++){
			array[i]=objects[i];
		}
		return array;
	}
	
	/**
	 * ��������VO�����voList�����ɸ�ָ������������id
	 * @param size ����
	 * @param start ��ʼ���
	 * @param step ����
	 * @param propName
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public static List newVoList(int size, int start, int step, String propName, Class clazz) throws Exception{
		List list = new ArrayList();
		
		Object obj=null;
		
		String propNameUp1=propName.substring(0, 1).toUpperCase()+propName.substring(1);
		String methodName_set="set"+propNameUp1;
		
//		String methodName_get="get"+propNameUp1;
//		Method methodGet=clazz.getDeclaredMethod(methodName_get, new Class[]{});
		
		
		Method methodSet=null;
		Method[] methods = clazz.getDeclaredMethods();
		for(Method method:methods){
			if(methodName_set.equals(method.getName())){
				methodSet=method;
				break;
			}
		}
		
		if(methodSet==null){
			logger.warn("----newVoList-methodName_set="+methodName_set+" not found");
			return list;
		}
		
		Type[] paraTypes=methodSet.getGenericParameterTypes();
		String typeName=paraTypes[0].getTypeName();
		int value=0;
		Object[] paraObjs=null;
		for(int i=0; i<size; i++){
			value = start+i*step;
			obj=clazz.newInstance();
			if("java.lang.Long".equals(typeName)){
				paraObjs=new Object[]{Long.valueOf(value)};
			}
			else if("java.lang.String".equals(typeName)){
				paraObjs=new Object[]{""+value};
			}
			methodSet.invoke(obj, paraObjs);
			list.add(obj);
		}
		return list;
	}
	
	/**
	 * ��˳��ÿ�������һmap��key��value
	 * @param objs
	 * @return
	 */
	public static Map newMap(Object ... objs){
		Map map = new HashMap();
		int size=objs.length/2;
		for(int i=0; i<size; i++){
			map.put(objs[i*2], objs[i*2+1]);
		}
		return map;
	}
	
	/**
	 * ������List��˳��һһ��Ӧ�����һ���µ�map
	 * ע������С���ȵ�Ϊ׼���������ݺ���
	 * @param keyList
	 * @param valueList
	 * @return
	 */
	public static Map zipMap(List keyList, List valueList){
		Map map = new HashMap();
		int size=keyList.size();
		if(size>valueList.size()){
			logger.warn("----map--keyList size> valueList size");
		}
		else if(size<valueList.size()){
			size=valueList.size();
			logger.warn("----map--keyList size< valueList size");
		}
		
		for(int i=0; i<size; i++){
			map.put(keyList.get(i), valueList.get(i));
		}
		return map;
	}
	
	public static String getStringLimit(String str, int maxLength){
		if(str!=null && str.length()>maxLength){
			return str.substring(0, maxLength);
		}
		return str;
	}
	
	public static String[] split(String str, String exp){
		String[] arrays=null;
		if(str!=null && exp!=null){
			arrays=str.split(exp);
		}
		return arrays;
	}
	
	/**
	 * 判断集合是否为空
	 * @param list
	 * @return
	 */
	public static <T> boolean isEmpty(Collection<T> c){
		return c == null || c.isEmpty();
	}
	
	/**
	 * 判断字符串是否等于null或空串
	 * @param list
	 * @return
	 */
	public static boolean isEmpty(String str){
		return str == null || str.trim().equals("");
	}
	
	
	/**
	 * 查找el表达式
	 * @param str
	 * @return
	 */
	private static String findEl(String str){
		int find=str.indexOf("#{");
		if(find<0){
			return null;
		}
		String tmp=str.substring(find);
		if(tmp.indexOf("}")<0){
			return null;
		}
		return tmp.substring(0, tmp.indexOf("}")+1);
	}
	
	/**
	 * 递归替换el表达式中的#{}数据
	 * 支持#{0},#{1}
	 * 支持对象#{0.code},#{0.name}
	 * @param str
	 * @param paramMap
	 * @return
	 */
	public static String replaceElByMap(String str, Map<String, Object> paramMap){
		String el=findEl(str);
		if(el==null){
			return str;
		}
		String v=el.substring(2, el.length()-1);
		Object value=null;
		int f_found=v.indexOf(".");
		if(f_found>0){//#{0.attr}
			String k=v.substring(0, f_found);
			String attr=v.substring(f_found+1);
			Object obj=paramMap.get(k);
			if(obj instanceof Map){
				Map<String, Object> kMap=(Map<String, Object>)obj;
				value=kMap.get(attr);
			}
		}
		else{//#{0}
			 value=paramMap.get(v);
		}
		if(value==null){
			value=v;
		}
		str=str.replace(el, ""+value);
		return replaceElByMap(str, paramMap);
	}
	
	/**
	 * 如果是内网IP，则截取前面部份的IP地址
	 * @param host
	 * @return
	 */
	public static String getHostShort(String host){
		if(host!=null){
			if(host.startsWith("192.168.")){
				host=host.replace("192.168.", "");
			}
			else if(host.startsWith("10.")){
				host=host.replace("10.", "");
			}
		}
		return host;
	}
	
	public static String getNumStr(Long num,int length){
		if(num==null){
			num=0l;
		}
		String timeStr=""+num;
		if(timeStr.length()<=length){
			int len=length-timeStr.length();
			String start="";
			for(int i=0; i<len; i++){
				start+="0";
			}
			timeStr=start+timeStr;
		}
		return timeStr;
	}
    
    public static String getNumStr(String num,int length){
    	if(num==null){
    		num="0";
    	}
		String timeStr=""+num;
		if(timeStr.length()<=length){
			int len=length-timeStr.length();
			String start="";
			for(int i=0; i<len; i++){
				start+="0";
			}
			timeStr=start+timeStr;
		}
		return timeStr;
	}
}

