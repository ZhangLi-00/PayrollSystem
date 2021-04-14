package com.tomorrow.util;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtil {
    private static JedisPool jedisPool = null;
    private static Jedis jedis = null;

    /**
     * 初始化jedis连接池
     */
    private static void initPool(){
        try{
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxTotal(Constant.REDIS_MAX_ACTIVE); //最大连接数
            jedisPoolConfig.setMaxIdle(Constant.REDIS_MAX_IDLE); //最大空闲连接数
            jedisPoolConfig.setMaxWaitMillis(Constant.REDIS_MAX_WAIT);//获取可用连接的最大等待时间
            jedisPool = new JedisPool(jedisPoolConfig,Constant.REDIS_HOST,Constant.REDIS_PORT);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 初始化jedis实例
     * @return
     */
    public synchronized static Jedis getJedis(){
        try{
            if(jedisPool==null){
                initPool();
            }
            jedis = jedisPool.getResource();
            return jedis;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 存字符串
     * @param key
     * @param value
     * @return
     */
    public static void setString(String key,String value){
        jedis = getJedis();
        jedis.set(key,value);
        jedis.expire(key,Constant.REDIS_EXPIRE_TIME);
        jedis.close();
    }

    /**
     * 刷新生存时间
     * @param key
     * @param redisExpireTime
     */
    public static void flushExpire(String key, int redisExpireTime){
        jedis = getJedis();
        jedis.expire(key,Constant.REDIS_EXPIRE_TIME);
        jedis.close();
    }
    /**
     * 取字符串
     * @param key
     * @return
     */
    public static String getString(String key){
        jedis = getJedis();
        String value = jedis.get(key);
        jedis.close();
        return value;
    }

}