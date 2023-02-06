package com.example.authservice.dto.api;

import com.example.authservice.entities.enums.ApiType;
import com.example.authservice.entities.enums.HttpMethod;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author bontk
 * @created_date 05/08/2020
 */
@Data
public class ApiAddDto {

  @NotNull(message = "Name not null")
  @Size(max = 255, message = "Name longer than 255 characters")
  private String name;

  @NotNull(message = "Code not null")
  @Size(max = 100, message = "Code longer than 100 characters")
  @Pattern(regexp = "^[A-Za-z0-9_]+$", message = "code invalid")
  private String code;

  @NotNull(message = "Api not null")
  @Size(max = 255, message = "Api longer than 255 characters")
  private String api;

  @NotNull(message = "Method not null")
  private HttpMethod method;

  private ApiType type;

  @JsonProperty("service_id")
  @NotNull(message = "Service not null")
  private Integer serviceId;

  @Size(max = 1000, message = "Description longer than 1000 characters")
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

  public String getApi() {
    return api;
  }

  public void setApi(String api) {
    this.api = api;
  }

  public HttpMethod getMethod() {
    return method;
  }

  public void setMethod(HttpMethod method) {
    this.method = method;
  }

  public ApiType getType() {
    return type;
  }

  public void setType(ApiType type) {
    this.type = type;
  }

  public Integer getServiceId() {
    return serviceId;
  }

  public void setServiceId(Integer serviceId) {
    this.serviceId = serviceId;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
