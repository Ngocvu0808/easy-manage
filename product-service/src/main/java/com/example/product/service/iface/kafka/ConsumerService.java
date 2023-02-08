package com.example.product.service.iface.kafka;

import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;

public interface ConsumerService<T> {

  void checkBalance(T t, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic);

  void fund(T t, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic);
}
