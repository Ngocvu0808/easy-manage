package com.example.authservice.utils;

public class ServiceInfo {

  public ServiceInfo() {
  }

  public static String getId() {
    try {
      String id = ServiceConfig.getProperty("id");
      return id != null && !id.isBlank() && !id.isEmpty() ? id : "00";
    } catch (Exception var1) {
      return "00";
    }
  }

  public static String getName() {
    try {
      String name = ServiceConfig.getProperty("name");
      return name != null && !name.isBlank() && !name.isEmpty() ? name : "NONE";
    } catch (Exception var1) {
      return "NONE";
    }
  }
}