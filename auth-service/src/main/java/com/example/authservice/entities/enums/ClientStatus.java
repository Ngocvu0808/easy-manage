package com.example.authservice.entities.enums;

public enum ClientStatus {
  ACTIVE(0), DEACTIVE(1);
  private final Integer value;

  ClientStatus(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }
}
