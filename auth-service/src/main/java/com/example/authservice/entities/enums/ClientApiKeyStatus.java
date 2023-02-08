package com.example.authservice.entities.enums;

public enum ClientApiKeyStatus {

  ACTIVE(0), EXPIRED(1);
  /**
   * @author nguyen
   * @created_date 03/06/2020
   */

  private final Integer value;

  ClientApiKeyStatus(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }
}
