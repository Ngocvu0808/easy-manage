package com.example.authservice.entities.enums;

public enum ClientApiStatus {
  ACTIVE(0), DEACTIVE(1);
  private final Integer value;

  ClientApiStatus(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }

}
