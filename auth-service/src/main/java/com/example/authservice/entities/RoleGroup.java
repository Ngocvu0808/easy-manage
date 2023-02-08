package com.example.authservice.entities;

import com.example.authservice.entities.role.Role;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author nguyen
 * @create_date 02/09/2022
 */
@Entity
@Table(
    name = "base_role_group"
)
public class RoleGroup extends BaseEntity<Integer> implements Serializable {

  private static final long serialVersionUID = -7395331729202046669L;
  @Id
  @GeneratedValue(
      strategy = GenerationType.IDENTITY
  )
  private Integer id;
  @Column(
      name = "role_id",
      insertable = false,
      updatable = false
  )
  private Integer roleId;
  @Column(
      name = "group_id",
      insertable = false,
      updatable = false
  )
  private Integer groupId;
  @JoinColumn(
      name = "role_id"
  )
  @ManyToOne(
      fetch = FetchType.LAZY
  )
  private Role role;
  @JoinColumn(
      name = "group_id"
  )
  @ManyToOne(
      fetch = FetchType.LAZY
  )
  private Group group;

  public RoleGroup() {
  }

  public Integer getId() {
    return this.id;
  }

  public Integer getRoleId() {
    return this.roleId;
  }

  public Integer getGroupId() {
    return this.groupId;
  }

  public Role getRole() {
    return this.role;
  }

  public Group getGroup() {
    return this.group;
  }

  public void setId(final Integer id) {
    this.id = id;
  }

  public void setRoleId(final Integer roleId) {
    this.roleId = roleId;
  }

  public void setGroupId(final Integer groupId) {
    this.groupId = groupId;
  }

  public void setRole(final Role role) {
    this.role = role;
  }

  public void setGroup(final Group group) {
    this.group = group;
  }

  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    } else if (!(o instanceof RoleGroup)) {
      return false;
    } else {
      RoleGroup other = (RoleGroup) o;
      if (!other.canEqual(this)) {
        return false;
      } else {
        label71:
        {
          Object this$id = this.getId();
          Object other$id = other.getId();
          if (this$id == null) {
            if (other$id == null) {
              break label71;
            }
          } else if (this$id.equals(other$id)) {
            break label71;
          }

          return false;
        }

        Object this$roleId = this.getRoleId();
        Object other$roleId = other.getRoleId();
        if (this$roleId == null) {
          if (other$roleId != null) {
            return false;
          }
        } else if (!this$roleId.equals(other$roleId)) {
          return false;
        }

        label57:
        {
          Object this$groupId = this.getGroupId();
          Object other$groupId = other.getGroupId();
          if (this$groupId == null) {
            if (other$groupId == null) {
              break label57;
            }
          } else if (this$groupId.equals(other$groupId)) {
            break label57;
          }

          return false;
        }

        Role this$role = this.getRole();
        Object other$role = other.getRole();
        if (this$role == null) {
          if (other$role != null) {
            return false;
          }
        } else if (!this$role.equals(other$role)) {
          return false;
        }

        Object this$group = this.getGroup();
        Object other$group = other.getGroup();
        if (this$group == null) {
          if (other$group == null) {
            return true;
          }
        } else if (this$group.equals(other$group)) {
          return true;
        }

        return false;
      }
    }
  }

  protected boolean canEqual(final Object other) {
    return other instanceof RoleGroup;
  }

  public int hashCode() {
    boolean PRIME = true;
    int result = 1;
    Object $id = this.getId();
    result = result * 59 + ($id == null ? 43 : $id.hashCode());
    Object $roleId = this.getRoleId();
    result = result * 59 + ($roleId == null ? 43 : $roleId.hashCode());
    Object $groupId = this.getGroupId();
    result = result * 59 + ($groupId == null ? 43 : $groupId.hashCode());
    Object $role = this.getRole();
    result = result * 59 + ($role == null ? 43 : $role.hashCode());
    Object $group = this.getGroup();
    result = result * 59 + ($group == null ? 43 : $group.hashCode());
    return result;
  }

  public String toString() {
    return "RoleGroup(id=" + this.getId() + ", roleId=" + this.getRoleId() + ", groupId="
        + this.getGroupId() + ", role=" + this.getRole() + ", group=" + this.getGroup() + ")";
  }
}
