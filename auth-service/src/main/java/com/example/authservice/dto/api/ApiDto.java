package com.example.authservice.dto.api;


import com.example.authservice.dto.appservice.SystemCustomDto;
import com.example.authservice.dto.service.CustomServiceDto;
import com.example.authservice.entities.enums.ApiStatus;
import com.example.authservice.entities.enums.ApiType;
import com.example.authservice.entities.enums.HttpMethod;

/**
 * @author nguyen
 * @created_date 05/08/2020
 */
public class ApiDto {

  private Long id;
  private String name;
  private String code;
  private String api;
  private HttpMethod method;
  private SystemCustomDto system;
  private CustomServiceDto service;
  private ApiType type;
  private ApiStatus status;
  private String description;
  private Long createdTime;
  private Long modifiedTime;
  private String creatorName;
  private String updaterName;

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

  public SystemCustomDto getSystem() {
    return system;
  }

  public void setSystem(SystemCustomDto system) {
    this.system = system;
  }

  public CustomServiceDto getService() {
    return service;
  }

  public void setService(CustomServiceDto service) {
    this.service = service;
  }

  public ApiType getType() {
    return type;
  }

  public void setType(ApiType type) {
    this.type = type;
  }

  public ApiStatus getStatus() {
    return status;
  }

  public void setStatus(ApiStatus status) {
    this.status = status;
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

  public Long getModifiedTime() {
    return modifiedTime;
  }

  public void setModifiedTime(Long modifiedTime) {
    this.modifiedTime = modifiedTime;
  }

  public String getCreatorName() {
    return creatorName;
  }

  public void setCreatorName(String creatorName) {
    this.creatorName = creatorName;
  }

  public String getUpdaterName() {
    return updaterName;
  }

  public void setUpdaterName(String updaterName) {
    this.updaterName = updaterName;
  }
}
