package com.babel.common.core.redis;

import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;

import com.babel.common.core.util.RedisUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;



public class RedisManager {
	private static Logger logger =  Logger.getLogger(RedisManager.class);
    
//    @Value("${redis.host}")
    private String host;
 
//    @Value("${redis.port}")
    private int port;
//    @Value("${redis.database}")
    private int database;
//    @Value("${redis.maxWait}")
    private int maxWait;
    
//    @Value("${redis.password}")
//    private String password;
 
    // 0 - never expire
    private int expire = 0;
 
    private static JedisPool jedisPool = null;
 
    public RedisManager() {
    }
    
    private RedisTemplate redisTemplate;
 
    /**
     * 初始化方法
     */
    public void init(){
    	logger.info("-------redisManager init----");
//        if(null == host || 0 == port){
//            logger.error("请初始化redis配置文件！");
//            throw new NullPointerException("找不到redis配置");
//        }
//        if(jedisPool == null){
        	RedisTemplate redisTemplate=RedisUtil.getRedisTemplate();
        	this.redisTemplate=redisTemplate;
        	if(redisTemplate==null){
        		logger.warn("redisTemplate not found");
        	}
//        	redisTemplate.getConnectionFactory().getClusterConnection().
            //jedisPool = JedisUtil.getJedisPool();
//            jedisPool = new JedisPool(new JedisPoolConfig(), host, port, maxWait);
//        }
    }
    
    public static JedisPool getJedisPool(){
    	return jedisPool;
    }
 
    /**
     * get value from redis
     * @param key
     * @return
     */
    public byte[] get(byte[] key) {
        byte[] value = null;
//        Jedis jedis = jedisPool.getResource();
        try {
//            value = jedis.get(key);
        	value = redisTemplate.getConnectionFactory().getConnection().get(key);
        } finally {
//            jedisPool.returnResource(jedis);
        }
        return value;
    }
    /**
     * get value from redis
     * @param key
     * @return
     */
    public String get(String key) {
        String value=null;
//        Jedis jedis = jedisPool.getResource();
        try {
//            value = jedis.get(key);
//        	value = redisTemplate.getConnectionFactory().getConnection().get(key.getBytes());
        	byte[] bytes = get(key.getBytes());
        	value=new String(bytes);
        } finally {
//            jedisPool.returnResource(jedis);
        }
        return value;
    }
 
    /**
     * set 
     * @param key
     * @param value
     * @return
     */
    public byte[] set(byte[] key, byte[] value) {
//        Jedis jedis = jedisPool.getResource();
        try {
//            jedis.set(key, value);
        	redisTemplate.getConnectionFactory().getConnection().set(key, value);
            if (this.expire != 0) {
            	redisTemplate.getConnectionFactory().getConnection().expire(key, this.expire);
            }
        } finally {
//            jedisPool.returnResource(jedis);
        }
        return value;
    }
    /**
     * set 
     * @param key
     * @param value
     * @return
     */
    public String set(String key,String value) {
//        Jedis jedis = jedisPool.getResource();
        try {
//            jedis.set(key, value);
        	this.set(key.getBytes(), value.getBytes());
//            if (this.expire != 0) {
////                jedis.expire(key, this.expire);
//            	redisTemplate.getConnectionFactory().getConnection().expire(key.getBytes(), arg1)
//            }
        } finally {
//            jedisPool.returnResource(jedis);
        }
        return value;
    }
 
    /**
     * set 
     * @param key
     * @param value
     * @param expire
     * @return
     */
    public byte[] set(byte[] key, byte[] value, int expire) {
//        Jedis jedis = jedisPool.getResource();
        try {
//            jedis.set(key, value);
        	redisTemplate.getConnectionFactory().getConnection().set(key, value);
            if (expire != 0) {
//                jedis.expire(key, expire);
            	redisTemplate.getConnectionFactory().getConnection().expire(key, expire);
            }
        } finally {
//            jedisPool.returnResource(jedis);
        }
        return value;
    }
    /**
     * set 
     * @param key
     * @param value
     * @param expire
     * @return
     */
    public String set(String key,String value, int expire) {
//        Jedis jedis = jedisPool.getResource();
        try {
//            jedis.set(key, value);
//            if (expire != 0) {
//                jedis.expire(key, expire);
//            }
        	this.set(key.getBytes(), value.getBytes(), expire);
        } finally {
//            jedisPool.returnResource(jedis);
        }
        return value;
    }
 
    /**
     * del
     * @param key
     */
    public void del(byte[] key) {
//        Jedis jedis = jedisPool.getResource();
        try {
//            jedis.del(key);
        	redisTemplate.getConnectionFactory().getConnection().del(key);
        } finally {
//            jedisPool.returnResource(jedis);
        }
    }
    /**
     * del
     * @param key
     */
    public void del(String key) {
//        Jedis jedis = jedisPool.getResource();
        try {
//            jedis.del(key);
        	this.del(key.getBytes());
        } finally {
//            jedisPool.returnResource(jedis);
        }
    }
 
    /**
     * flush
     */
    public void flushDB() {
        Jedis jedis = jedisPool.getResource();
        try {
//            jedis.flushDB();//禁止
        } finally {
            jedisPool.returnResource(jedis);
        }
    }
 
    /**
     * size
     */
    public Long dbSize() {
        Long dbSize = 0L;
//        Jedis jedis = jedisPool.getResource();
        try {
            dbSize = redisTemplate.getConnectionFactory().getConnection().dbSize();
        } finally {
//            jedisPool.returnResource(jedis);
        }
        return dbSize;
    }
 
    /**
     * keys
     * @param regex
     * @return
     */
    public Set<byte[]> keys(String pattern) {
        Set<byte[]> keys = null;
//        Jedis jedis = jedisPool.getResource();
        try {
            keys = redisTemplate.getConnectionFactory().getConnection().keys(pattern.getBytes());
        } finally {
//            jedisPool.returnResource(jedis);
        }
        return keys;
    }
 
    public String getHost() {
        return host;
    }
 
    public void setHost(String host) {
        this.host = host;
    }
 
    public int getPort() {
        return port;
    }
 
    public void setPort(int port) {
        this.port = port;
    }
 
    public int getExpire() {
        return expire;
    }
 
    public void setExpire(int expire) {
        this.expire = expire;
    }

	public void setDatabase(int database) {
		this.database = database;
	}

	public void setMaxWait(int maxWait) {
		this.maxWait = maxWait;
	}
    
    
}