package com.babel.common.core.util;


import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.babel.common.core.redis.RedisManager;
import com.babel.common.core.tools.ShortUrlGenerator;

import redis.clients.jedis.JedisPool;

public class RedisUtil {
	private static final Logger logger = Logger.getLogger(RedisUtil.class);
	private static RedisTemplate redisTemplate=null;
	private static Date redisErrorDate=null;
	private static void initRedis(){
		if(SpringContextUtil.containsBean("redisTemplate")){
			redisTemplate=(RedisTemplate)SpringContextUtil.getBean("redisTemplate");
		}
	}
	
	public static RedisTemplate getRedisTemplate(){
		initRedis();
		return redisTemplate;
	}
	
	public static boolean isHasRedisTemplate(){
		return SpringContextUtil.containsBean("redisTemplate");
	}
	
	/**
	 * 设备失效期
	 * @param redisKey
	 * @param timeout
	 */
	public static void expire(String redisKey, int timeout){
		if(timeout==0){
			return;
		}
		RedisTemplate redisTemplate=getRedisTemplate();
		if(redisTemplate!=null){
			TimeUnit unit=TimeUnit.SECONDS;
			redisTemplate.expire(redisKey, timeout, unit);
		}
	}
	
	public static void putRedis(String redisKey, String key, Object obj){
		if(key==null){
			logger.warn("----putRedis--redisKey="+redisKey+" key="+key+" is empty");
			return;
		}
		RedisTemplate redisTemplate=getRedisTemplate();
		if(redisTemplate!=null){
			try {
				if(redisErrorDate==null||new Date().getTime()-redisErrorDate.getTime()>10000){//如果redis发生错误，10秒内不处理
					redisTemplate.opsForHash().put(redisKey, key, obj);
				}
			} catch (Exception e) {
				redisErrorDate=new Date();
				logger.warn("-----putRedis--redisKey="+redisKey+" key="+key+" error:"+e.getMessage(), e);
			}
		}
	}
	
	public static Object getRedis(String redisKey, Object key){
		if(key==null){
			logger.warn("----getRedis--redisKey="+redisKey+" key="+key+" is empty");
			return null;
		}
		
		Object user=null;
		RedisTemplate redisTemplate = RedisUtil.getRedisTemplate();
		if (redisTemplate != null) {
			try {
				if (redisErrorDate == null || new Date().getTime() - redisErrorDate.getTime() > 10000) {// 如果redis发生错误，10秒内不处理
					user = redisTemplate.opsForHash().get(redisKey, key);
				}
			} catch (Exception e) {
				redisErrorDate = new Date();
				logger.warn("-----getRedis--redisKey=" + redisKey + " key=" + key + " error:" + e.getMessage(), e);
			}

		}
		return user;
	}
	
	public static Set getRedisKeys(String redisKey){

		Set user=null;
		RedisTemplate redisTemplate = RedisUtil.getRedisTemplate();
		if (redisTemplate != null) {
			try {
				if (redisErrorDate == null || new Date().getTime() - redisErrorDate.getTime() > 10000) {// 如果redis发生错误，10秒内不处理
					user = redisTemplate.opsForHash().keys(redisKey);
				}
			} catch (Exception e) {
				redisErrorDate = new Date();
				logger.warn("-----getRedisKeys--redisKey=" + redisKey + " error:" + e.getMessage(), e);
			}

		}
		return user;
	}
	
	public static Map getRedisAlls(String redisKey){

		Map map=null;
		RedisTemplate redisTemplate = RedisUtil.getRedisTemplate();
		if (redisTemplate != null) {
			try {
				if (redisErrorDate == null || new Date().getTime() - redisErrorDate.getTime() > 10000) {// 如果redis发生错误，10秒内不处理
					map = redisTemplate.opsForHash().entries(redisKey);
				}
			} catch (Exception e) {
				redisErrorDate = new Date();
				logger.warn("-----getRedisKeys--redisKey=" + redisKey + " error:" + e.getMessage(), e);
			}

		}
		return map;
	}
	
