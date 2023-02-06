package com.example.authservice.dto.auth;


import com.example.authservice.dto.appservice.ExternalApiCustomDto;
import com.example.authservice.dto.appservice.SystemCustomDto;
import com.example.authservice.entities.enums.ServiceStatus;

import java.util.List;

/**
 * @author bontk
 * @created_date 04/08/2020
 */
public class ServiceDto {

  private Integer id;
  private String code;
  private String name;
  private String description;
  private SystemCustomDto system;
  private List<ExternalApiCustomDto> apis;
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

  public SystemCustomDto getSystem() {
    return system;
  }

  public void setSystem(SystemCustomDto system) {
    this.system = system;
  }

  public List<ExternalApiCustomDto> getApis() {
    return apis;
  }

  public void setApis(List<ExternalApiCustomDto> apis) {
    this.apis = apis;
  }

  public ServiceStatus getStatus() {
    return status;
  }

  public void setStatus(ServiceStatus status) {
    this.status = status;
  }
}
