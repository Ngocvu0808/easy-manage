package com.example.product.service.impl.kafka;

import com.example.product.service.iface.kafka.ConsumerService;
import com.example.product.service.iface.ms.BusinessService;
import org.slf4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;


@Service
public class ConsumerServiceImpl implements ConsumerService<String> {

  private static final Logger logger = org.slf4j.LoggerFactory
      .getLogger(ConsumerServiceImpl.class);

  private final BusinessService businessService;

  public ConsumerServiceImpl(BusinessService businessService) {
    this.businessService = businessService;
  }

  @Override
  @KafkaListener(topics = "${kafka.topic.check-balance}")
  public void checkBalance(String data, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {

  }

  @Override
  @KafkaListener(topics = "${kafka.topic.fund}")
  public void fund(String data, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
    businessService.fund(data);
  }
}