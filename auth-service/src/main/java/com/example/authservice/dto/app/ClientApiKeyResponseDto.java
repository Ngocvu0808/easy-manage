package com.example.authservice.dto.app;


import com.example.authservice.entities.enums.ClientApiKeyStatus;

import java.util.Date;

public class ClientApiKeyResponseDto {

  private Integer id;

  private String apiKey;

  private ClientApiKeyStatus status;

  private String creatorName;

  private Date createdTime;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getApiKey() {
    return apiKey;
  }

  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

  public ClientApiKeyStatus getStatus() {
    return status;
  }

  public void setStatus(ClientApiKeyStatus status) {
    this.status = status;
  }

  public String getCreatorName() {
    return creatorName;
  }

  public void setCreatorName(String creatorName) {
    this.creatorName = creatorName;
  }

  public Date getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(Date createdTime) {
    this.createdTime = createdTime;
  }
}
