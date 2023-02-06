package com.example.authservice.dto.auth;

import lombok.Data;

@Data
public class UserProfile {

  private String userId;
  private String email;
  private String firstName;
  private String lastName;
  private long creationTime;
  private long lastAccessTime;
  private long validUntil;
  private Status State;

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void setCreationTime(long creationTime) {
    this.creationTime = creationTime;
  }

  public long getLastAccessTime() {
    return lastAccessTime;
  }

  public void setLastAccessTime(long lastAccessTime) {
    this.lastAccessTime = lastAccessTime;
  }

  public long getValidUntil() {
    return validUntil;
  }

  public void setValidUntil(long validUntil) {
    this.validUntil = validUntil;
  }

  public Status getState() {
    return State;
  }

  public void setState(Status state) {
    State = state;
  }
}