	/**
	 * 优先用本地缓存
	 * redis没有或失效不影响功能的使用
	 * @param cacheMap 本地静态map缓存
	 * @param redisKey
	 * @param key
	 * @param obj
	 */
	public static void putRedis(Map cacheMap, String redisKey, Object key, Object obj){
		if(key==null){
			logger.warn("----putRedis--redisKey="+redisKey+" key="+key+" is empty");
			return;
		}
		cacheMap.put(key, obj);
		putRedis(redisKey, key.toString(), obj);
	}
	
//	private static long refresh_time=0;;
//	private static void refresh(Map cacheMap){
//		Date curDate=new Date();
//		if(refresh_time==0){
//			refresh_time=curDate.getTime();
//		}
//		else if((curDate.getTime()-refresh_time)>600000){//10分钟刷一次
//			refresh_time=curDate.getTime();
//			cacheMap.clear();
//		}
//	}
	
	/**
	 * 优先用本地缓存，没有则自动从redis更新数据到本地缓存
	 * redis没有或失效不影响功能的使用
	 * @param cacheMap 本地静态map缓存
	 * @param redisKey
	 * @param key
	 * @return
	 */
	public static Object getRedis(Map cacheMap, String redisKey, Object key){
		if(key==null){
			logger.warn("----getRedis--redisKey="+redisKey+" key="+key+" is empty");
			return null;
		}
		
		Object user= cacheMap.get(key);
		if(user==null){
			user=getRedis(redisKey, key.toString());
			if(user!=null){
				cacheMap.put(key, user);
			}
		}
		return user;
	}
	
	/**
	 * 短链接生成
	 * @param url
	 * @return
	 */
	public static String genShortUrlKey(int shortType, String url){
		String key=ShortUrlGenerator.getShortUrl(url);
		putRedis(null, "K_SHORT_LINK_"+shortType, key, url);
		return key;
	}
	
	/**
	 * 短链接对应的url获取
	 * @param key
	 * @return
	 */
	public static String getShortUrl(int shortType, String key){
		return (String)getRedis(null, "K_SHORT_LINK_"+shortType, key);
	}
	
	private static Map<String, Date> redisKeyDateMap= new ConcurrentHashMap<>();
	
	/**
	 * 检查间隔时间是否>1s
	 * @param redisKey
	 * @return
	 */
	public static boolean isRunLimitSecond(String redisKey, int secondLimit){
		synchronized (redisKey) {
			Date now = new Date();
			Date redisKeyDate=redisKeyDateMap.get(redisKey);
			if(redisKeyDate==null){
				redisKeyDateMap.put(redisKey, now);
				return false;
			}
			if(now.getTime()-redisKeyDate.getTime()<secondLimit*1000){
				redisKeyDateMap.put(redisKey, now);
				return true;
			}
		}
		return false;
		
	}
	
	/**
	 * redis执行限制，也用于集群或多机情况下的并发控制
	 * 是否时间低于限制的时间
	 * @param clazz
	 * @param timeLimit
	 * @param redisKey
	 * @return
	 */
	public static boolean isRunLimit(Class clazz, int timeLimit, String redisKey){
		String runType=ConfigUtils.getConfigValue("sys.runType", "dev");
		boolean showLog="true".equals(runType);
		return isRunLimit(clazz, timeLimit, redisKey, showLog);
	}
	
	/**
	 * redis执行限制
	 * 是否时间低于限制的时间
	 * @param clazz
	 * @param timeLimit
	 * @param redisKey
	 * @return
	 */
	public static boolean isRunLimit(Class clazz, int timeLimit, String redisKey, boolean showLog){
		if(isRunLimitSecond(redisKey, 1)){
			return true;
		}
		RedisTemplate redisTemplate = RedisUtil.getRedisTemplate();
		synchronized (redisKey) {
			Date date=(Date)redisTemplate.boundHashOps("_redisBatch").get(redisKey);
			Date now = new Date();
			if(date!=null){
				long seconds=(now.getTime()-date.getTime())/1000;
				
				if(seconds<timeLimit){//5秒内不处理
//					System.out.println("----seconds="+seconds+" timeLimit="+timeLimit);
					if(!showLog){
						return true;
					}
					BoundListOperations operList=redisTemplate.boundListOps(redisKey);
					long logSize=operList.size();
					if(logSize>0){
						Object host=redisTemplate.boundHashOps("_redisBatchHost").get(redisKey);
						logger.info("-----isRunLimit--host="+host+" class="+clazz+" redisKey="+redisKey+" timeLimit:"+seconds+"/"+timeLimit+" size="+logSize);
					}
					
					return true;
				}
			}
			redisTemplate.boundHashOps("_redisBatch").put(redisKey, now);
			redisTemplate.boundHashOps("_redisBatchHost").put(redisKey, ServerUtil.getLocalIP());
		}
		return false;
	}
	

