package com.babel.common.core.service.impl;

import org.springframework.data.redis.core.RedisTemplate;

import com.babel.common.core.service.IRedisCacheClient;

public class RedisCacheClient implements IRedisCacheClient{
	
	
	private RedisTemplate<String, String> redisTemplate;
	
	@Override
	public void listLpush(String key, String value) {
		this.redisTemplate.opsForList().leftPush(key, value);
		
	}

	@Override
	public String listLpop(String key) {
		// TODO Auto-generated method stub
		return redisTemplate.opsForList().rightPop(key);
	}

}
