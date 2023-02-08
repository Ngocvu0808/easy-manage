package com.example.product.service.impl.kafka;

import com.example.product.service.iface.kafka.ProducerService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducerServiceImpl implements ProducerService<String> {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(ProducerServiceImpl.class);

  private final KafkaTemplate<String, Object> kafkaTemplate;

  public ProducerServiceImpl(KafkaTemplate<String, Object> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public Boolean sendMessage(String dto, String topic) {
    try {
      logger.info("Topic: {} with data: {}", topic, dto);
      kafkaTemplate.send(topic, dto);
      return true;
    } catch (Exception e) {
      logger.error("error detail: {}", e.getMessage());
      return false;
    }
  }
}
