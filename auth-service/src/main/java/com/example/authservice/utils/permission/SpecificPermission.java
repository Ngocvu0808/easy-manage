package com.example.authservice.utils.permission;

import java.util.List;

/**
 * @author nguyen
 * @create_date 02/09/2022
 */
public class SpecificPermission {

  private Integer id;
  private List<String> permissions;

  public SpecificPermission() {
  }

  public Integer getId() {
    return this.id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public List<String> getPermissions() {
    return this.permissions;
  }

  public void setPermissions(List<String> permissions) {
    this.permissions = permissions;
  }
}
