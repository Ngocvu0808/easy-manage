package com.example.authservice.dto.app;

import com.example.authservice.entities.UserStatus;

import java.util.List;

public class UserClientCustomDto {

  private Integer id;
  private String username;
  private String name;
  private String email;
  private UserStatus status;
  private List<ClientUserPermissionDto> permissions;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public UserStatus getStatus() {
    return status;
  }

  public void setStatus(UserStatus status) {
    this.status = status;
  }

  public List<ClientUserPermissionDto> getPermissions() {
    return permissions;
  }

  public void setPermissions(List<ClientUserPermissionDto> permissions) {
    this.permissions = permissions;
  }
}
