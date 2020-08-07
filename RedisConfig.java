package com.enjoy.config;

import cn.yuexiang365.common.utils.LongRedisTemplate;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

/**
 * @program: yueenjoy
 * @description: Redis相关配置
 * @author: LiZhaofu
 * @create: 2020-04-27 09:24
 **/

@Configuration
@Slf4j
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfig {

//    @Bean
//    @ConditionalOnMissingBean(name = "jedisConnectionFactory")
//    public JedisConnectionFactory jedisConnectionFactory(RedisProperties redisProperties) {
//        log.info("redisProperties.host:{}",redisProperties.getHost());
//        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(redisProperties.getHost(),redisProperties.getPort());
//        return new JedisConnectionFactory(redisStandaloneConfiguration);
//    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory(RedisProperties redisProperties){
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setDatabase(redisProperties.getDatabase());
        config.setHostName(redisProperties.getHost());
        config.setPassword(redisProperties.getPassword());

        JedisClientConfiguration.JedisClientConfigurationBuilder builder = JedisClientConfiguration.builder();

        builder.connectTimeout(redisProperties.getTimeout());

        JedisPoolConfig poolCofig = new JedisPoolConfig();

        poolCofig.setMaxIdle(redisProperties.getJedis().getPool().getMaxIdle());
        poolCofig.setMinIdle(redisProperties.getJedis().getPool().getMinIdle());
        poolCofig.setMaxTotal(redisProperties.getJedis().getPool().getMaxActive());
        poolCofig.setMaxWaitMillis(redisProperties.getJedis().getPool().getMaxWait().toMillis());

        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(config, builder.build());

        jedisConnectionFactory.getConnection().close();

        return jedisConnectionFactory;
    }

    @SuppressWarnings("rawtypes")
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        //使用StringRedisSerializer来序列化和反序列化redis的key值
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);

        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        return new StringRedisTemplate(redisConnectionFactory);
    }

    @Bean
    public LongRedisTemplate longRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        return new LongRedisTemplate(redisConnectionFactory);
    }

    /**
     * 对hash类型的数据操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public HashOperations<String, String, Object> hashOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForHash();
    }

    /**
     * 对redis字符串类型数据操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public ValueOperations<String, Object> valueOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForValue();
    }

    /**
     * 对链表类型的数据操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public ListOperations<String, Object> listOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForList();
    }

    /**
     * 对无序集合类型的数据操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public SetOperations<String, Object> setOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForSet();
    }

    /**
     * 对有序集合类型的数据操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public ZSetOperations<String, Object> zSetOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForZSet();
    }


}
