package com.example.authservice.entities.enums;

public enum ClientAuthType {
  OAUTH(0, "OAuth2"), API_KEY(1, "API Key");
  private final Integer value;
  private final String description;


  ClientAuthType(Integer value, String description) {
    this.value = value;
    this.description = description;
  }

  public Integer getValue() {
    return value;
  }

  public String getDescription() {
    return description;
  }
}
