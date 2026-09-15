package com.example.springredisexample.config;

import com.example.springredisexample.controller.dto.ChatMessage;
import io.lettuce.core.RedisURI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;
import java.util.HashMap;

@Configuration
public class RedisConfig {

  public static final String CACHE_FINANCIAL_SUMMARIES = "financial_summaries";

  @Bean
  public RedisConnectionFactory redisConnectionFactory(@Value("${redis.uri}") String uri) {
    final var redisUri = RedisURI.create(uri);

    final var clientConfigBuilder =
        LettuceClientConfiguration.builder();

    if (redisUri.isSsl()) {
      clientConfigBuilder.useSsl();
    }

    final var clientConfig = clientConfigBuilder.build();
    final var config = LettuceConnectionFactory.createRedisConfiguration(redisUri);

    return new LettuceConnectionFactory(config, clientConfig);
  }

  @Bean
  public RedisTemplate<String, ChatMessage> redisTemplate(RedisConnectionFactory connectionFactory) {
    final var template = new RedisTemplate<String, ChatMessage>();
    template.setConnectionFactory(connectionFactory);
    template.setKeySerializer(RedisSerializer.string());
    template.setValueSerializer(RedisSerializer.json());
    template.afterPropertiesSet();

    return template;
  }

  @Bean
  public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
    final var defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
        .entryTtl(Duration.ofHours(1)) // Fallback TTL
        .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()))
        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()));

    final var cacheConfigs = new HashMap<String, RedisCacheConfiguration>();
    cacheConfigs.put(CACHE_FINANCIAL_SUMMARIES,  defaultConfig.entryTtl(Duration.ofMinutes(15)));

    return RedisCacheManager.builder(connectionFactory)
        .cacheDefaults(defaultConfig)
        .withInitialCacheConfigurations(cacheConfigs)
        .build();
  }
}
