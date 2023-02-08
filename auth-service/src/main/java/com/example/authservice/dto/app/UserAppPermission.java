package com.example.authservice.dto.app;

import lombok.Data;

import java.util.List;

/**
 * @author nguyen
 * @created_date 08/08/2020
 */
@Data
public class UserAppPermission {

  private Integer id;
  private List<String> permissions;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public List<String> getPermissions() {
    return permissions;
  }

  public void setPermissions(List<String> permissions) {
    this.permissions = permissions;
  }
}
