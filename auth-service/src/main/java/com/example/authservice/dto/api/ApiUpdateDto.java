package com.example.authservice.dto.api;

import com.example.authservice.entities.enums.ApiType;
import com.example.authservice.entities.enums.HttpMethod;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author nguyen
 * @created_date 05/08/2020
 */
public class ApiUpdateDto {

  @NotNull(message = "Name not null")
  @Size(max = 255, message = "Name longer than 255 characters")
  private String name;
  @NotNull(message = "Code not null")
  @Size(max = 100, message = "Code longer than 255 characters")
  @Pattern(regexp = "^[A-Za-z0-9_]+$", message = "code invalid")
  private String code;
  @NotNull(message = "Method not null")
  private HttpMethod method;
  @NotNull(message = "Api not null")
  @Size(max = 255, message = "Api longer than 255 characters")
  private String api;
  @JsonProperty("service_id")
  @NotNull(message = "Service not null")
  private Integer serviceId;
  private ApiType type;
  @Size(max = 1000, message = "Description longer than 255 characters")
  private String description;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public HttpMethod getMethod() {
    return method;
  }

  public void setMethod(HttpMethod method) {
    this.method = method;
  }

  public String getApi() {
    return api;
  }

  public void setApi(String api) {
    this.api = api;
  }

  public Integer getServiceId() {
    return serviceId;
  }

  public void setServiceId(Integer serviceId) {
    this.serviceId = serviceId;
  }

  public ApiType getType() {
    return type;
  }

  public void setType(ApiType type) {
    this.type = type;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
