package com.example.authservice.config.redis;

import com.example.authservice.utils.JsonUtils;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

/**
 * @author nguyen
 * @created_date 18/11/2021
 */
@Component
public class ExpirationListener implements MessageListener {

  private static final Logger logger = LoggerFactory.getLogger(ExpirationListener.class);

  private final MessagePublisher messagePublisher;

  public ExpirationListener(MessagePublisher messagePublisher) {
    this.messagePublisher = messagePublisher;
  }

  @Override
  public void onMessage(Message message, byte[] bytes) {
    String key = new String(message.getBody());
    Map<String, Object> data = new HashMap<>();
    data.put("key", key);
    data.put("type", "expired");
    data.put("timestamp", System.currentTimeMillis());
    messagePublisher.publish(JsonUtils.toJson(data));
  }
}
