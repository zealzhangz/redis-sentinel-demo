package com.zealzhangz.redissentineldemo.redis.service.impl;

import com.zealzhangz.redissentineldemo.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @author Created by zealzhangz.<br/>
 * @version Version: 0.0.1
 * @date DateTime: 2019/07/30 16:08:00<br/>
 */
@Service
public class RedisClusterServiceImpl implements RedisService {
    @Autowired
    private RedisTemplate<Serializable, Serializable> redisTemplate;

    @Override
    public void save(Serializable key, Serializable value, long exp) {
        redisTemplate.opsForValue().set(key,value,exp, TimeUnit.SECONDS);
    }

    @Override
    public void save(Serializable key, Serializable value) {
        redisTemplate.opsForValue().set(key,value);
    }

    @Override
    public void expire(String key, int seconds) {
        redisTemplate.expire(key,seconds,TimeUnit.SECONDS);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void batchDelete(String keyPrefix) {
        redisTemplate.delete(redisTemplate.keys(keyPrefix +"*"));
    }

    @Override
    public Boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public Object get(Object key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public Long increment(String key, long delta) {
        return redisTemplate.opsForValue().increment(key,delta);
    }

    @Override
    public Long increment(String key) {
        return increment(key,1);
    }
}
