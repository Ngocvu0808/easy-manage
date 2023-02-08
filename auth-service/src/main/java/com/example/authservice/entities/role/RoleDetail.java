package com.example.authservice.entities.role;

import com.example.authservice.entities.BaseEntity;
import com.example.authservice.entities.SysPermission;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(
    name = "base_role_detail"
)
public class RoleDetail extends BaseEntity<Integer> implements Serializable {

  private static final long serialVersionUID = 2016153074729469960L;
  @Id
  @GeneratedValue(
      strategy = GenerationType.IDENTITY
  )
  private int id;
  @Column(
      name = "role_id",
      updatable = false,
      insertable = false
  )
  private int roleId;
  @Column(
      name = "permission_id",
      updatable = false,
      insertable = false
  )
  private int permissionId;
  @ManyToOne(
      cascade = {CascadeType.MERGE}
  )
  @OnDelete(
      action = OnDeleteAction.NO_ACTION
  )
  @JoinColumn(
      name = "role_id",
      referencedColumnName = "id"
  )
  private Role role;
  @ManyToOne(
      cascade = {CascadeType.MERGE}
  )
  @OnDelete(
      action = OnDeleteAction.NO_ACTION
  )
  @JoinColumn(
      name = "permission_id",
      referencedColumnName = "id"
  )
  private SysPermission permission;

  public RoleDetail() {
  }

  public int getId() {
    return this.id;
  }

  public int getRoleId() {
    return this.roleId;
  }

  public int getPermissionId() {
    return this.permissionId;
  }

  public Role getRole() {
    return this.role;
  }

  public SysPermission getPermission() {
    return this.permission;
  }

  public void setId(final int id) {
    this.id = id;
  }

  public void setRoleId(final int roleId) {
    this.roleId = roleId;
  }

  public void setPermissionId(final int permissionId) {
    this.permissionId = permissionId;
  }

  public void setRole(final Role role) {
    this.role = role;
  }

  public void setPermission(final SysPermission permission) {
    this.permission = permission;
  }

  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    } else if (!(o instanceof RoleDetail)) {
      return false;
    } else {
      RoleDetail other = (RoleDetail) o;
      if (!other.canEqual(this)) {
        return false;
      } else if (this.getId() != other.getId()) {
        return false;
      } else if (this.getRoleId() != other.getRoleId()) {
        return false;
      } else if (this.getPermissionId() != other.getPermissionId()) {
        return false;
      } else {
        Object this$role = this.getRole();
        Object other$role = other.getRole();
        if (this$role == null) {
          if (other$role != null) {
            return false;
          }
        } else if (!this$role.equals(other$role)) {
          return false;
        }

        Object this$permission = this.getPermission();
        Object other$permission = other.getPermission();
        if (this$permission == null) {
          if (other$permission != null) {
            return false;
          }
        } else if (!this$permission.equals(other$permission)) {
          return false;
        }

        return true;
      }
    }
  }

  protected boolean canEqual(final Object other) {
    return other instanceof RoleDetail;
  }

  public int hashCode() {
    boolean PRIME = true;
    int result = 1;
    result = result * 59 + this.getId();
    result = result * 59 + this.getRoleId();
    result = result * 59 + this.getPermissionId();
    Object $role = this.getRole();
    result = result * 59 + ($role == null ? 43 : $role.hashCode());
    Object $permission = this.getPermission();
    result = result * 59 + ($permission == null ? 43 : $permission.hashCode());
    return result;
  }

  public String toString() {
    return "RoleDetail(id=" + this.getId() + ", roleId=" + this.getRoleId() + ", permissionId="
        + this.getPermissionId() + ", role=" + this.getRole() + ", permission="
        + this.getPermission() + ")";
  }
}
