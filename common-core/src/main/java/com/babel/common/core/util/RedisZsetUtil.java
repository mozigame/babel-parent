package com.babel.common.core.util;

import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;

public class RedisZsetUtil {
	private static final Logger logger = Logger.getLogger(RedisZsetUtil.class);
	private static RedisTemplate redisTemplate=null;
	public static void add(String redisKey, String data, Long time){
		if(redisTemplate==null){
			redisTemplate=RedisUtil.getRedisTemplate();
		}
		if(redisTemplate!=null){
			redisTemplate.opsForZSet().add(redisKey, data, time);	
		}
		else{
			logger.warn("----add--redisTemplate is null");
		}
	}
	
	public static Set<TypedTuple<String>> geTuples(String redisKey, Long time){
		if(redisTemplate==null){
			redisTemplate=RedisUtil.getRedisTemplate();
		}
		if(redisTemplate!=null){
			Set<TypedTuple<String>> set=RedisUtil.getRedisTemplate().opsForZSet().reverseRangeByScoreWithScores(redisKey, time, -1);
			return set;
		}
		else{
			logger.warn("----geTuples--redisTemplate is null");
		}
		return null;
	}
}
