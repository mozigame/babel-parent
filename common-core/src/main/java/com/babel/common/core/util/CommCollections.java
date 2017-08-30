package com.babel.common.core.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class CommCollections {
	private static Logger logger=Logger.getLogger(CommCollections.class); 
	
	public static Double sumAll(Object ... objs){
		double total=0;
		Number num=null;
		for(Object obj:objs){
			if(obj==null){
				continue;
			}
			if(obj.getClass().isArray()){
				total+=CommCollectionSum.sumAll((Object[])obj);
				
			}
			else if(obj instanceof List){
				total+=CommCollectionSum.sumAll((List)obj);
			}
			else if(obj instanceof Number){
				num=(Number)obj;
				total+=num.doubleValue();
			}
			else{
				total+=Double.parseDouble(""+obj);
			}
		}

		return total;
	}
	
	public static Double sum(Object ... objs){
		double total=0;
		Number num=null;
		for(Object obj:objs){
			if(obj==null){
				continue;
			}
			if(obj.getClass().isArray()){
				total+=CommCollectionSum.sum((Object[])obj);
				
			}
			else if(obj instanceof List){
				total+=CommCollectionSum.sum((List)obj);
			}
			else if(obj instanceof Number){
				num=(Number)obj;
				total+=num.doubleValue();
			}
			else{
				total+=Double.parseDouble(""+obj);
			}
		}

		return total;
	}
	
	public static List cal(List list, Object v){
		return CommCollectionCal.cal(list, v);
	}
	
	public static Object[] cal(Object[] array, Object v){
		return CommCollectionCal.cal(array, v);
	}
	
	public static List findDataList(List list, String propName, Object value) throws Exception{
		if(list==null||list.size()==0){
			return list;
		}
		List rList=new ArrayList();
		Object obj=list.get(0);
		Class clazz=obj.getClass();
		String propNameUp1=propName.substring(0, 1).toUpperCase()+propName.substring(1);
		String methodGet="get"+propNameUp1;
		Method method=clazz.getDeclaredMethod(methodGet, null);
		Object dataValue=null;
		for(Object data:list){
			dataValue=method.invoke(data, null);
			if(dataValue!=null && CommUtil.isObjectEqual(dataValue, value)){
				rList.add(data);
			}
		}
		
		return rList;
	}
	
	public static Object findDataFirst(List list, String propName, Object value) throws Exception{
		if(list==null||list.size()==0){
			return null;
		}
		Object obj=list.get(0);
		Class clazz=obj.getClass();
		String propNameUp1=propName.substring(0, 1).toUpperCase()+propName.substring(1);
		String methodGet="get"+propNameUp1;
		Method method=clazz.getDeclaredMethod(methodGet, null);
		Object dataValue=null;
		for(Object data:list){
			dataValue=method.invoke(data, null);
			if(dataValue!=null && CommUtil.isObjectEqual(dataValue, value)){
				return data;
			}
		}
		
		return null;
	}
	
	public static List<Map<String, Object>> findMapDataList(List<Map<String, Object>> mapList
			, String key, String value){
		List<Map<String, Object>> rList=new ArrayList<Map<String, Object>>();
		
		for(Map<String, Object> map:mapList){
			if(map.containsKey(key) && map.get(key)!=null 
					&& CommUtil.isObjectEqual(map.get(key), value)){
				rList.add(map);
			}
		}
		
		return rList;
	}
	
	public static Map<String, Object> findMapDataFirst(List<Map<String, Object>> mapList
			, String key, String value){
		for(Map<String, Object> map:mapList){
			if(map.containsKey(key) && map.get(key)!=null
					&& CommUtil.isObjectEqual(map.get(key), value)){
				return map;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param size
	 * @param start
	 * @param step
	 * @return
	 */
	public static List<Integer> genList(int size, int start, int step){
		List<Integer> vList=new ArrayList<Integer>();
		int v=0;
		for(int i=0; i<size; i++){
			v=start+i*step;
			vList.add(v);
		}
		return vList;
	}
	
	public static Integer[] genArray(int size, int start, int step){
		Integer[] arrays=new Integer[size];
		int v=0;
		for(int i=0; i<size; i++){
			arrays[i]=start+i*step;
		}
		return arrays;
	}
	
	public static List<String> filter(List<String> list, String filter){
		if(filter==null){
			return list;
		}
		List<String> rList=new ArrayList<String>();
		filter=filter.trim();
		if(filter.indexOf("%")<0 || filter.startsWith("%") && filter.endsWith("%")){
			if(filter.indexOf("%")>0){
				filter=filter.substring(1, filter.length()-1);
			}
			for(String l:list){
				if(l.indexOf(filter)>=0){
					rList.add(l);
				}
			}
		}
		else if(filter.startsWith("%")){
			filter=filter.substring(1);
			for(String l:list){
				if(l.endsWith(filter)){
					rList.add(l);
				}
			}
		}
		else if(filter.endsWith("%")){
			filter=filter.substring(0, filter.length()-1);
			for(String l:list){
				if(l.startsWith(filter)){
					rList.add(l);
				}
			}
		}
		
		return rList;
	}
	
	public static <T> List<T> obj2List(T[] arrays){
		List<T> list=new ArrayList<T>();
		for(T t: arrays){
			list.add(t);
		}
		return list;
	}
	
	public static Object getFirst(Collection list){
		for(Object o:list){
			return o;
		}
		return null;
	}
	
	public static Object getFirst(Object[] arrays){
		if(arrays!=null && arrays.length>0){
			return arrays[0];
		}
		return null;
	}
	
}

class CommCollectionSum{
	private static Integer sumInt(List<Integer> list){
		int sum=0;
		for(Integer num:list){
			sum+=num;
		}
		return sum;
	}
	private static Integer sumInt(Integer[] list){
		int sum=0;
		for(Integer num:list){
			sum+=num;
		}
		return sum;
	}
	private static long sumLong(List<Long> list){
		long sum=0;
		for(Long num:list){
			sum+=num;
		}
		return sum;
	}
	private static long sumLong(Long[] list){
		long sum=0;
		for(Long num:list){
			sum+=num;
		}
		return sum;
	}
	private static float sumFloat(List<Float> list){
		float sum=0;
		for(Float num:list){
			sum+=num;
		}
		return sum;
	}
	private static float sumFloat(Float[] list){
		float sum=0;
		for(Float num:list){
			sum+=num;
		}
		return sum;
	}
	private static double sumDouble(List<Double> list){
		float sum=0;
		for(Double num:list){
			sum+=num;
		}
		return sum;
	}
	private static double sumDouble(Double[] list){
		float sum=0;
		for(Double num:list){
			sum+=num;
		}
		return sum;
	}
	
	public static Double sum(List ... lists){
		double total=0;
		Object obj=null;
		for(List list:lists){
			if(list.size()>0){
				obj=list.get(0);
				if(obj instanceof Integer){
					total+=sumInt(list);
				}
				else if(obj instanceof Long){
					total+=sumLong(list);
				}
				else if(obj instanceof Float){
					total+=sumFloat(list);
				}
				else if(obj instanceof Double){
					total+=sumDouble(list);
				}
				else{
					for(Object cur:list){
						total+=Double.parseDouble(""+cur);
					}
				}
			}
		}
		return total;
	}
	
	public static Double sumAll(Object[] array){
		double total=0;
		Number num=null;
		for(Object obj:array){
			if(obj instanceof Number){
				total+=((Number)obj).doubleValue();
			}
			else{
				total+=Double.parseDouble(""+obj);
			}
		}
		return total;
	}
	
	public static Double sumAll(List list){
		double total=0;
		Number num=null;
		for(Object obj:list){
			if(obj instanceof Number){
				total+=((Number)obj).doubleValue();
			}
			else{
				total+=Double.parseDouble(""+obj);
			}
		}
		return total;
	}
	
	public static Double sum(Object[] ... arrays){
		double total=0;
		Object obj=null;
		for(Object[] list:arrays){
			if(list.length>0){
				obj=list[0];
				if(obj instanceof Integer){
					Integer[] a=(Integer[])list;
					total+=sumInt(a);
				}
				else if(obj instanceof Long){
					Long[] a=(Long[])list;
					total+=sumLong(a);
				}
				else if(obj instanceof Float){
					Float[] a=(Float[])list;
					total+=sumFloat(a);
				}
				else if(obj instanceof Double){
					Double[] a=(Double[])list;
					total+=sumDouble(a);
				}
				else{
					for(Object cur:list){
						total+=Double.parseDouble(""+cur);
					}
				}
			}
		}
		return total;
	}
	
	
}

class CommCollectionCal{
	private static Logger logger=Logger.getLogger(CommCollections.class); 
	private static List<Long> cal(List<Long> list, long v){
		List<Long> rList=new ArrayList<Long>();
		for(Long d:list){
			rList.add(d+v);
		}
		return rList;
	}
	
	private static Long[] cal(Long[] list, long v){
		for(int i=0; i<list.length; i++){
			list[i]=list[i]+v;
		}
		return list;
	}
	
	private static List<Integer> cal(List<Integer> list, int v){
		List<Integer> rList=new ArrayList<Integer>();
		for(Integer d:list){
			rList.add(d+v);
		}
		return rList;
	}
	
	private static Integer[] cal(Integer[] list, int v){
		for(int i=0; i<list.length; i++){
			list[i]=list[i]+v;
		}
		return list;
	}
	
	private static List<Float> cal(List<Float> list, float v){
		List<Float> rList=new ArrayList<Float>();
		for(Float d:list){
			rList.add(d+v);
		}
		return rList;
	}
	
	private static Float[] cal(Float[] list, float v){
		for(int i=0; i<list.length; i++){
			list[i]=list[i]+v;
		}
		return list;
	}
	
	private static List<Double> cal(List<Double> list, double v){
		List<Double> rList=new ArrayList<Double>();
		for(Double d:list){
			rList.add(d+v);
		}
		return rList;
	}
	
	private static Double[] cal(Double[] list, double v){
		for(int i=0; i<list.length; i++){
			list[i]=list[i]+v;
		}
		return list;
	}
	
	private static String[] cal(String[] list, String v){
		for(int i=0; i<list.length; i++){
			list[i]=list[i]+v;
		}
		return list;
	}
	
	
	

	
	/**
	 * �Ӽ��˳�����
	 * @param obj1 ����
	 * @param obj2 ����
	 * @param type +-* /
	 * @return
	 */
	public static Double cal(Object obj1, Object obj2, String type){
		double sum=0;
		Number num1=null;
		Number num2=null;
		if(type==null){
			type="+";
		}
		if(obj1==null||obj2==null){
			return sum;
		}
		if(obj1 instanceof Number){
			num1=(Number)obj1;
		}
		else{
			num1=Double.parseDouble(""+obj1);
		}
		
		if(obj2 instanceof Number){
			num2=(Number)obj2;
		}
		else{
			num2=Double.parseDouble(""+obj2);
		}
		if("+".equals(type)){
			sum=num1.doubleValue()+num2.doubleValue();
		}
		else if("-".equals(type)){
			sum=num1.doubleValue()-num2.doubleValue();
		}
		else if("*".equals(type)){
			sum=num1.doubleValue()*num2.doubleValue();
		}
		else if("/".equals(type)){
			if(num2.longValue()==0){
				sum=0;
			}
			else{
				sum=num1.doubleValue()/num2.doubleValue();
			}
		}
		
		return sum;
	}
	
	public static Object[] cal( Object[] array, Object v){
		Object obj=null;
	
		if(array!=null && array.length>0){
			obj=array[0];
			if(obj instanceof Integer){
				return cal((Integer[])array, Integer.parseInt(""+v));
			}
			else if(obj instanceof Long){
				return cal((Long[])array, Long.parseLong(""+v));
			}
			else if(obj instanceof Float){
				return cal((Float[])array, Float.parseFloat(""+v));
			}
			else if(obj instanceof Double){
				return cal((Double[])array, Double.parseDouble(""+v));
			}
			else if(obj instanceof String){
				return cal((String[])array, (String)v);
			}
			else{
				logger.warn("--cal-array-unsupport type="+obj.getClass().getName());
			}
		}
		
		return null;
		
	}
	
	public static List cal(List list, Object v){
		Object obj=null;
		
		if(list!=null && list.size()>0){
			obj=list.get(0);
			if(obj instanceof Integer){
				return CommCollectionCal.cal((List<Integer>)list, Integer.parseInt(""+v));
			}
			else if(obj instanceof Long){
				return cal((List<Long>)list, Long.parseLong(""+v));
			}
			else if(obj instanceof Float){
				return cal((List<Float>)list, Float.parseFloat(""+v));
			}
			else if(obj instanceof Double){
				return cal((List<Double>)list, Double.parseDouble(""+v));
			}
			else if(obj instanceof String){
				return cal((List<String>)list, (String)v);
			}
			else{
				logger.warn("--cal-list-unsupport type="+obj);
			}
		}
		return null;
	}
}
