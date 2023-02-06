package com.example.authservice.dto.auth;

import com.example.authservice.dto.SysPermissionDto;

import java.util.List;

public class RolePermissionDto {

  private String service;
  private String objectCode;
  private String objectName;

  private List<SysPermissionDto> sysPermissions;

  public String getService() {
    return service;
  }

  public void setService(String service) {
    this.service = service;
  }

  public String getObjectCode() {
    return objectCode;
  }

  public void setObjectCode(String objectCode) {
    this.objectCode = objectCode;
  }

  public String getObjectName() {
    return objectName;
  }

  public void setObjectName(String objectName) {
    this.objectName = objectName;
  }

  public List<SysPermissionDto> getSysPermissions() {
    return sysPermissions;
  }

  public void setSysPermissions(List<SysPermissionDto> sysPermissions) {
    this.sysPermissions = sysPermissions;
  }
}