	private static RedisLockUtil redisLockUtil;
	private static void initRedisLock(){
		if(redisLockUtil==null){
			if(RedisManager.getJedisPool()==null){
				logger.info("----initRedisLock--init jedisPool");
				RedisManager redisManager=null;
				if(SpringContextUtil.containsBean("redisManager")){
					redisManager=(RedisManager)SpringContextUtil.getBean("redisManager");
				}
				else{
					redisManager=new RedisManager();
				}
				if(StringUtils.isEmpty(redisManager.getHost())){
					redisManager.setHost(ConfigUtils.getConfigValue("redis.host"));
					redisManager.setPort(ConfigUtils.getConfigValueInt("redis.port"));
					redisManager.setDatabase(ConfigUtils.getConfigValueInt("redis.database"));
					redisManager.setMaxWait(ConfigUtils.getConfigValueInt("redis.maxWait"));
				}
				redisManager.init();
			}
			if(RedisManager.getJedisPool()!=null){
				redisLockUtil=new RedisLockUtil(RedisManager.getJedisPool());
			}
			if(redisLockUtil==null && SpringContextUtil.containsBean("jedisPool")){
				JedisPool jedisPool=(JedisPool)SpringContextUtil.getBean("jedisPool");
				redisLockUtil=new RedisLockUtil(jedisPool);
			}
			else{
				logger.warn("----initRedisLock--beanName:jedisPool not found");
			}
		}
	}
	public static boolean tryLock(String redisKey){
		initRedisLock();
		if(redisLockUtil==null){
			return false;
		}
		return redisLockUtil.tryLock("_lock."+redisKey);
	}
	
	public static void unLock(String redisKey){
		initRedisLock();
		if(redisLockUtil==null){
			return;
		}
		redisLockUtil.unLock("_lock."+redisKey);
	}
	
	private static Map<String, Long> clearCacheTimeMap = new ConcurrentHashMap<>();
	private static Map<String, Integer> cacheCounterMap = new ConcurrentHashMap<>();

	/**
	 * 用于控制自动清理本地缓存，支持按间隔时间或按调用次数
	 * 
	 * @param key
	 * @param cacheMap
	 * @param intvTime
	 * @param maxCount
	 */
	public static void cleanCacheMap(String key, Map cacheMap, Integer intvTime, Integer maxCount) {
		if (maxCount > 0) {
			Integer count = cacheCounterMap.get(key);
			if (count == null) {
				count = 0;
			}
			count++;
			if (count > maxCount) {
				count = 0;
				cacheCounterMap.put(key, count);
				cacheMap.clear();
				Long time = clearCacheTimeMap.get(key);
				Long curTime = System.currentTimeMillis();
				logger.info("----clear cache:"+key+" by maxCount:" + maxCount + " cacheSize=" + cacheMap.size() + " time="+ (curTime - time));

			}
		}

		if (intvTime > 0) {
			Long time = clearCacheTimeMap.get(key);
			Long curTime = System.currentTimeMillis();
			if (time == null) {
				clearCacheTimeMap.put(key, curTime);
			} else if (curTime - time > intvTime * 1000) {
				clearCacheTimeMap.put(key, curTime);
				cacheMap.clear();
				Integer count = cacheCounterMap.get(key);
				logger.info("----clear cache:"+key+" by intvTime:" + intvTime + " cacheSize=" + cacheMap.size() + " count="	+ count);
			}
		}

	}

}
