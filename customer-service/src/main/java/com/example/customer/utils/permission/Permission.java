package com.example.customer.utils.permission;

import com.example.product.utils.permission.ObjectPermission;
import java.util.List;

/**
 * @author nguyen
 * @create_date 02/09/2022
 */
public class Permission {

  private List<String> generalPermissions;
  private List<ObjectPermission> specificPermissions;

  public Permission() {
  }

  public List<String> getGeneralPermissions() {
    return this.generalPermissions;
  }

  public void setGeneralPermissions(List<String> generalPermissions) {
    this.generalPermissions = generalPermissions;
  }

  public List<ObjectPermission> getSpecificPermissions() {
    return this.specificPermissions;
  }

  public void setSpecificPermissions(List<ObjectPermission> specificPermissions) {
    this.specificPermissions = specificPermissions;
  }
}
