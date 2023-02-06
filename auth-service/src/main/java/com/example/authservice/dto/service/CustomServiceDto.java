package com.example.authservice.dto.service;

import com.example.authservice.entities.enums.ServiceStatus;

/**
 * @author bontk
 * @created_date 04/08/2020
 */
public class CustomServiceDto {

  private Integer id;
  private String code;
  private String name;
  private String description;
  private ServiceStatus status;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ServiceStatus getStatus() {
    return status;
  }

  public void setStatus(ServiceStatus status) {
    this.status = status;
  }
}
