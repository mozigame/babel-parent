package com.babel.common.core.util;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.babel.common.core.entity.IPoolInfoVO;
import com.babel.common.core.entity.PoolInfoVO;
import com.babel.common.core.service.IThreadPoolInfoService;



/**
 * 线程池管理
 * @author jinhe.chen
 *
 */
public class TaskExecutorUtils {
	private static final Logger logger = Logger.getLogger(TaskExecutorUtils.class);
	public static String PACKAGE_NAME="com.babel";
	private static Map<String, TaskExecutor> taskExecutorMap=new HashMap<>();
	private static Map<String, IPoolInfoVO> poolInfoMap=new HashMap<>();
	private static Map<String, Date> lastDateMap=new ConcurrentHashMap<String, Date>();
	private static String threadInfoServiceName="basedataThreadPoolInfoService";
//	private static List<PoolInfoVO> savePoolInfoList=new ArrayList<>();
	
	public static String getKeyName(Class clazz, final String key){
		String keyName=clazz.getName();
		if(keyName.startsWith(PACKAGE_NAME)){
			keyName=keyName.substring(PACKAGE_NAME.length()+1);
		}
		if(key!=null){
			keyName+="-"+key;
		}
		return keyName;
	}
	public static TaskExecutor getTaskExecutorInstance(String sysType, Class clazz,  String key, Properties properties){
		final String keyName=getKeyName(clazz, key);
		String threadNamePrefix="taskExecutor-"+clazz.getSimpleName();
		if(key!=null){
			threadNamePrefix+="-"+key;
		}
		
		TaskExecutor taskExecutor=taskExecutorMap.get(keyName);
		lastDateMap.put(keyName, new Date());
		if(taskExecutor!=null){
//			loadThreadInfoAsync(sysType);//异步更新配置信息
			return taskExecutor;
		}
		synchronized (keyName) {
			taskExecutor=taskExecutorMap.get(keyName);
			if(taskExecutor==null){
//				logger.info("-----poolInfoMap="+poolInfoMap+" keyName="+keyName);
				if(poolInfoMap.isEmpty()){
					loadThreadInfoAll();//载入线程池数据
				}
				IPoolInfoVO poolInfoVO=poolInfoMap.get(keyName);
				logger.info("-----poolInfoMap="+poolInfoMap+" keyName="+keyName+" poolInfoVO="+poolInfoVO);
				if(poolInfoVO==null){
					PoolInfoVO poolInfo=new PoolInfoVO();
					if(properties==null){
						properties=new Properties();
					}
					poolInfo.load(properties);
					poolInfo.setCode(keyName);
					poolInfo.setName(threadNamePrefix);
					poolInfoVO=poolInfo;
					poolInfoMap.put(poolInfo.getCode(), poolInfo);
//					savePoolInfoList(sysType, taskExecutor, poolInfo);
				}
				
				logger.info("-----init--getTaskExecutorInstance--"+keyName+" init:"+threadNamePrefix+" taskExecutor="+taskExecutor);
				ThreadPoolTaskExecutor threadPoolTask=new ThreadPoolTaskExecutor();
				threadPoolTask.setCorePoolSize(poolInfoVO.getCorePoolSize());
				threadPoolTask.setMaxPoolSize(poolInfoVO.getMaxPoolSize());
				threadPoolTask.setQueueCapacity(poolInfoVO.getQueueCapacity());
				threadPoolTask.setKeepAliveSeconds(poolInfoVO.getKeepAliveSeconds());
				threadPoolTask.setBeanName(clazz.getSimpleName());
				threadPoolTask.setThreadNamePrefix(threadNamePrefix);
				threadPoolTask.initialize();
				taskExecutor=threadPoolTask;
				taskExecutorMap.put(keyName, threadPoolTask);
			}
			else{
				logger.info("-----init--getTaskExecutorInstance--taskExecutor="+taskExecutor);
			}
		}
		return taskExecutor;
	}
	
