package com.example.customer.utils.permission;

import com.example.product.utils.permission.SpecificPermission;
import java.util.List;

/**
 * @author nguyen
 * @create_date 02/09/2022
 */
public class ObjectPermission {

  private String name;
  private List<SpecificPermission> permissionList;

  public ObjectPermission() {
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<SpecificPermission> getPermissionList() {
    return this.permissionList;
  }

  public void setPermissionList(List<SpecificPermission> permissionList) {
    this.permissionList = permissionList;
  }
}
