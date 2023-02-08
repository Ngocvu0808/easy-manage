package com.example.product.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ServiceConfig {

  private static final Logger log = LoggerFactory.getLogger(ServiceConfig.class);
  private static Properties properties = new Properties();

  public ServiceConfig() {
  }

  public static String getProperty(String key) {
    return properties.getProperty(key);
  }

  static {
    try {
      ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
      Resource resource = context.getResource("classpath:/bootstrap.yml");
      properties.load(resource.getInputStream());
    } catch (FileNotFoundException var2) {
      log.error("FileNotFoundException {}", var2.getLocalizedMessage());
    } catch (IOException var3) {
      log.error("IOException {}", var3.getLocalizedMessage());
    }

  }
}
