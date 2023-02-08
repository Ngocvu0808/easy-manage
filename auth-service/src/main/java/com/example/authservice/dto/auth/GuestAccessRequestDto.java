package com.example.authservice.dto.auth;

import java.util.List;

/**
 * @author nguyen
 * @created_date 19/05/2021
 */
public class GuestAccessRequestDto {

  private String sessionId;
  private String service;
  private List<String> permissions;

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public String getService() {
    return service;
  }

  public void setService(String service) {
    this.service = service;
  }

  public List<String> getPermissions() {
    return permissions;
  }

  public void setPermissions(List<String> permissions) {
    this.permissions = permissions;
  }
}
