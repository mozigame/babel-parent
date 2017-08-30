package com.babel.common.core.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.apache.shiro.session.Session;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.SerializationUtils;

import com.alibaba.druid.pool.DruidDataSource;
import com.babel.common.core.entity.OnlineSession;
import com.babel.common.core.entity.RunCountVO;
import com.babel.common.core.redis.RedisManager;
import com.zaxxer.hikari.HikariDataSource;

public class ServerUtil {
	private static final Logger logger = Logger.getLogger(ServerUtil.class);
	/**
	  * 获得主机IP
	  *
	  * @return String
	  */
	 public static boolean isWindowsOS(){
	    boolean isWindowsOS = false;
	    String osName = System.getProperty("os.name");
	    if(osName.toLowerCase().indexOf("windows")>-1){
	     isWindowsOS = true;
	    }
	    return isWindowsOS;
	  }

	 private static String localIp=null;
	  /**
	    * 获取本机ip地址，并自动区分Windows还是linux操作系统
	    * @return String
	    */
	  public static String getLocalIP(){
		  if(localIp!=null){
			  return localIp;
		  }
	    String sIP = "";
	    InetAddress ip = null; 
	    try {
	     //如果是Windows操作系统
	     if(isWindowsOS()){
	      ip = InetAddress.getLocalHost();
	     }
	     //如果是Linux操作系统
	     else{
	      boolean bFindIP = false;
	      Enumeration<NetworkInterface> netInterfaces = (Enumeration<NetworkInterface>) NetworkInterface
	        .getNetworkInterfaces();
	      while (netInterfaces.hasMoreElements()) {
	       if(bFindIP){
	        break;
	       }
	       NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
	       //----------特定情况，可以考虑用ni.getName判断
	       //遍历所有ip
	       Enumeration<InetAddress> ips = ni.getInetAddresses();
	       while (ips.hasMoreElements()) {
	        ip = (InetAddress) ips.nextElement();
	        if( ip.isSiteLocalAddress()  
	                   && !ip.isLoopbackAddress()   //127.开头的都是lookback地址
	                   && ip.getHostAddress().indexOf(":")==-1){
	            bFindIP = true;
	               break;  
	           }
	       }

	      }
	     }
	    }
	    catch (Exception e) {
	     e.printStackTrace();
	    }

	    if(null != ip){
	     sIP = ip.getHostAddress();
	    }
	    localIp=sIP;
	    return localIp;
	  }
	  
	  public static ThreadPoolTaskExecutor getThreadPoolTask(TaskExecutor taskExecutor){
		  if(taskExecutor instanceof ThreadPoolTaskExecutor){
			  ThreadPoolTaskExecutor threadPool=(ThreadPoolTaskExecutor)taskExecutor;
			  
			  return threadPool;
		  }
		  return null;
	  }
	  
	  private static Map<String, Date> lastLogDateMap=new ConcurrentHashMap<String, Date>();
	  public static Map<String, Map<String, Object>> threadPoolMap=new ConcurrentHashMap<>();
	  private static Map<String, TaskExecutor> taskExecutorMap=new ConcurrentHashMap<>();
	  
	  /**
	   * 线程池信息日志，为免日志过多，5分钟日志一次
	   * @param name 名称，一般是所在的类名+线程池变量名
	   * @param taskExecutor
	   * @param lastLogDate上次日志时间
	   */
	  public static void logPool(String name, TaskExecutor taskExecutor){
		  if(name==null ||taskExecutor==null){
			  return;
		  }
		  taskExecutorMap.put(name, taskExecutor);
//		  lastLogDateMap.put(name, new Date());
		  logRunCount(name);
	  }
	  
	public static Map<String, Object> getThreadPoolMap(String key, TaskExecutor taskExecutor) {
		Map<String, Object> map = new TreeMap<>();
		ThreadPoolTaskExecutor taskPool = getThreadPoolTask(taskExecutor);
		if (taskPool == null) {
			return map;
		}
		map.put("activeCount", taskPool.getActiveCount());
		map.put("poolSize", taskPool.getPoolSize());
		map.put("corePoolSize", taskPool.getCorePoolSize());
		map.put("maxPoolSize", taskPool.getMaxPoolSize());
		map.put("keepAliveSeconds", taskPool.getKeepAliveSeconds());
		map.put("threadPriority", taskPool.getThreadPriority());
		map.put("threadPoolExecutor.taskCount", taskPool.getThreadPoolExecutor().getTaskCount());
		map.put("threadPoolExecutor.largestPoolSize", taskPool.getThreadPoolExecutor().getLargestPoolSize());
		map.put("threadPoolExecutor.maximumPoolSize", taskPool.getThreadPoolExecutor().getMaximumPoolSize());
		map.put("threadPoolExecutor.queue.size", taskPool.getThreadPoolExecutor().getQueue().size());
		map.put("threadPoolExecutor.completedTaskCount", taskPool.getThreadPoolExecutor().getCompletedTaskCount());
		map.put("lastLogDate", lastLogDateMap.get(key));
		RunCountVO runCount = runCountMap.get(key);
		if (runCount != null) {
			map.put("lastLogDate", runCount.getNewDate());
			map.put("firstDate", runCount.getFirstDate());
			map.put("runCount", runCount.getCount());
		}
		map.put("lastLogDate", lastLogDateMap.get(key));
		return map;
	}
	  
