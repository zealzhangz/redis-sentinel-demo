package com.zealzhangz.redissentineldemo.redis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPoolConfig;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * @author Created by zealzhangz.<br/>
 * @version Version: 0.0.1
 * @date DateTime: 2019/07/30 17:14:00<br/>
 */
@Configuration
@EnableCaching
public class RedisSentinelConfig extends CachingConfigurerSupport {

    @Autowired
    private RedisProperties properties;

    @Bean(name = "redisTemplate")
    public RedisTemplate redisTemplate() {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(connectionFactory());
        setSerializer(template);
        return template;
    }

    private JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMinIdle(properties.getPool().getMinIdle());
        jedisPoolConfig.setMaxIdle(properties.getPool().getMaxIdle());
        jedisPoolConfig.setMaxTotal(properties.getPool().getMaxActive());
        jedisPoolConfig.setMaxWaitMillis(properties.getPool().getMaxWait());

        return jedisPoolConfig;
    }

    /**
     * Jedis
     */
    @Bean
    public RedisConnectionFactory connectionFactory() {
        RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration()
                .master(properties.getSentinel().getMaster());
        Set<String> hostAndPorts = StringUtils.commaDelimitedListToSet(properties.getSentinel().getNodes());
            for(String hostPort : hostAndPorts){
                sentinelConfig.addSentinel(readHostAndPortFromString(hostPort));
            }
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory(sentinelConfig,jedisPoolConfig());
            if(!StringUtils.isEmpty(properties.getPassword())){
                connectionFactory.setPassword(properties.getPassword());
            }
        return connectionFactory;
    }

    private RedisNode readHostAndPortFromString(String hostAndPort) {
        String[] args = StringUtils.split(hostAndPort, ":");
        Assert.notNull(args, "HostAndPort need to be seperated by  ':'.");
        Assert.isTrue(args.length == 2, "Host and Port String needs to specified as host:port");
        return new RedisNode(args[0], Integer.valueOf(args[1]));
    }

    /**
     * Serializer
     *
     * @param template
     */
    private void setSerializer(StringRedisTemplate template) {
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
    }

    /**
     * The Strategy of Producing Key
     *
     * @return
     */
    @Bean
    public KeyGenerator wiselyKeyGenerator() {
        return (Object target, Method method, Object... params) ->{
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName());
            sb.append(method.getName());
            for (Object obj : params) {
                sb.append(obj.toString());
            }
            return sb.toString();
        };
    }

}
