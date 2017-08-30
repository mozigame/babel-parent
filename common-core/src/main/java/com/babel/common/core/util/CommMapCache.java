package com.babel.common.core.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;

/**
 * 通用缓存处理
 * @author 金和
 *
 */
public class CommMapCache {
	private static final Logger log = Logger.getLogger(CommMapCache.class);
	public CommMapCache(){
		
	}
	public CommMapCache(int cleanIntervel, int effectTime, int maxSize){
		System.out.println("--MapCache-");
		this.cleanIntervel=cleanIntervel;
		this.effectTime=effectTime;
		this.maxSize=maxSize;
	}
	private int cleanIntervel=600;//10分钟
	private int effectTime=3600;//1小时
	private int maxSize=1000;
	private Date cleanDate=null;
	private Map<String, Object> cacheMap=new HashMap<String, Object>();
	private Map<String, Date> cacheDateMap=new HashMap<String, Date>();
	public synchronized boolean isExistCache(String key, Object obj){
		boolean isExist=cacheDateMap.containsKey(key);
		if(!isExist){
			cacheDateMap.put(key, new Date());
			if(obj!=null)
				cacheMap.put(key, obj);
		}
		return isExist;
	}
	
	public synchronized void cache(String key, Object obj){
		this.cacheMap.put(key, obj);
		this.cacheDateMap.put(key, new Date());
	}
	
	public synchronized void cleanCache(String keyStart){
		String cleanStr="";
		int cacheSize=cacheMap.size();
		Iterator<Map.Entry<String, Date>> itor=cacheDateMap.entrySet().iterator();
		Date date = DateUtils.addSeconds(new Date(), -effectTime);
		Date curDate=null;
		Map.Entry<String, Date> entry=null;
		while(itor.hasNext()){
			entry=itor.next();
			if(entry.getKey().startsWith(keyStart)){
				curDate=entry.getValue();
				if(curDate.before(date)){
					cleanStr+=entry.getKey()+",";
					cacheMap.remove(entry.getKey());
					itor.remove();
				}
			}
		}
		if(cleanStr.length()>0){
			log.info("----cacheSize="+cacheSize+"/"+cacheMap.size()+" Clean remove:"+cleanStr);
		}
	}
	
	public Object getData(String key){
		return cacheMap.get(key);
	}
	
	public synchronized void cleanOldData(){
		//1小时检查一次，看是否需要清除缓存
		if(cleanDate==null||cleanDate.before(DateUtils.addSeconds(new Date(), -cleanIntervel))){
			int cacheSize=cacheMap.size();
			if(cacheSize<this.maxSize){
				return;
			}
			
			String cleanStr="";
			Iterator<Map.Entry<String, Date>> itor=cacheDateMap.entrySet().iterator();
			Date date = DateUtils.addSeconds(new Date(), -effectTime);
			Date curDate=null;
			Map.Entry<String, Date> entry=null;
			while(itor.hasNext()){
				entry=itor.next();
				curDate=entry.getValue();
				if(curDate.before(date)){
					cleanStr+=entry.getKey()+",";
					cacheMap.remove(entry.getKey());
					itor.remove();
				}
			}
			if(cleanStr.length()>0){
				log.info("----cacheSize="+cacheSize+"/"+cacheMap.size()+" Expired remove:"+cleanStr);
			}
		}
	}
}
