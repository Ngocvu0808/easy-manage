package com.example.authservice.entities;

/**
 * @author nguyen
 * @create_date 02/09/2022
 */
public enum UserStatus {
  ACTIVE(0, "Active"),
  DEACTIVE(1, "Deactive");

  private final Integer value;
  private final String description;

  private UserStatus(Integer value, String description) {
    this.value = value;
    this.description = description;
  }

  public Integer getValue() {
    return this.value;
  }

  public String getDescription() {
    return this.description;
  }
}
