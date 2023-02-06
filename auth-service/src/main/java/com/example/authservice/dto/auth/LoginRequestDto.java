package com.example.authservice.dto.auth;

import lombok.Data;

@Data
public class LoginRequestDto {

  private String userName;
  private String password;

  public LoginRequestDto() {
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
