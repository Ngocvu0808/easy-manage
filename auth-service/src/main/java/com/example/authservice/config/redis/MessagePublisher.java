package com.example.authservice.config.redis;

/**
 * @author nguyen
 * @created_date 18/11/2021
 */
public interface MessagePublisher {
  void publish(String message);
}
