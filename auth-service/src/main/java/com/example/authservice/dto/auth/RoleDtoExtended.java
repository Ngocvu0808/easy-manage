package com.example.authservice.dto.auth;

import com.example.authservice.dto.role.RoleDto;

/**
 * @author bontk
 * @created_date 23/08/2020
 */
public class RoleDtoExtended extends RoleDto {

  private Boolean isSystemRole;

  public Boolean getSystemRole() {
    return isSystemRole;
  }

  public void setSystemRole(Boolean systemRole) {
    isSystemRole = systemRole;
  }
}
