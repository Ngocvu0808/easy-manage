package com.example.authservice.config.redis;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

/**
 * @author nguyen
 * @created_date 18/11/2021
 */

@Service
public class MessageSubscriber implements MessageListener {

  private final Logger logger = LoggerFactory.getLogger(MessageSubscriber.class);

  public static List<String> messageList = new ArrayList<>();

  @Override
  public void onMessage(Message message, byte[] pattern) {
    messageList.add(message.toString());
  }

}
