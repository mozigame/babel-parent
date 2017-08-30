package com.babel.common.core.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 动态累加器
 * @author 金和
 *
 */
public class CommAutoSum {
	private Map<Object, Double> map=new HashMap<Object, Double>();
	public void sum(Object key, Double value){
		Double v = map.get(key);
		if(v==null){
			v=0d;
		}
		map.put(key, v+value);
	}
	
	public void count(Object key){
		this.sum(key, 1d);
	}
	
	public Double getValue(Object key){
		return map.get(key);
	}
}
