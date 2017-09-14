package com.babel.common.core.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * redis list数据获取
 * 处理机制:leftPush, rightPop
 * @author jinhe.chen
 *
 */
public class RedisListUtil {
	private static final Logger logger = Logger.getLogger(RedisListUtil.class);
	private static final int LIST_LOG_INFO_COUNT=5;
	/**
	 * 批量从redis获取日志数据，事务模式，高共发下可能会有丢失(获取size后又有数据插入，则trim的数据会把新插的数据一起删了)
	 * 
	 * @param redisKey
	 * @param sizeGet
	 * @return
	 */
	public static List getListWithTrim(final RedisTemplate redisTemplate, final String redisKey, final long sizeGet){
		List list=new ArrayList();
		synchronized (redisKey) {
			final long dataSize = redisTemplate.opsForList().size(redisKey);
			
			if(dataSize==0){
				return list;
			}
			long size=dataSize;
			if(size>sizeGet){
				size=sizeGet;
			}
			final long findSize=size;
			List exeList = (List) redisTemplate.execute(new SessionCallback<List>() {
				@Override
				public List execute(RedisOperations operations) throws DataAccessException {
					operations.multi();
					operations.opsForList().range(redisKey, 0, findSize - 1);
					if (dataSize == findSize) {
						operations.opsForList().trim(redisKey, 10, 1);// 清空所有数据
					} else {
						operations.opsForList().trim(redisKey, findSize, dataSize);
					}
					return operations.exec();
				}
			});
			
			if(exeList.size()>0){
				list=(List)exeList.get(0);
			}
			logger.debug("----getListWithTrim--redisKey="+redisKey+" size="+size+"/"+dataSize+" "+list.size());
		}
		return list;
	}
	
	/**
	 * 批量从redis获取日志数据，事务模式，高共发下不会丢失，但如果批量数据太大，对redis性能会有影响，建议分小批量处理(即sizeGet应该就一些)
	 * @param redisKey
	 * @param sizeGet
	 * @return
	 */
	public static  List getListWithPop(final RedisTemplate redisTemplate, final String redisKey, final long sizeGet){
		List exeList=new ArrayList<>();
		synchronized (redisKey) {
			Long removeCount=redisTemplate.opsForList().remove(redisKey, 0, "del");
			if(removeCount>0){
				logger.info("-----getListWithPop--redisKey="+redisKey+" remove:del removeCount="+removeCount);
			}
			removeCount=redisTemplate.opsForList().remove(redisKey, 0, null);
			if(removeCount>0){
				logger.info("-----getListWithPop--redisKey="+redisKey+" remove:null removeCount="+removeCount);
			}
			
			final long dataSize = redisTemplate.opsForList().size(redisKey);
			
			if(dataSize==0){
				return exeList;
			}
			long size=dataSize;
			if(size>sizeGet){
				size=sizeGet;
			}
		
			final long findSize=size;
			try {
				exeList = (List) redisTemplate.execute(new SessionCallback<List>() {
					@Override
					public List execute(RedisOperations operations) throws DataAccessException {
						operations.multi();
						for(int i=0; i<findSize; i++){
							operations.opsForList().rightPop(redisKey);
						}
						return operations.exec();
					}
				});
				int count=LIST_LOG_INFO_COUNT;
				if(exeList.size()<count){
					count=exeList.size();
				}
				logger.debug("----getListWithPop--redisKey="+redisKey+" size="+size+"/"+dataSize+" exeList="+exeList.subList(0,  count));
			} catch (Exception e) {
				logger.error("-----getListWithPop--redisKey="+redisKey+" size="+size+"/"+dataSize, e);
			}
		}
		return exeList;
	}
	
	
	public static void appendList(final RedisTemplate redisTemplate, final String redisKey, List list){
		if(list.size()==0){
			return;
		}
		
		synchronized(redisKey){
			redisTemplate.boundListOps(redisKey).leftPushAll(list.toArray());
		}
	}
	
	public static void leftPush(final RedisTemplate redisTemplate, final String redisKey, Object object){
		if(object==null){
			return;
		}
		
//		synchronized(redisKey){
		redisTemplate.boundListOps(redisKey).leftPush(object);
//		}
	}
	
	public static List getListWithRemove(final RedisTemplate redisTemplate, final String redisKey, final long sizeGet){
		List list=new ArrayList();
		synchronized(redisKey){
			Long removeCount=redisTemplate.opsForList().remove(redisKey, 0, "del");
			if(removeCount>0){
				logger.info("-----getListWithRemove--redisKey="+redisKey+" remove:del removeCount="+removeCount);
			}
			
			final long dataSize = redisTemplate.opsForList().size(redisKey);
			
			
			if(dataSize==0){
				return list;
			}
			long size=dataSize;
			if(size>sizeGet){
				size=sizeGet;
			}
			list=redisTemplate.opsForList().range(redisKey, 0, size - 1);
			final long findSize=list.size();
			if(findSize==0){
				return list;
			}
	
			Object object=null;
			try {
				final RedisSerializer<String> serial = redisTemplate.getKeySerializer();
				final RedisSerializer<String> serialValue = redisTemplate.getValueSerializer();
				byte[] value =serialValue.serialize("del");
				byte[] key = serial.serialize(redisKey);
				
				
				object = redisTemplate.execute(new RedisCallback<Object>() {
					public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
						for(int i=0; i<findSize; i++){
							redisConnection.lSet(key, i, value);
						}
						return redisConnection.lRem(key, 0, value);
					}
				});
				logger.debug("----getListWithRemove--redisKey="+redisKey+" size="+size+"/"+dataSize+" findSize="+findSize+" return="+object+" endSize="+redisTemplate.opsForList().size(redisKey));
			} catch (Exception e) {
				logger.error("-----getListWithRemove--redisKey="+redisKey+" size="+size+"/"+dataSize+" findSize="+findSize, e);
			}
		}
		return list;
	}
}

