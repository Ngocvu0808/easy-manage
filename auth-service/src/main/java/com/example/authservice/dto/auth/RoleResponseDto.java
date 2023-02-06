package com.example.authservice.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class RoleResponseDto {

  private Integer id;
  private String code;
  private String name;
  private Boolean defaultRole;
  private List<RolePermissionDto> permissions;
  @JsonProperty("type")
  private Integer roleType;

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


  public List<RolePermissionDto> getPermissions() {
    return permissions;
  }

  public void setPermissions(List<RolePermissionDto> permissions) {
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
