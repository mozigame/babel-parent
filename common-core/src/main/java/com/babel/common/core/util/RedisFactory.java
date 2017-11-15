package com.babel.common.core.util;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.babel.common.redis.RedisConstant;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RedisFactory {
	public static RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory, String redisKeySerializer, String redisHashKeySerializer, String redisValueSerializer, String redisHashValueSerializer) {
		RedisTemplate template = new StringRedisTemplate(factory);
		// 定义key序列化方式
		RedisSerializer<String> redisSerializer = new StringRedisSerializer();// Long类型会出现异常信息;需要我们上面的自定义key生成策略，一般没必要
		RedisSerializer jdkSerializer = new JdkSerializationRedisSerializer();
		
		Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = getJackson2JsonRedisSerializer();
		//KeySerializer
		if (RedisConstant.REDIS_SERIALIZER_STRING.equals(redisKeySerializer)) {
			template.setKeySerializer(redisSerializer);
		}
		else{
			template.setKeySerializer(jdkSerializer);
		}

		//HashKeySerializer
		if (RedisConstant.REDIS_SERIALIZER_STRING.equals(redisHashKeySerializer)) {
			template.setHashKeySerializer(redisSerializer);
		}
		else{
			template.setHashKeySerializer(jdkSerializer);
		}
		
		//ValueSerializer
		if (RedisConstant.REDIS_SERIALIZER_JACKSON2JSON.equals(redisValueSerializer)) {
			template.setValueSerializer(jackson2JsonRedisSerializer);
		}
		else{
			template.setValueSerializer(jdkSerializer);
		}
		
		//HashValueSerializer
		if (RedisConstant.REDIS_SERIALIZER_JACKSON2JSON.equals(redisHashValueSerializer)) {
			template.setHashValueSerializer(jackson2JsonRedisSerializer);
		}
		else{
			template.setHashValueSerializer(jdkSerializer);
		}
		template.afterPropertiesSet();
		return template;
	}

	private static Jackson2JsonRedisSerializer getJackson2JsonRedisSerializer() {
		// 定义value的序列化方式
		Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		jackson2JsonRedisSerializer.setObjectMapper(om);
		return jackson2JsonRedisSerializer;
	}
	
	public static RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory, String redisKeySerializer, String redisHashKeySerializer, String redisValueSerializer) {
		String redisHashValueSerializer=redisValueSerializer;
		return redisTemplate(factory, redisKeySerializer, redisHashKeySerializer, redisValueSerializer, redisHashValueSerializer);
	}
}