	private static Properties propertiesSingle=null;
	private static Map<String, Object> executorUseMap=new TreeMap<>();
	public static TaskExecutor getExecutorSingle(final String sysType, final Class classFrom,final String methodCode, final Integer limit){
		Class clazz=TaskExecutorUtils.class;
		 String key="single";
		 Integer timeLimit=5;
		if(propertiesSingle==null){
			 Properties properties=TaskExecutorUtils.getPoolInfo(1, 10, 10);
			 properties.put("limitTime", timeLimit);
			 propertiesSingle=properties;
		}
		String keyName=getKeyName(clazz, key);
		putUseMap(keyName, classFrom.getSimpleName()+"."+methodCode, limit);
		TaskExecutor taskExecutorRun=TaskExecutorUtils.getTaskExecutorInstance(sysType, clazz, key, propertiesSingle);
		return taskExecutorRun;
	}
	
	private static void putUseMap(final String keyName,final String className,final Integer timeLimit){
		Map<String, Object> classNameMap=(Map<String, Object>)executorUseMap.get(keyName);
		if(classNameMap==null){
			classNameMap=new TreeMap<>();
		}
		Integer count=(Integer)classNameMap.get(className+".count");
		if(count==null){
			count=0;
		}
		count++;
		Date now = new Date();
		if(!classNameMap.containsKey(className+".first")){
			classNameMap.put(className+".first", now);
		}
		classNameMap.put(className+".count", count);
		classNameMap.put(className+".limit", timeLimit);
		classNameMap.put(className+".date", now);
		executorUseMap.put(keyName, classNameMap);
	}
	
	private static void savePoolInfoList(final String sysType, final TaskExecutor taskExecutor, PoolInfoVO poolInfoVO){

//		TaskExecutor taskExecutorRun=getExecutorSingle(sysType, TaskExecutorUtils.class, "savePoolInfoList", 0);
		final int timeLimit=0;//5s
		final Class clazz=TaskExecutorUtils.class;
		final String key="save";
		final Properties properties=TaskExecutorUtils.getPoolInfo(1, 10, 10);
		properties.put("limitTime", timeLimit);

		TaskExecutor taskExecutorRun=TaskExecutorUtils.getTaskExecutorInstance(sysType, clazz, key, properties);
//		String keyName=poolInfoVO.getCode();
//		IPoolInfoVO poolInfoVO2=poolInfoMap.get(keyName);
//		taskExecutorRun.execute(new Runnable() {
//			public void run() {
//				try {
//					Thread.sleep(poolInfoVO2.getLimitTime()*1000);
//				} catch (InterruptedException e) {
//					logger.error("----sleep error:"+e.getMessage());
//				}
//				IThreadPoolInfoService poolInfoService=geThreadPoolInfoService();
//				if(poolInfoService!=null){
//					poolInfoService.savePoolInfo(sysType, taskExecutor, poolInfoVO);
//				}
//			}
//		});
	}
	
