package com.zealzhangz.redissentineldemo.redis.service;


import java.io.Serializable;

/**
 * @author Created by zealzhangz.<br/>
 * @version Version: 0.0.1
 * @date DateTime: 2019/07/30 16:07:00<br/>
 */
public interface RedisService {
    /**
     * cache object
     * @param key
     * @param value
     * @param exp
     */
    void save(Serializable key, Serializable value, long exp);

    /**
     * cache object
     * @param key
     * @param value
     */
    void save(Serializable key, Serializable value);

    /**
     * Set key expiration time
     * @param key
     * @param seconds
     */
    void expire(final String key, final int seconds);

    /**
     * delete
     * @param key
     */
    void delete(final String key);

    /**
     * Fuzzy batch delete
     * @param keyPrefix
     */
    void batchDelete(String keyPrefix);

    /**
     * Does key exists
     * @param key
     * @return
     */
    Boolean exists(final String key);

    /**
     * Get cache by key
     * @param key
     * @return
     */
    Object get(Object key);

    /**
     *  Increment an integer value stored as string value under {@code key} by {@code delta}.
     *
     * @param key
     * @param delta
     * @return
     */
    Long increment(String key, long delta);

    /**
     * Increment an integer value stored as string value under key, delta is 1
     * @param key
     * @return
     */
    Long increment(String key);
}
