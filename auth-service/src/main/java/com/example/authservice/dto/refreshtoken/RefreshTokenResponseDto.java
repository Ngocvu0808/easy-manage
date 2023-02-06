package com.example.authservice.dto.refreshtoken;

import com.example.authservice.entities.enums.RefreshTokenStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

public class RefreshTokenResponseDto {

  private Integer id;

  private String token;

  private Date createdTime;

  private String ip;

  private RefreshTokenStatus status;

  private Boolean approved;

  private Long expireTime;

  private String developer;

  @JsonIgnore
  private Integer apiKeyId;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
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

  public RefreshTokenStatus getStatus() {
    return status;
  }

  public void setStatus(RefreshTokenStatus status) {
    this.status = status;
  }

  public Boolean getApproved() {
    return approved;
  }

  public void setApproved(Boolean approved) {
    this.approved = approved;
  }

  public Long getExpireTime() {
    return expireTime;
  }

  public void setExpireTime(Long expireTime) {
    this.expireTime = expireTime;
  }

  public String getDeveloper() {
    return developer;
  }

  public void setDeveloper(String developer) {
    this.developer = developer;
  }

  public Integer getApiKeyId() {
    return apiKeyId;
  }

  public void setApiKeyId(Integer apiKeyId) {
    this.apiKeyId = apiKeyId;
  }
}
