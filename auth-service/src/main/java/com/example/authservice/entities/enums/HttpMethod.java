package com.example.authservice.entities.enums;

public enum HttpMethod {
  GET(0), POST(1), PUT(2), DELETE(3);
  private final Integer value;

  HttpMethod(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }
}
