package com.example.authservice.dto.app;

/**
 * @author bontk
 * @created_date 01/08/2020
 */
public class ClientUserPermissionDto {

  private Integer roleId;

  private String roleName;

  public Integer getRoleId() {
    return roleId;
  }

  public void setRoleId(Integer roleId) {
    this.roleId = roleId;
  }

  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    ClientUserPermissionDto that = (ClientUserPermissionDto) obj;
    return roleId.equals(that.roleId) && roleName.equals(that.roleName);
  }
}
