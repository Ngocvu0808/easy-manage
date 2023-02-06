package com.example.authservice.dto.refreshtoken;

import com.example.authservice.entities.enums.TokenStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class AccessTokenResponseDto {

  @JsonProperty("accessToken")
  private String token;

  @JsonProperty("refreshToken")
  private String refresh;

  private Date createdTime;

  private String ip;

  private Long expireTime;

  private TokenStatus status;

  private Long id;

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getRefresh() {
    return refresh;
  }

  public void setRefresh(String refresh) {
    this.refresh = refresh;
  }

  public Date getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(Date createdTime) {
    this.createdTime = createdTime;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public Long getExpireTime() {
    return expireTime;
  }

  public void setExpireTime(Long expireTime) {
    this.expireTime = expireTime;
  }

  public TokenStatus getStatus() {
    return status;
  }

  public void setStatus(TokenStatus status) {
    this.status = status;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
