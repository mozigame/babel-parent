package com.babel.common.core.exception;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.core.RedisTemplate;

import com.babel.common.core.data.RetData;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.entity.LogExpVO;
import com.babel.common.core.util.ConfigUtils;
import com.babel.common.core.util.RedisListUtil;
import com.babel.common.core.util.RedisUtil;
import com.babel.common.core.util.TaskExecutorUtils;

/**
 * 异常通知管理员
 * @author jinhe.chen
 *
 */
public class NoticeException extends Exception{
	private static final Logger logger = Logger.getLogger(NoticeException.class);
	private static BlockingQueue<LogExpVO> logExpQueue=new LinkedBlockingQueue<>();
	private static Properties properties=null;
	private static Date asyncDate;
	public static final String REDIS_KEY_RET="_logExp_notice_save_queue";

	private LogExpVO logExp=null; 
	public NoticeException(Object obj, Exception e){
		logExp=new LogExpVO(obj, e);
		logExpQueue.add(logExp);
		saveNoticeToRedisAsync();
	}
	
	public NoticeException(Object obj, RetResult ret){
		logExp=new LogExpVO(obj, ret, null);
		logExpQueue.add(logExp);
		saveNoticeToRedisAsync();
	}
	
	public NoticeException(Object obj, RetResult ret, Throwable cause){
		logExp=new LogExpVO(obj, ret, cause);
		logExpQueue.add(logExp);
		saveNoticeToRedisAsync();
	}
	
	public NoticeException(Object obj, RetData ret, Throwable cause){
		logExp=new LogExpVO(obj, ret, cause);
		logExpQueue.add(logExp);
		saveNoticeToRedisAsync();
	}
	
	public void saveNoticeToRedisAsync(){
		int timeLimit=5;
		Date now = new Date();
		if(asyncDate!=null && asyncDate.after(DateUtils.addSeconds(now, -timeLimit))){//5秒内不处理
			return;
		}
		asyncDate=now;
		
		final String className=this.getClass().getSimpleName();
		final Class clazz=TaskExecutorUtils.class;
		final String key=null;
		if(properties==null){
			properties=TaskExecutorUtils.getPoolInfo(1, 10, 10);
			properties.put("limitTime", timeLimit);
		}
		
		String sysType=ConfigUtils.getConfigValue("sys.appType");
		final int limit_count=500;
		RedisTemplate redisTemplate=RedisUtil.getRedisTemplate();
		if(redisTemplate==null){
			logExpQueue.clear();
			return;
		}
		
		TaskExecutor taskExecutor=TaskExecutorUtils.getTaskExecutorInstance(sysType, clazz, key, properties);
		taskExecutor.execute(new Runnable() {
			public void run() {
				saveLogExpQueue();
			}
			
			public void saveLogExpQueue(){
				int size=logExpQueue.size();
				if(size>0){
					logger.info("---saveLogExpQueue--size="+size);
					if(size>limit_count){
						size=limit_count;
					}
					List<LogExpVO> list=new ArrayList<>();
					try {
						for(int i=0; i<size; i++){
							list.add(logExpQueue.take());
						}
						RedisListUtil.appendList(redisTemplate, REDIS_KEY_RET, list);
					} catch (Exception e) {
						logger.warn("---saveLogExpQueue--retQueue:"+e.getMessage(), e);
					}
				}
			}
			
		});
	}
	
	public LogExpVO getLogExp(){
		return logExp;
	}
}
