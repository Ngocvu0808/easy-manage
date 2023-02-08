//package com.example.product.utils.cache.impl;
//
//
//import com.example.product.utils.cache.CacheRedisService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.ScanOptions;
//import org.springframework.data.redis.serializer.RedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//import org.springframework.stereotype.Service;
//
//import java.util.*;
//import java.util.concurrent.TimeUnit;
//
///**
// * @author nguyen
// * @create_date 02/09/2022
// */
//@Service
//public class CacheRedisServiceImpl implements CacheRedisService {
//
//  private final RedisTemplate<String, Object> template;
//
//  @Autowired
//  public CacheRedisServiceImpl(RedisTemplate<String, Object> template) {
//    RedisSerializer<?> redisSerializer = new StringRedisSerializer();
//    template.setKeySerializer(redisSerializer);
//    template.setValueSerializer(redisSerializer);
//    template.setHashKeySerializer(redisSerializer);
//    template.setHashValueSerializer(redisSerializer);
//    this.template = template;
//  }
//
//  public Boolean exists(String key) {
//    return this.template.hasKey(key);
//  }
//
//  public Boolean hasKeyPattern(String pattern) {
//    Set<String> keys = this.template.keys(pattern);
//    return keys != null && !keys.isEmpty();
//  }
//
//  public Set<Object> keys(String pattern) {
//    return Collections.singleton(this.template.keys(pattern));
//  }
//
//  public void remove(String key) {
//    this.template.delete(key);
//  }
//
//  public void removePattern(String pattern) {
//    Set<String> keys = this.template.keys(pattern);
//    if (keys != null && !keys.isEmpty()) {
//      this.template.delete(keys);
//    }
//
//  }
//
//  public void setExpirePattern(String pattern, long duration, TimeUnit timeUnit) {
//    Set<String> keys = this.template.keys(pattern);
//    if (keys != null && !keys.isEmpty()) {
//      this.template.delete(keys);
//      Iterator var6 = keys.iterator();
//
//      while (var6.hasNext()) {
//        Object key = var6.next();
//        this.template.expire((String) key, duration, timeUnit);
//      }
//    }
//
//  }
//
//  public void setValue(String key, String value) {
//    this.template.opsForValue().set(key, value);
//  }
//
//  public void setValue(String key, String value, Long expiryTime) {
//    this.template.opsForValue().set(key, value);
//    this.template.expire(key, expiryTime, TimeUnit.SECONDS);
//  }
//
//  public Object getValue(String key) {
//    return this.template.opsForValue().get(key);
//  }
//
//  public void incrLong(String key, Long value) {
//    this.template.opsForValue().increment(key, value);
//  }
//
//  public void incrDouble(String key, Double value) {
//    this.template.opsForValue().increment(key, value);
//  }
//
//  public void setList(String key, Collection<Object> list) {
//    this.template.opsForList().rightPushAll(key, list);
//  }
//
//  public void indexOfList(String key, Long index) {
//    this.template.opsForList().index(key, index);
//  }
//
//  public Long leftPut(String key, String item) {
//    return this.template.opsForList().leftPush(key, item);
//  }
//
//  public Long rightPut(String key, String item) {
//    return this.template.opsForList().rightPush(key, item);
//  }
//
//  public Object leftPop(String key) {
//    return this.template.opsForList().leftPop(key);
//  }
//
//  public Object rightPop(String key) {
//    return this.template.opsForList().rightPop(key);
//  }
//
//  public Long lLen(String key) {
//    return this.template.opsForList().size(key);
//  }
//
//  public Boolean hExists(String key, String field) {
//    return this.template.opsForHash().hasKey(key, field);
//  }
//
//  public Object hGet(String key, String field) {
//    return this.template.opsForHash().get(key, field);
//  }
//
//  public void hSet(String key, String field, Object value) {
//    this.template.opsForHash().put(key, field, value);
//  }
//
//  public void hSetAll(String key, Map<String, Object> data) {
//    this.template.opsForHash().putAll(key, data);
//  }
//
//  public Set<Object> hKeys(String key) {
//    return this.template.opsForHash().keys(key);
//  }
//
//  public Map<Object, Object> hEntries(String key) {
//    return this.template.opsForHash().entries(key);
//  }
//
//  public void hDel(String key, String field) {
//    this.template.opsForHash().delete(key, new Object[]{field});
//  }
//
//  public List<Object> hValues(String key) {
//    this.template.opsForHash().scan(key, ScanOptions.scanOptions().build());
//    return this.template.opsForHash().values(key);
//  }
//
//  public void setExpireTime(String key, long duration, TimeUnit timeUnit) {
//    this.template.expire(key, duration, timeUnit);
//  }
//
//  public Long getExpireTime(String key) {
//    return this.template.getExpire(key);
//  }
//
//  public void updateValue(String key, String value) {
//    this.setValue(key, value);
//    this.setExpireTime(key, this.getExpireTime(key), TimeUnit.SECONDS);
//  }
//}
