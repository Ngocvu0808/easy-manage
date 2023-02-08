package com.example.authservice.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author nguyen
 * @created_date 22/04/2021
 */
public class RoleTypeDto {

  private Integer id;

  private String code;

  private String name;

  private String description;

  @JsonProperty("is_default")
  private Boolean isDefault;

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

  public Boolean getIsDefault() {
    return isDefault;
  }

  public void setIsDefault(Boolean aDefault) {
    isDefault = aDefault;
  }
}