	private static Date asyncLoadDate=null;
	/**
	 * 异步从表中的数据更新配置信息
	 * 以下代码不用加锁，已做了时间控制
	 * @param sysType
	 */
//	private static void loadThreadInfoAsync(String sysType){
//		final int timeLimit=5;//5s
//		if(RedisUtil.isRunLimit(TaskExecutorUtils.class, timeLimit, "loadThreadInfoAsync", false)){
//			return ;
//		}
//		Date now = new Date();
//		if(asyncLoadDate==null){
//			asyncLoadDate=now;
//		}
//		TaskExecutor taskExecutorRun=getExecutorSingle(sysType, TaskExecutorUtils.class, "loadThreadInfoAsync", timeLimit);
//		taskExecutorRun.execute(new Runnable() {
//			public void run() {
//				IThreadPoolInfoService poolInfoService=geThreadPoolInfoService();
//				if(poolInfoService==null){
//					return;
//				}
//				
//				List<IPoolInfoVO> list=poolInfoService.findThreadPoolList(sysType, asyncLoadDate);
//				if(CollectionUtils.isEmpty(list)){
//					return;
//				}
//				asyncLoadDate=now;
//				logger.info("-----loadThreadInfoAsync--sysType="+sysType+" asyncLoadDate="+asyncLoadDate+" list="+list.size());
//				for(IPoolInfoVO poolInfoVO:list){
//					TaskExecutor taskObj=taskExecutorMap.get(poolInfoVO.getCode());
//					if(taskObj!=null){
//						if(taskObj instanceof ThreadPoolTaskExecutor){
//							ThreadPoolTaskExecutor taskPool=(ThreadPoolTaskExecutor)taskObj;
//							taskPool.setCorePoolSize(poolInfoVO.getCorePoolSize());
//							taskPool.setMaxPoolSize(poolInfoVO.getMaxPoolSize());
//							taskPool.setKeepAliveSeconds(poolInfoVO.getKeepAliveSeconds());
//							taskPool.setQueueCapacity(poolInfoVO.getQueueCapacity());
//							taskPool.setThreadNamePrefix(poolInfoVO.getName());
//						}
//					}
//				}
//			}
//		});
//	}
	
	public static void loadThreadInfoAll(){
		logger.info("-----loadThreadInfoAll--");
//		IThreadPoolInfoService poolInfoService=geThreadPoolInfoService();
//		if(poolInfoService==null){
//			logger.warn("-----loadThreadInfoAll--poolInfoService is null");
//			return;
//		}
//		
//		List<IPoolInfoVO> list=poolInfoService.findThreadPoolList(null, null);
//		if(CollectionUtils.isEmpty(list)){
//			return;
//		}
//		logger.info("-----loadThreadInfoAll--list="+list.size());
//		for(IPoolInfoVO pInfo:list){
//			poolInfoMap.put(pInfo.getCode(), pInfo);
//		}
	}
	
//	private static IThreadPoolInfoService threadPoolInfoService=null;
//	private static IThreadPoolInfoService geThreadPoolInfoService() {
//		if(threadPoolInfoService==null && SpringContextUtil.containsBean(threadInfoServiceName)){
//			threadPoolInfoService=(IThreadPoolInfoService)SpringContextUtil.getBean(threadInfoServiceName);
//		}
//		return threadPoolInfoService;
//	}
	
	public static Properties getPoolInfo(int corePoolSize, int  maxPoolSize, int queueCapacity){
		Properties properties=new Properties();
		properties.put("corePoolSize", corePoolSize);
		properties.put("maxPoolSize", maxPoolSize);
		properties.put("queueCapacity", queueCapacity);
		return properties;
	}
	
	public static Map<String, TaskExecutor> getTaskExecutors(){
		return taskExecutorMap;
	}
	
	 public static Map<String, Map<String, Object>> getThreadPoolMap(){
	    	Map<String, Map<String, Object>> poolInfoMap=new HashMap<>();
	    	
	    	
	    	//taskExecutorUtils
	    	Map<String, TaskExecutor> taskExecutorMap=TaskExecutorUtils.getTaskExecutors();
	    	Set<String> keys = taskExecutorMap.keySet();
			TaskExecutor taskExecutor = null;
			for (String key : keys) {
				taskExecutor = taskExecutorMap.get(key);
				Map<String, Object> map = ServerUtil.getThreadPoolMap(key, taskExecutor);
				if(!map.isEmpty()){
					map.put("lastLogDate", lastDateMap.get(key));
					poolInfoMap.put(key, map);
				}
			}
			poolInfoMap.put("useMap",executorUseMap);
	    	return poolInfoMap;
	    }

//	/**
//	 * @param threadInfoServiceName the threadInfoServiceName to set
//	 */
//	public void setThreadInfoServiceName(String threadInfoServiceName) {
//		TaskExecutorUtils.threadInfoServiceName = threadInfoServiceName;
//	}
	

}
