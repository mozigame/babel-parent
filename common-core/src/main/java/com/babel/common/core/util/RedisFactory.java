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
	public static RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory, String redisKeySerializer, String redisHashKeySerializer, String redisValueSerializer) {
		RedisTemplate template = new StringRedisTemplate(factory);
		// 定义key序列化方式
		RedisSerializer<String> redisSerializer = new StringRedisSerializer();// Long类型会出现异常信息;需要我们上面的自定义key生成策略，一般没必要
		RedisSerializer jdkSerializer = new JdkSerializationRedisSerializer();
		if (RedisConstant.REDIS_SERIALIZER_STRING.equals(redisKeySerializer)) {
			template.setKeySerializer(redisSerializer);
		}
		else{
			template.setKeySerializer(jdkSerializer);
		}

		if (RedisConstant.REDIS_SERIALIZER_STRING.equals(redisHashKeySerializer)) {
			template.setHashKeySerializer(redisSerializer);
		}
		else{
			template.setHashKeySerializer(jdkSerializer);
		}

		if (RedisConstant.REDIS_SERIALIZER_JDK.equals(redisValueSerializer)) {
			template.setHashValueSerializer(jdkSerializer);
			template.setValueSerializer(jdkSerializer);
		} else if (RedisConstant.REDIS_SERIALIZER_JACKSON2JSON.equals(redisValueSerializer)) {
			// 定义value的序列化方式
			Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
			ObjectMapper om = new ObjectMapper();
			om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
			om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
			jackson2JsonRedisSerializer.setObjectMapper(om);
			
			template.setValueSerializer(jackson2JsonRedisSerializer);
			template.setHashValueSerializer(jackson2JsonRedisSerializer);
		}
		RedisSerializer valueSerializer = new JdkSerializationRedisSerializer();
		template.afterPropertiesSet();
		return template;
	}
}
