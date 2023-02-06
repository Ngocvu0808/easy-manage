package com.example.authservice.dto.auth;

/**
 * @author nguyen
 * @create_date 09/11/2021
 */
public class UserLoginResponseDto {

  private int userId;
  LoginResponseDto responseDto;

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public LoginResponseDto getResponseDto() {
    return responseDto;
  }

  public void setResponseDto(LoginResponseDto responseDto) {
    this.responseDto = responseDto;
  }
}
