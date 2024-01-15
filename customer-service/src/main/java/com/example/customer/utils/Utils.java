package com.example.customer.utils;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Utils {

  public static String bytesToHex(byte[] hashInBytes) {

    StringBuilder sb = new StringBuilder();
    for (byte b : hashInBytes) {
      sb.append(String.format("%02x", b));
    }
    return sb.toString();
  }

  public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
    Map<Object, Boolean> map = new ConcurrentHashMap<>();
    return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
  }

  public static Set<Integer> strToIntegerSet(String data) {
    if (data == null || data.isEmpty()) {
      return new HashSet<>();
    }
    return Arrays.stream(data.split(","))
        .map(Integer::parseInt)
        .collect(Collectors.toSet());
  }

  public static Set<String> strToStringSet(String data) {
    if (data == null || data.isEmpty()) {
      return new HashSet<>();
    }
    return Arrays.stream(data.split(","))
        .collect(Collectors.toSet());
  }

  public static String readFileAsString(String fileName) throws Exception {
    StringBuilder builder = new StringBuilder();
    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
    Resource resource = context.getResource("classpath:/".concat(fileName));
    if (resource.isReadable()) {
      InputStream ip = resource.getInputStream();
      BufferedReader reader = new BufferedReader(new InputStreamReader(ip));
      reader.lines().forEach(builder::append);
      reader.close();
    }
    return builder.toString();
  }

  public static String[] getNullPropertyNames (Object source) {
    final BeanWrapper src = new BeanWrapperImpl(source);
    java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

    Set<String> emptyNames = new HashSet<String>();
    for(java.beans.PropertyDescriptor pd : pds) {
//      Object srcValue = src.getPropertyValue(pd.getName());
      if (checkInput(src, pd))
        emptyNames.add(pd.getName());
    }

    String[] result = new String[emptyNames.size()];
    return emptyNames.toArray(result);
  }

  private static boolean checkInput(BeanWrapper src, java.beans.PropertyDescriptor pd) {
    if (pd.getPropertyType() == Long.class) {
      Object srcValue = src.getPropertyValue(pd.getName());
      if (srcValue == null) {
        return true;
      }
      if (Long.parseLong(srcValue.toString()) ==0L)
        return true;
    }
    return src.getPropertyValue(pd.getName()) == null;
  }

  // then use Spring BeanUtils to copy and ignore null using our function
  public static void myCopyProperties(Object src, Object target) {
    BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
  }
}
