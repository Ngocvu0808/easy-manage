package com.example.authservice.entities.enums;

/**
 * @author nguyen
 * @create_date 09/11/2021
 */
public enum UserActivityType {

  LOGIN("LOGIN"), LOGOUT("LOGOUT");
  private final String value;

  UserActivityType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
