package com.example.authservice.dto.app;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author nguyen
 * @created_date 03/06/2020
 */
public class RenewTokenDto {

  @JsonProperty("refresh_token")
  private String refreshToken;

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }
}
