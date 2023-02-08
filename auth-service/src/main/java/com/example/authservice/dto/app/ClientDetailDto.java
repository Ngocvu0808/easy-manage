package com.example.authservice.dto.app;

import com.example.authservice.entities.enums.ClientAuthType;
import com.example.authservice.entities.enums.ClientStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author nguyen
 * @created_date 02/06/2020
 */
public class ClientDetailDto {

  private Integer id;
  private String name;
  @JsonProperty("client_id")
  private String clientId;
  @JsonProperty("client_secret")
  private String clientSecret;
  private String description;
  @JsonProperty("created_time")
  private Long createdTime;
  private ClientStatus status;
  @JsonProperty("creator_name")
  private String creatorName;
  @JsonProperty("approve_require")
  private Boolean approveRequire;
  @JsonProperty("share_token")
  private Boolean shareToken;
  @JsonProperty("auth_type")
  private ClientAuthType authType;
  @JsonProperty("api_key")
  private String apiKey;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Long getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(Long createdTime) {
    this.createdTime = createdTime;
  }

  public ClientStatus getStatus() {
    return status;
  }

  public void setStatus(ClientStatus status) {
    this.status = status;
  }

  public String getCreatorName() {
    return creatorName;
  }

  public void setCreatorName(String creatorName) {
    this.creatorName = creatorName;
  }

  public Boolean getApproveRequire() {
    return approveRequire;
  }

  public void setApproveRequire(Boolean approveRequire) {
    this.approveRequire = approveRequire;
  }

  public Boolean getShareToken() {
    return shareToken;
  }

  public void setShareToken(Boolean shareToken) {
    this.shareToken = shareToken;
  }

  public ClientAuthType getAuthType() {
    return authType;
  }

  public void setAuthType(ClientAuthType authType) {
    this.authType = authType;
  }

  public String getApiKey() {
    return apiKey;
  }

  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }
}
