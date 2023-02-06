package com.example.authservice.utils.cache;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author nguyen
 * @create_date 02/09/2022
 */
public interface CacheRedisService {
  Boolean exists(String key);

  Boolean hasKeyPattern(String pattern);

  Set<Object> keys(String pattern);

  void remove(String key);

  void removePattern(String pattern);

  void setExpirePattern(String pattern, long duration, TimeUnit timeUnit);

  void setValue(String key, String value);

  void setValue(String key, String value, Long expiryTime);

  Object getValue(String key);

  void incrLong(String key, Long value);

  void incrDouble(String key, Double value);

  void setList(String key, Collection<Object> list);

  void indexOfList(String key, Long index);

  Long leftPut(String key, String item);

  Long rightPut(String key, String item);

  Object leftPop(String key);

  Object rightPop(String key);

  Long lLen(String key);

  Boolean hExists(String key, String field);

  Object hGet(String key, String field);

  void hSet(String key, String field, Object value);

  void hSetAll(String key, Map<String, Object> data);

  Set<Object> hKeys(String key);

  Map<Object, Object> hEntries(String key);

  void hDel(String key, String field);

  List<Object> hValues(String key);

  void setExpireTime(String key, long duration, TimeUnit timeUnit);

  Long getExpireTime(String key);

  void updateValue(String key, String value);
}
