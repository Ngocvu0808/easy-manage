package com.example.authservice.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author nguyen
 * @created 06/03/2020
 */
public class RoleDtoRequest {

  private String code;
  private String note;
  private String name;
  @JsonProperty("default_role")
  private Boolean defaultRole;
  private List<Integer> permissions;
  @JsonProperty("type")
  private Integer roleType;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Integer> getPermissions() {
    return permissions;
  }

  public void setPermissions(List<Integer> permissions) {
    this.permissions = permissions;
  }

  public Boolean getDefaultRole() {
    return defaultRole;
  }

  public void setDefaultRole(Boolean defaultRole) {
    this.defaultRole = defaultRole;
  }

  public Integer getRoleType() {
    return roleType;
  }

  public void setRoleType(Integer roleType) {
    this.roleType = roleType;
  }
}
