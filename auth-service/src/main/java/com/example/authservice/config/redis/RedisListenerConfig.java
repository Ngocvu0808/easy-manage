package com.example.authservice.config.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

/**
 * @author nguyen
 * @created_date 18/11/2021
 */

@Configuration
@EnableRedisRepositories(enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP)
public class RedisListenerConfig {

  private static final Logger logger = LoggerFactory.getLogger(RedisListenerConfig.class);

  @Bean
  StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
    return new StringRedisTemplate(connectionFactory);
  }

  @Bean
  MessageListenerAdapter messageListener() {
    return new MessageListenerAdapter(new MessageSubscriber());
  }

  @Bean
  MessagePublisher redisPublisher() {
    return new MessagePublisherImpl();
  }

  @Bean
  ChannelTopic topic() {
    return new ChannelTopic("messageQueue");
  }

  @Bean
  RedisMessageListenerContainer redisContainer(RedisConnectionFactory connectionFactory) {
    RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.addMessageListener(messageListener(), topic());
    return container;
  }


  @Bean
  RedisMessageListenerContainer keyExpirationListenerContainer(
      RedisConnectionFactory connectionFactory, ExpirationListener expirationListener) {
    RedisMessageListenerContainer listenerContainer = new RedisMessageListenerContainer();
    listenerContainer.setConnectionFactory(connectionFactory);
    listenerContainer.addMessageListener(expirationListener,
        new PatternTopic("__keyevent@*__:expired"));
    listenerContainer.setErrorHandler(e ->
        logger.info("There was an error in redis key expiration listener container {}",
            e.toString()));
    return listenerContainer;
  }
}
