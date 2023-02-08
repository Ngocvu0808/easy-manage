package com.example.product.service.iface.kafka;

public interface ProducerService<T> {

  Boolean sendMessage(T t, String topic);
}
