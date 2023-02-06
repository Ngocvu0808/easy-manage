package com.example.authservice.dto.appservice;

import com.example.authservice.entities.enums.ApiStatus;

/**
 * @author bontk
 * @created_date 03/08/2020
 */
public class ApiClientServiceDto {

  private Long id;
  private String name;
  private String api;
  private String method;
  private String type;
  private String description;
  private ApiStatus status;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getApi() {
    return api;
  }

  public void setApi(String api) {
    this.api = api;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ApiStatus getStatus() {
    return status;
  }

  public void setStatus(ApiStatus status) {
    this.status = status;
  }
}
