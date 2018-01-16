package com.babel.common.core.util;

import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;

public class RedisZsetUtil {
	private static RedisTemplate redisTemplate=null;
	public static void add(String redisKey, String data, Long time){
		redisTemplate=RedisUtil.getRedisTemplate();
		if(redisTemplate!=null){
			redisTemplate.opsForZSet().add(redisKey, data, time);
		}
	}
	
	public static Set<TypedTuple<String>> geTuples(String redisKey, Long time){
		redisTemplate=RedisUtil.getRedisTemplate();
		if(redisTemplate!=null){
			Set<TypedTuple<String>> set=RedisUtil.getRedisTemplate().opsForZSet().reverseRangeByScoreWithScores(redisKey, time, -1);
			return set;
		}
		return null;
	}
}
