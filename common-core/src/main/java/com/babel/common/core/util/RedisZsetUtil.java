package com.babel.common.core.util;

import java.util.HashMap;
import java.util.Map;
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
	
	public static Map<String, Double> geTuples(String redisKey, Long score){
		if(redisTemplate==null){
			redisTemplate=RedisUtil.getRedisTemplate();
		}
		if(redisTemplate!=null){
			Set<TypedTuple<String>> set=redisTemplate.opsForZSet().reverseRangeByScoreWithScores(redisKey, score, 3516091475392L);
			Map<String, Double> map=new HashMap<>();
			for(TypedTuple<String> tuple:set){
				map.put(tuple.getValue(), tuple.getScore());
			}
			return map;
		}
		else{
			logger.warn("----geTuples--redisTemplate is null");
		}
		return new HashMap<>();
	}
}
