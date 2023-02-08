package com.example.authservice.dto.auth;

/**
 * @author nguyen
 * @created_date 19/05/2021
 */

public class GuestAccessResponseDto {

  private String token;
  private String sessionId;

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }
}
