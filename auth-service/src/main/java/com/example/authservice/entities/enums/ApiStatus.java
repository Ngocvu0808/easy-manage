package com.example.authservice.entities.enums;

public enum ApiStatus {
  ACTIVE(0), DEACTIVE(1);
  private final Integer value;

  ApiStatus(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }

}
