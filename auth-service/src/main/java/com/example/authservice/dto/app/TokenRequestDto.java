package com.example.authservice.dto.app;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author nguyen
 * @created_date 03/06/2020
 */
public class TokenRequestDto {

  @JsonProperty("client_id")
  private String clientId;
  @JsonProperty("client_secret")
  private String clientSecret;
  @JsonProperty("api_key")
  private String apiKey;

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

  public String getApiKey() {
    return apiKey;
  }

  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }
}
