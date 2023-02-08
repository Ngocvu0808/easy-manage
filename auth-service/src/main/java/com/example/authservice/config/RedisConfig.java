package com.example.authservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration(proxyBeanMethods = true)
public class RedisConfig {


  @Value("${spring.data.redis.host}")
  private String redisHost;

  @Value("${spring.data.redis.port}")
  private int redisPort;

  @Value("${spring.data.redis.database}")
  private int redisDatabase;

  @Value("${spring.data.redis.password}")
  private String redisPassword;

  /**
   * Tạo Standalone Connection tới Redis
   *
   * @return
   */
  @Bean
  public JedisConnectionFactory redisConnectionFactory() {
    RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
    configuration.setHostName(redisHost);
    configuration.setPort(redisPort);
    configuration.setDatabase(redisDatabase);
    configuration.setPassword(RedisPassword.of(redisPassword));
    return new JedisConnectionFactory(configuration);
  }

  /**
   * Tạo ra một RedisTemplate với Key là Object, Value là Object RedisTemplate dể làm việc với
   * Redis
   *
   * @return RedisTemplate
   */
  @Bean
  @Primary
  public RedisTemplate<String, Object> redisTemplate() {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(this.redisConnectionFactory());
    return template;
  }

}
