package com.example.authservice.entities.enums;

public enum ServiceStatus {
  ACTIVE(0), DEACTIVE(1);
  private final Integer value;

  ServiceStatus(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }
}
