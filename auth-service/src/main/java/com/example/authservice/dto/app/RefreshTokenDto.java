package com.example.authservice.dto.app;

import com.example.authservice.entities.enums.RefreshTokenStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author bontk
 * @created_date 09/06/2020
 */
public class RefreshTokenDto {

  private Integer id;

  private String token;

  private Boolean approved;

  private RefreshTokenStatus status;

  private String ip;

  @JsonProperty("created_time")
  private Long createdTime;

  @JsonProperty("expire_time")
  private Long expireTime;

  @JsonProperty("client_name")
  private String clientName;

  @JsonProperty("client_id")
  private String clientId;

  @JsonProperty("client_secret")
  private String clientSecret;

  @JsonProperty("update_time")
  private Long updateTime;

  private Boolean isDeleted;


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

  public Boolean getApproved() {
    return approved;
  }

  public RefreshTokenStatus getStatus() {
    return status;
  }

  public void setStatus(RefreshTokenStatus status) {
    this.status = status;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public void setApproved(Boolean approved) {
    this.approved = approved;
  }

  public Long getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(Long createdTime) {
    this.createdTime = createdTime;
  }

  public Long getExpireTime() {
    return expireTime;
  }

  public void setExpireTime(Long expireTime) {
    this.expireTime = expireTime;
  }

  public String getClientName() {
    return clientName;
  }

  public void setClientName(String clientName) {
    this.clientName = clientName;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getClientSecret() {
    return clientSecret;
  }

  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }

  public Long getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Long updateTime) {
    this.updateTime = updateTime;
  }

  public Boolean getDeleted() {
    return isDeleted;
  }

  public void setDeleted(Boolean deleted) {
    isDeleted = deleted;
  }
}