	public static Map<String, Map<String, Object>> getThreadPoolInfo() {
		Map<String, Map<String, Object>> poolMap = new ConcurrentHashMap<>();
		Set<String> keys = taskExecutorMap.keySet();

		TaskExecutor taskExecutor = null;
		for (String key : keys) {
			taskExecutor = taskExecutorMap.get(key);
			Map<String, Object> map = getThreadPoolMap(key, taskExecutor);
			if (!map.isEmpty())
				poolMap.put(key, map);

		}

		return poolMap;

	}
	  
	  public static Map<String, Map<String, Object>> getDataSourceInfo(){
		  Map<String, Map<String, Object>> dataSourceMap=new ConcurrentHashMap<>();
		  
		  
		  Map<String, Object> dataSource=getDataSourceMap("dataSource");
		  dataSourceMap.put("dataSource", dataSource);
		  return dataSourceMap;
		  
	  }
	  
	  private static Map<String, Object> getDataSourceMap(String dataSourceCode){
		  Object object=SpringContextUtil.getBean(dataSourceCode);
		  Map<String, Object> poolMap=new TreeMap<>();
		  if(object instanceof DruidDataSource){
			  return getDataSourceDruidMap(dataSourceCode);
		  }
		  else if(object instanceof HikariDataSource){
			  return getDataSourceHikariMap(dataSourceCode);
		  }
		  
		  return poolMap;
	  }
	  
