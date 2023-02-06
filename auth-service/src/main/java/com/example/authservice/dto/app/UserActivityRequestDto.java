package com.example.authservice.dto.app;

/**
 * @author nguyen
 * @create_date 09/11/2021
 */
public class UserActivityRequestDto {

  private int userId;
  private String sessionId;
  private String token;
  private String activity;

  public UserActivityRequestDto() {
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getActivity() {
    return activity;
  }

  public void setActivity(String activity) {
    this.activity = activity;
  }

  public UserActivityRequestDto(int userId, String sessionId, String token,
      String activity) {
    this.userId = userId;
    this.sessionId = sessionId;
    this.token = token;
    this.activity = activity;
  }
}
