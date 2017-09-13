package com.babel.common.core.util;

import java.nio.charset.StandardCharsets;

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
		Long initCid=100000000l;
		initRedisKeyCid(cidKey, initCid);
	}
	
	/**
	 * redis的cid生成器
	 * @param cidKey
	 * @return
	 */
	public static Long getRedisNextCid(String cidKey){
		Long cid=null;
		RedisTemplate redisTemplate=RedisUtil.getRedisTemplate();
		if(redisTemplate!=null){
			cid=redisTemplate.opsForHash().increment(REDIS_KEY_SEQUENCE_CID, cidKey, 1);
		}
		return cid;
	}
}
