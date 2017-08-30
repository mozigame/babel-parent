package com.babel.common.core.util;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.bootstrap.BootstrapCacheLoader;
import net.sf.ehcache.event.RegisteredEventListeners;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;



/**
 * 因未配置保存到硬盘，重启后数据会丢失
 * @author 金和
 *
 */
public class EhcacheUtil {
	private static final Log logger = LogFactory.getLog(EhcacheUtil.class);
	/**
	 * 用于重试规则的缓存，失效期为element级，即由element自已管。
	 * @param cacheName
	 * @param expireSeconds
	 * @return
	 */
	public static synchronized Cache  getCache(String cacheName){
		Cache cache=CacheManager.getInstance().getCache(cacheName);
		if(cache==null){
			logger.info("-----ehcache create:"+cacheName);
			 //method 1: create new one and add into cache manager.
			/**
			 * 内存缓存中最多可以存放的元素数量,若放入Cache中的元素超过这个数值,则有以下两种情况。
				1)若overflowToDisk=true,则会将Cache中多出的元素放入磁盘文件中。
				2)若overflowToDisk=false,则根据memoryStoreEvictionPolicy策略替换Cache中原有的元素。
			 */
			int maxElementsInMemory=10000;
			MemoryStoreEvictionPolicy memoryStoreEvictionPolicy=MemoryStoreEvictionPolicy.LFU;
			boolean overflowToDisk=false;//内存不足时,是否启用磁盘缓存
			String diskStorePath=null;
			boolean eternal=true;//缓存中对象是否永久有效,即是否永驻内存,true时将忽略timeToIdleSeconds和timeToLiveSeconds。
			long timeToLiveSeconds=120;//缓存自创建日期起至失效时的间隔时间x；
			long timeToIdleSeconds=120;//缓存创建以后，最后一次访问缓存的日期至失效之时的时间间隔y；
			boolean diskPersistent=false;
			long diskExpiryThreadIntervalSeconds=120;//磁盘缓存的清理线程运行间隔,默认是120秒。
			RegisteredEventListeners registeredEventListeners=null;
			BootstrapCacheLoader bootstrapCacheLoader=null;
			
	//		int maxElementsOnDisk;
	//		int diskSpoolBufferSizeMB;
	//		boolean clearOnFlush=false;
	//		boolean isTerracottaClustered;
	//		boolean terracottaCoherentReads;
			cache = new Cache(cacheName, maxElementsInMemory, memoryStoreEvictionPolicy, overflowToDisk, diskStorePath, eternal, 
	        		timeToLiveSeconds, timeToIdleSeconds, diskPersistent, diskExpiryThreadIntervalSeconds, registeredEventListeners, bootstrapCacheLoader);
	        CacheManager.getInstance().addCache(cache);
		}
        
        
//        System.out.println("------cacheName="+cacheName+" cache="+cache);
        
		return cache;
	}
	
	public static Element elementChange(Element e, Object newValue){
		Object key=e.getObjectKey();
		Object value=newValue;
		long version=e.getVersion();
		long creationTime=e.getCreationTime();
		long lastAccessTime=e.getLastAccessTime();
		long lastUpdateTime=System.currentTimeMillis();
		long hitCount=e.getHitCount();
		
		Element element=new Element(key, value, version, creationTime,
				lastAccessTime, lastUpdateTime, hitCount);
		return element;
	}
}