	  private static Map<String, Object> getDataSourceHikariMap(String dataSourceCode){
		  Map<String, Object> poolMap=new TreeMap<>();
		  Object object=SpringContextUtil.getBean(dataSourceCode);
		  if(!(object instanceof HikariDataSource)){
			  logger.warn("----invalid--type:"+dataSourceCode);
			  return poolMap;
		  }
		  HikariDataSource dataSource= (HikariDataSource)object;
		  poolMap.put("maxLifetime", dataSource.getMaxLifetime());
		  try {
			poolMap.put("loginTimeout", dataSource.getLoginTimeout());
		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		  poolMap.put("dataSourceClass", object.getClass().getName());
		  poolMap.put("connectionTimeout", dataSource.getConnectionTimeout());
		  poolMap.put("connectionTestQuery", dataSource.getConnectionTestQuery());
		  poolMap.put("connectionInitSql", dataSource.getConnectionInitSql());
		  poolMap.put("idleTimeout", dataSource.getIdleTimeout());
		  
		  poolMap.put("maximumPoolSize", dataSource.getMaximumPoolSize());
		  poolMap.put("minimumIdle", dataSource.getMinimumIdle());
		  poolMap.put("validationTimeout", dataSource.getValidationTimeout());
		  poolMap.put("driverClassName", dataSource.getDriverClassName());
		  poolMap.put("leakDetectionThreshold", dataSource.getLeakDetectionThreshold());
		  poolMap.put("poolName", dataSource.getPoolName());
		  poolMap.put("jdbcUrl", dataSource.getJdbcUrl());
		  poolMap.put("dataSourceProperties", dataSource.getDataSourceProperties());
		  
		  
		  return poolMap;
	  }
	  
	  private static Map<String, Object> getDataSourceDruidMap(String dataSourceCode){
		  Map<String, Object> poolMap=new TreeMap<>();
		  Object object=SpringContextUtil.getBean(dataSourceCode);
		  if(!(object instanceof DruidDataSource)){
			  logger.warn("----invalid--type:"+dataSourceCode);
			  return poolMap;
		  }
		  DruidDataSource dataSource= (DruidDataSource)object;
		  poolMap.put("dataSourceClass", object.getClass().getName());
		  poolMap.put("activeCount", dataSource.getActiveCount());
		  poolMap.put("activePeak", dataSource.getActivePeak());
		  poolMap.put("cachedPreparedStatementCount", dataSource.getCachedPreparedStatementCount());
		  poolMap.put("cachedPreparedStatementAccessCount", dataSource.getCachedPreparedStatementAccessCount());
		  poolMap.put("cachedPreparedStatementHitCount", dataSource.getCachedPreparedStatementHitCount());
		  poolMap.put("cachedPreparedStatementMissCount", dataSource.getCachedPreparedStatementMissCount());
		  poolMap.put("cachedPreparedStatementDeleteCount", dataSource.getCachedPreparedStatementDeleteCount());
		  poolMap.put("closeCount", dataSource.getCloseCount());
		  poolMap.put("transactionQueryTimeout", dataSource.getTransactionQueryTimeout());
		  poolMap.put("transactionThresholdMillis", dataSource.getTransactionThresholdMillis());
		  poolMap.put("loginTimeout", dataSource.getLoginTimeout());
		  poolMap.put("connectCount", dataSource.getConnectCount());
		  poolMap.put("commitCount", dataSource.getCommitCount());
		  poolMap.put("createCount", dataSource.getCreateCount());
		  poolMap.put("destroyCount", dataSource.getDestroyCount());
		  
		  poolMap.put("initialSize", dataSource.getInitialSize());
		  poolMap.put("maxActive", dataSource.getMaxActive());
		  poolMap.put("maxIdle", dataSource.getMaxIdle());
		  poolMap.put("maxWait", dataSource.getMaxWait());
		  poolMap.put("maxWaitThreadCount", dataSource.getMaxWaitThreadCount());
		  poolMap.put("maxOpenPreparedStatements", dataSource.getMaxOpenPreparedStatements());
		  poolMap.put("maxPoolPreparedStatementPerConnectionSize", dataSource.getMaxPoolPreparedStatementPerConnectionSize());
		  poolMap.put("minIdle", dataSource.getMinIdle());
		  poolMap.put("minEvictableIdleTimeMillis", dataSource.getMinEvictableIdleTimeMillis());
		  poolMap.put("discardCount", dataSource.getDiscardCount());
		  poolMap.put("errorCount", dataSource.getErrorCount());
		  poolMap.put("executeCount", dataSource.getExecuteCount());
		  poolMap.put("poolingCount", dataSource.getPoolingCount());
		  poolMap.put("waitThreadCount", dataSource.getWaitThreadCount());
		  poolMap.put("validationQueryTimeout", dataSource.getValidationQueryTimeout());
		  poolMap.put("validationQuery", dataSource.getValidationQuery());
		  poolMap.put("rollbackCount", dataSource.getRollbackCount());
		  poolMap.put("resetCount", dataSource.getResetCount());
		  poolMap.put("queryTimeout", dataSource.getQueryTimeout());
		  poolMap.put("poolingCount", dataSource.getPoolingCount());
		  poolMap.put("poolingPeak", dataSource.getPoolingPeak());
		  return poolMap;
	  }
	  
	
	  public static Map<String, Object> scheduleMap=new TreeMap<String, Object>();
	  
	  public static Map<String, Object> getRedisListSize(){
		  String list_key="logSqls,logServices,logAudit";
		  Map<String, Object> map=new TreeMap();
		  
		  Object object=SpringContextUtil.getBean("redisTemplate");
		  if(object instanceof RedisTemplate){
			  RedisTemplate redisTemplate=(RedisTemplate)object;
			 
			  String[] keys=list_key.split(",");
			  for(String key:keys){
				  map.put(key, redisTemplate.opsForList().size(key));
			  }
		  }
		  return map;
		  
	  }
	  
	private static String SESSION_KEY_PREFIX= "shiro_session:";
//	private static SimpleDateFormat sdf_datetime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static RedisManager redisManager=null;
	
	private static void initRedisManager(){
		if(redisManager==null && SpringContextUtil.containsBean("redisManager")){
			Object object=SpringContextUtil.getBean("redisManager");
			if(object instanceof RedisManager){
	    		redisManager=(RedisManager)object;
			}
		}
	}
	public static int getOnlineSessionCount() {
		if(redisManager==null){
			initRedisManager();
		}
		int sessionCount=0;
    	if(redisManager!=null){
	        Set<byte[]> keys = redisManager.keys(SESSION_KEY_PREFIX + "*");
	        sessionCount=keys.size();
    	}
    	return sessionCount;
	}
	
	private static RedisSerializer redisKeySerial=new JdkSerializationRedisSerializer();
	public static Session getSessionByRedis(final String sessionId){
		Session session=null;
		if(redisManager==null){
			initRedisManager();
		}
		if(redisManager!=null){
			byte[] serialKey=(SESSION_KEY_PREFIX+sessionId).getBytes();
			session= (Session)SerializationUtils.deserialize(redisManager.get(serialKey));
		}
		return session;
		
	}
	
	public static String getUserNameBySession(String sessionId) {
		String userName = sessionUserMap.get(sessionId);
		if(userName==null){
			Session session=(getSessionByRedis(sessionId));
			if(session!=null){
				userName=(String)session.getAttribute("sessionUserName");
				sessionUserMap.put(sessionId, userName);
			}
		}
		return userName;
	}
	
	/**
	 * 清除旧的没有操作的旧session数据
	 * 如5个小时内都没有操作的
	 * @return
	 */
	public static List<OnlineSession> cleanOldSessions(){
		List<OnlineSession> list = new ArrayList<OnlineSession>();
		if(redisManager==null){
			initRedisManager();
		}
    	if(redisManager!=null){
    		Date now = new Date();
    		Date timeBefore_5=org.apache.commons.lang3.time.DateUtils.addHours(now, -5);
	        Set<byte[]> keys = redisManager.keys(SESSION_KEY_PREFIX + "*");
	        if(keys != null && keys.size()>0){
	            for(byte[] key:keys){
	                Session s = (Session)SerializationUtils.deserialize(redisManager.get(key));
	                String loginid=(String)s.getAttribute("sessionUserName");
                	String ip=(String)s.getAttribute("sessionUserIp");
                	String loginTime=DateUtils.format(s.getLastAccessTime(),DateUtils.SDF_FORMAT_DATETIME);
                	if(loginid==null){
                		loginid="";
                	}
                	if(s.getLastAccessTime().before(timeBefore_5)){
	                	OnlineSession online = new OnlineSession(ip, loginid, loginTime);
	                	online.setStartTimestamp(DateUtils.format(s.getStartTimestamp(), DateUtils.SDF_FORMAT_DATETIME));
	                	redisManager.del(key);
	                	list.add(online);
                	}
	            }
	        }
	        if(list.size()>0)
	        	logger.info("-----cleanOldSessions--size="+list.size());
    	}
    	return list;
	}
	public static List<OnlineSession> getOnlineSessions() {
		List<OnlineSession> list = new ArrayList<OnlineSession>();
		if(redisManager==null){
			initRedisManager();
		}
    	if(redisManager!=null){
	        Set<byte[]> keys = redisManager.keys(SESSION_KEY_PREFIX + "*");
	        if(keys != null && keys.size()>0){
	            for(byte[] key:keys){
	                Session s = (Session)SerializationUtils.deserialize(redisManager.get(key));
	                String loginid=(String)s.getAttribute("sessionUserName");
                	String ip=(String)s.getAttribute("sessionUserIp");
                	String loginTime=DateUtils.format(s.getLastAccessTime(), DateUtils.SDF_FORMAT_DATETIME);
                	if(loginid==null){
                		loginid="";
                	}
                	OnlineSession online = new OnlineSession(ip, loginid, loginTime);
                	online.setStartTimestamp(DateUtils.format(s.getStartTimestamp(), DateUtils.SDF_FORMAT_DATETIME));
                	list.add(online);
	            }
	        }
    	}
    	/**
    	 * 排序处理，按账号+时间
    	 */
    	Collections.sort(list, new Comparator() {  
            public int compare(Object a, Object b) { 
            	if (a == null || b == null) return 0;
              String loginA= ((OnlineSession)a).getLoginId();
              String loginB = ((OnlineSession)b).getLoginId();
              int v=loginA.compareTo(loginB);
              if(v!=0){
            	  return v;
              }
              String timeA= ((OnlineSession)a).getLastAccessTime();  
              String timeB = ((OnlineSession)b).getLastAccessTime(); 
              return timeA.compareTo(timeB);
            }  
         });     
//    	System.out.println("----sessions="+sessions);
        return list;
    }
	
	public static Map<String, String> filterChainMap=null;
	
	
	public static Map<String, String> sessionUserMap=new HashMap<String, String>();
	/**
	 * 用于调用次数，执行次数统计
	 * 可用于统计调用频率比较高的接口
	 */
	public static Map<String, RunCountVO> runCountMap=new ConcurrentHashMap<>();
	public static RunCountVO logRunCount(String key){
		RunCountVO run=runCountMap.get(key);
		if(run==null){
			run=new RunCountVO();
		}
		run.increase();
		runCountMap.put(key, run);
		return run;
	}
}
