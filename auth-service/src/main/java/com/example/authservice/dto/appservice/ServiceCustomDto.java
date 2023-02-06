package com.example.authservice.dto.appservice;

import java.util.List;

/**
 * @author bontk
 * @created_date 03/08/2020
 */
public class ServiceCustomDto {

  private Integer id;
  private String code;
  private String name;
  private String description;
  private SystemCustomDto system;
  private List<ExternalApiCustomDto> apis;

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
}
