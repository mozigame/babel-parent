package com.babel.common.core.util;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

public class RedisIdUtil {
	public static final String REDIS_KEY_SEQUENCE_CID="redisSequenceId";

	/**
	 * 初始化redis的cid初始值
	 * @param cidKey
	 */
	public synchronized static void initRedisKeyCid(String cidKey, Long initCid){
		RedisTemplate redisTemplate=RedisUtil.getRedisTemplate();
		if(redisTemplate!=null){
			boolean isExist=redisTemplate.opsForHash().hasKey(REDIS_KEY_SEQUENCE_CID, cidKey);//customer 自增的id
			if(!isExist){
				RedisSerializer hashKey=redisTemplate.getHashKeySerializer();
				RedisSerializer hashValue=redisTemplate.getHashValueSerializer();
				redisTemplate.setHashKeySerializer(new StringRedisSerializer(StandardCharsets.UTF_8));
				redisTemplate.setHashValueSerializer(new StringRedisSerializer(StandardCharsets.UTF_8));
				redisTemplate.opsForHash().put(REDIS_KEY_SEQUENCE_CID, cidKey, ""+initCid);//设置自增涨的redis id初始值
				redisTemplate.setHashKeySerializer(hashKey);
				redisTemplate.setHashValueSerializer(hashValue);
			}
		}
	}
	
	public synchronized static void initRedisKeyCid(String cidKey){
		Long initCid=10000000l;
		initRedisKeyCid(cidKey, initCid);
	}
	
	public final static boolean hasSequenceId(String cidKey){
		return RedisUtil.getRedisTemplate().opsForHash().hasKey(REDIS_KEY_SEQUENCE_CID, cidKey);
	}
	
	/**
	 * redis的cid生成器
	 * @param cidKey
	 * @return
	 */
	public static Long getRedisNextCid(String cidKey){
		return getRedisNextCid(cidKey, 1);
	}
	
	/**
	 * redis的cid生成器
	 * @param cidKey
	 * @return
	 */
	public static Long getRedisNextCid(String cidKey, int increment){
		if(increment==0){
			increment=1;
		}
		Long cid=null;
		RedisTemplate redisTemplate=RedisUtil.getRedisTemplate();
		if(redisTemplate!=null){
			cid=redisTemplate.opsForHash().increment(REDIS_KEY_SEQUENCE_CID, cidKey, increment);
		}
		return cid;
	}
	
	/**
	 * redis的cid生成器
	 * @param cidKey
	 * @return
	 */
	public static List<Long> getRedisNextCids(String cidKey, int size){
		if(size==0){
			size=1;
		}
		List<Long> cidList=new ArrayList<>();
		RedisTemplate redisTemplate=RedisUtil.getRedisTemplate();
		if(redisTemplate!=null){
			Long cid=redisTemplate.opsForHash().increment(REDIS_KEY_SEQUENCE_CID, cidKey, size);
			if(size>0){
				for(long i=size-1; i>=0; i--){
					cidList.add(cid-i);
				}
			}
		}
		return cidList;
	}
}
