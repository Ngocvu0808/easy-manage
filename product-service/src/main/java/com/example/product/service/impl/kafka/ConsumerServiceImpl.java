package com.example.product.service.impl.kafka;

import com.example.product.service.iface.kafka.ConsumerService;
import org.slf4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;


@Service
public class ConsumerServiceImpl implements ConsumerService<String> {

  private static final Logger logger = org.slf4j.LoggerFactory
      .getLogger(ConsumerServiceImpl.class);

  @Override
  @KafkaListener(topics = "${kafka.topic.check-balance}")
  public void checkBalance(String data, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {

  }

  @Override
  @KafkaListener(topics = "${kafka.topic.fund}")
  public void fund(String leadId, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {

  }
}