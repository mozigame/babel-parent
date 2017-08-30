package com.babel.common.core.redislist;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisZSetCommands.Tuple;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationUtils;

public class OriRedisTemplate<E,K,V> extends RedisTemplate<K, V> {
	@Override
	public List<Object> executePipelined(final RedisCallback<?> action,
		final	RedisSerializer<?> resultSerializer) {
		return execute(new RedisCallback<List<Object>>() {
			public List<Object> doInRedis(RedisConnection connection) throws DataAccessException {
				connection.openPipeline();
				boolean pipelinedClosed = false;
				try {
					Object result = action.doInRedis(connection);
					if (result != null) {
						throw new InvalidDataAccessApiUsageException(
								"Callback cannot return a non-null value as it gets overwritten by the pipeline");
					}
					List<Object> closePipeline = connection.closePipeline();
					pipelinedClosed = true;
					return deserializeMixedResults(closePipeline, getValueSerializer(),getHashKeySerializer(),resultSerializer);
				} finally {
					if (!pipelinedClosed) {
						connection.closePipeline();
					}
				}
			}
		});
	}
	
	/**
	 * 
	* @Title: executeConn
	* @Description: 序列化POJO
	* @return List<Object>
	* @throws
	 */
	public List<Object> executeConn(final RedisCallback<?> action,final HashMapper<E, K, V> jhm) {
			return execute(new RedisCallback<List<Object>>() {
				public List<Object> doInRedis(RedisConnection connection) throws DataAccessException {
					connection.openPipeline();
					boolean pipelinedClosed = false;
					try {
						Object result = action.doInRedis(connection);
						if (result != null) {
							throw new InvalidDataAccessApiUsageException(
									"Callback cannot return a non-null value as it gets overwritten by the pipeline");
						}
						List<Object> closePipeline = connection.closePipeline();
						pipelinedClosed = true;
						return deserializeMixedResults(closePipeline, getHashKeySerializer(),getHashValueSerializer(),jhm);
					} finally {
						if (!pipelinedClosed) {
							connection.closePipeline();
						}
					}
				}
			});
		}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<Object> deserializeMixedResults(List<Object> rawValues, RedisSerializer valueSerializer,
			RedisSerializer hashKeySerializer, RedisSerializer hashValueSerializer) {
		if (rawValues == null) {
			return null;
		}
		List<Object> values = new ArrayList<Object>();
		for (Object rawValue : rawValues) {
			if (rawValue instanceof byte[] && valueSerializer != null) {
				values.add(valueSerializer.deserialize((byte[]) rawValue));
			} else if (rawValue instanceof List) {
				// Lists are the only potential Collections of mixed values....
				values.add(deserializeMixedResults((List) rawValue, valueSerializer, hashKeySerializer, hashValueSerializer));
			} else if (rawValue instanceof Set && !(((Set) rawValue).isEmpty())) {
				values.add(deserializeSet((Set) rawValue, valueSerializer));
			} else if (rawValue instanceof Map && !(((Map) rawValue).isEmpty())
					&& ((Map) rawValue).values().iterator().next() instanceof byte[]) {
				values.add(SerializationUtils.deserialize((Map) rawValue, hashKeySerializer, hashValueSerializer));
			} else {
				values.add(rawValue);
			}
		}
		return values;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<Object> deserializeMixedResults(List<Object> rawValues,RedisSerializer hashKeySerializer, RedisSerializer hashValueSerializer,HashMapper<E, K, V> jhm) {
		if (rawValues == null) {
			return null;
		}
		List<Object> values = new ArrayList<Object>();
		for (Object rawValue : rawValues) {
			Map<byte[], byte[]>  obj = (Map<byte[], byte[]>) rawValue;
			Map<K, V> map = new LinkedHashMap<K, V>(obj.size());
			for (Map.Entry<byte[], byte[]> entry : obj.entrySet()) {
				map.put((K) hashKeySerializer.deserialize((entry.getKey())), (V) hashValueSerializer.deserialize((entry.getValue())));
			}
			values.add(jhm.fromHash(map));
		}
		return values;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Set<?> deserializeSet(Set rawSet, RedisSerializer valueSerializer) {
		if (rawSet.isEmpty()) {
			return rawSet;
		}
		Object setValue = rawSet.iterator().next();
		if (setValue instanceof byte[] && valueSerializer != null) {
			return (SerializationUtils.deserialize((Set) rawSet, valueSerializer));
		} else if (setValue instanceof Tuple) {
			return convertTupleValues(rawSet, valueSerializer);
		} else {
			return rawSet;
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Set<TypedTuple<V>> convertTupleValues(Set<Tuple> rawValues, RedisSerializer valueSerializer) {
		Set<TypedTuple<V>> set = new LinkedHashSet<TypedTuple<V>>(rawValues.size());
		for (Tuple rawValue : rawValues) {
			Object value = rawValue.getValue();
			if (valueSerializer != null) {
				value = valueSerializer.deserialize(rawValue.getValue());
			}
			set.add(new DefaultTypedTuple(value, rawValue.getScore()));
		}
		return set;
	}
}