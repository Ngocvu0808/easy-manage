package com.example.authservice.entities.enums;

public enum ApiRequestStatus {
  REQUESTING(0), APPROVED(1), REJECTED(2);
  private final Integer value;

  ApiRequestStatus(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }

}
