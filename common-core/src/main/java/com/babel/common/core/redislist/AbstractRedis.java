package com.babel.common.core.redislist;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.data.redis.hash.JacksonHashMapper;

public abstract class AbstractRedis<E, K, V> {
	@Resource
	private OriRedisTemplate<E,K, V> oriRedisTemplate;
	private Class<E> entityClass;
	private String region;
	
	@SuppressWarnings({ "unchecked"})
	public AbstractRedis() {
		Type c = (((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0]);
		if (c instanceof ParameterizedType) {
			entityClass = (Class<E>) ((ParameterizedType) c).getRawType();
		} else {
			entityClass = (Class<E>) c;
		}
		region = entityClass.getSimpleName();
	}
	/**
	 * 
	* @Title: setKey
	* @Description: 设置自增长的Key
	* @return Long
	* @throws
	 */
	@SuppressWarnings({ "unchecked"})
	public Long setKey(String region) {
		return oriRedisTemplate.opsForValue().increment((K) region, 1);
	}
	
	/**
	 * 
	* @Title: set
	* @Description: 保存实体
	* @return void
	* @throws
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void save(V v) {
		Map<K, V> map = new JacksonHashMapper(entityClass).toHash(v);
		oriRedisTemplate.boundHashOps((K) (region + ":H:" + setKey(region)))
				.putAll(map);
	};
	/**
	 * 
	* @Title: get
	* @Description: 根据redis 中对象的key 获取对象信息
	* @return E
	* @throws
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public E get(String key) {
		Map<K, V> map = (Map<K, V>) oriRedisTemplate.boundHashOps(
				(K) (region + ":H:" + key)).entries();
		return (E) new JacksonHashMapper(entityClass).fromHash(map);
	}
	
	/**
	 * 
	* @Title: list
	* @Description:返回对象列表 传入对象的key
	* @return List<E>
	* @throws
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<E> list(List<String> keys) {
		List<E> lists = new ArrayList<E>();
		for (int i = 0; i < keys.size(); i++) {
			E e = (E) new JacksonHashMapper(entityClass).fromHash(oriRedisTemplate
					.boundHashOps((K)(region + ":H:"+keys.get(i))).entries());
			lists.add(e);
		}
		return lists;
	}
	
	/**
	 * 
	* @Title: listConn
	* @Description: 管道批量获取对象集合 传入对象的key
	* @return List<E>
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public List<E> listPipe(final List<String> keys) {
		List<Object> result = oriRedisTemplate.executePipelined(new RedisCallback<List<Object>>() {
			@Override
			public List<Object> doInRedis(RedisConnection connection)
					throws DataAccessException {
				for (int i = 0; i < keys.size(); i++) {
					byte[] key = oriRedisTemplate.getStringSerializer().serialize(
							(region + ":H:"+keys.get(i)));
					connection.hGetAll(key);
				}
				return null;
			}
		});
		return ((List<E>) result);
	}
	
	@SuppressWarnings("unchecked")
	public List<E> listConn(final List<String> keys) {
		HashMapper<E, K, V> jhm = (HashMapper<E, K, V>) new JacksonHashMapper<E>(entityClass);
		List<Object> result = oriRedisTemplate.executeConn(new RedisCallback<Map<byte[], byte[]>>() {
			@Override
			public Map<byte[], byte[]> doInRedis(RedisConnection connection)
					throws DataAccessException {
//				Map<byte[], byte[]> map = new HashMap<byte[], byte[]>();
				for (int i = 0; i < keys.size(); i++) {
					byte[] key = oriRedisTemplate.getStringSerializer().serialize(
							(region + ":H:"+keys.get(i)));
					connection.hGetAll(key);
				}
				return null;
			}
		},jhm);
		return ((List<E>) result);
	}

	// public void set(V v) {
	// oriRedisTemplate.boundHashOps((K) (region + ":H:" + setKey(region)))
	// .putAll(new BeanUtilsHashMapper(entityClass).toHash(v));
	// };
	//
	// public E getObj(List<Object> hkeys) {
	// return (E) new BeanUtilsHashMapper(entityClass).fromHash(oriRedisTemplate
	// .boundHashOps((K) (region + ":H:" + getKey())).entries());
	// }
	
	/**
	 * 
	* @Title: getKey
	* @Description:获取最新的key
	* @return String
	* @throws
	 */
	public String getKey() {
		return (String) oriRedisTemplate.boundValueOps((K) (region)).get();
	}
}