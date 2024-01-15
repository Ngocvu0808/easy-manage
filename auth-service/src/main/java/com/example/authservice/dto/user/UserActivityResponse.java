package com.example.authservice.dto.user;


public class UserActivityResponse {
  private String IPAddress;
  private String activity;
  private String time;
  private String sessionId;
  private String userName;
  private int userId;

  public String getIPAddress() {
    return IPAddress;
  }

  public void setIPAddress(String IPAddress) {
    this.IPAddress = IPAddress;
  }

  public String getActivity() {
    return activity;
  }

  public void setActivity(String activity) {
    this.activity = activity;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }
}
