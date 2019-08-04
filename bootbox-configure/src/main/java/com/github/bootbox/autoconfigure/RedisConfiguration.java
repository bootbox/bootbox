package com.github.bootbox.autoconfigure;

import com.github.bootbox.cache.CacheService;
import com.github.bootbox.cache.redis.RedisCacheService;
import com.github.bootbox.config.ConfigService;
import com.github.bootbox.config.redis.RedisConfigService;
import com.github.bootbox.redis.PrimaryRedisService;
import com.github.bootbox.redis.RedisService;
import com.github.bootbox.redis.SpringRedisProviderAdapter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Created by zgq on 17-3-9.
 */
@Configuration
public class RedisConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisConfiguration.class);

    @Bean
    @ConditionalOnClass(ConfigService.class)
    @ConditionalOnMissingBean(ConfigService.class)
    public ConfigService redisConfigService(RedisService redisClient) {
        return new RedisConfigService(redisClient);
    }

    @Bean
    @ConditionalOnClass(CacheService.class)
    public CacheService redisCacheService(RedisService redisClient) {
        return new RedisCacheService(redisClient);
    }


    @Configuration
    @ConditionalOnProperty(name = "redis.primary.enable", havingValue = "true")
    public static class PrimaryConfig {
        @Value("${redis.primary.host}")
        private String host;
        @Value("${redis.primary.port}")
        private int port;
        @Value("${redis.primary.password}")
        private String password;


        @Primary
        @Bean(name = "primaryRedisConnectionFactory")
        public RedisConnectionFactory primaryRedisConnectionFactory() {
            return createConnection(host, port, password);
        }

        @Bean(name = "primaryStringRedisTemplate")
        @Primary
        public StringRedisTemplate primaryRedisTemplate(@Qualifier("primaryRedisConnectionFactory")
                                                                        RedisConnectionFactory cf) {
            StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
            stringRedisTemplate.setEnableTransactionSupport(true);
            stringRedisTemplate.setConnectionFactory(cf);
            return stringRedisTemplate;
        }

        @Primary
        @Bean
        public PrimaryRedisService createProvider(@Qualifier("primaryStringRedisTemplate")
                                                                   StringRedisTemplate redisTemplate) {
            LOGGER.info("Creating primary redis provider");
            return new PrimaryRedisService(new SpringRedisProviderAdapter(redisTemplate));
        }
    }

    @Configuration
    @ConditionalOnProperty(name = "redis.secondary.enable", havingValue = "true")
    public static class SecondaryConfig {
        @Value("${redis.secondary.host}")
        private String secondaryHost;

        @Value("${redis.secondary.password}")
        private String secondaryPassword;

        @Value("${redis.secondary.port}")
        private int secondaryPort;

        @Bean(name = "secondaryRedisConnectionFactory")
        public RedisConnectionFactory secondaryRedisConnectionFactory() {
            return createConnection(secondaryHost, secondaryPort, secondaryPassword);
        }

        @Bean(name = "secondaryStringRedisTemplate")
        public StringRedisTemplate secondaryStringRedisTemplate(@Qualifier("secondaryRedisConnectionFactory")
                                                                                RedisConnectionFactory cf) {
            StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
            stringRedisTemplate.setConnectionFactory(cf);
            stringRedisTemplate.setEnableTransactionSupport(true);
            return stringRedisTemplate;
        }
    }

    private static RedisConnectionFactory createConnection(String host, int port, String password) {
//        JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory();
//        redisConnectionFactory.setHostName(host);
//        redisConnectionFactory.setPort(port);
//        redisConnectionFactory.setPassword(password);
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        if (StringUtils.isBlank(password)) {
            redisStandaloneConfiguration.setPassword(RedisPassword.none());
        } else {
            redisStandaloneConfiguration.setPassword(RedisPassword.of(password));
        }
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setHostName(host);
        LettuceConnectionFactory redisConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);
        return redisConnectionFactory;
    }
}
