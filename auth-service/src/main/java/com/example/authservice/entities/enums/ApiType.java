package com.example.authservice.entities.enums;

public enum ApiType {
  PUBLIC(0, "Public API"), PRIVATE(1, "Private API");
  private final Integer value;
  private final String description;

  ApiType(Integer value, String description) {
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
