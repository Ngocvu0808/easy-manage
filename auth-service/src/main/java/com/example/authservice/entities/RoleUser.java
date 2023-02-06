package com.example.authservice.entities;


import com.example.authservice.entities.role.Role;
import com.example.authservice.entities.user.User;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author nguyen
 * @create_date 02/09/2022
 */
@Entity
@Table(
    name = "base_role_user"
)
public class RoleUser extends BaseEntity<Integer> implements Serializable {

  private static final long serialVersionUID = 7994640025883608361L;
  @Id
  @GeneratedValue(
      strategy = GenerationType.IDENTITY
  )
  private Integer id;
  @Column(
      name = "user_id",
      insertable = false,
      updatable = false
  )
  private Integer userId;
  @Column(
      name = "role_id",
      insertable = false,
      updatable = false
  )
  private Integer roleId;
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
      name = "user_id",
      referencedColumnName = "id"
  )
  private User user;
  @Column(
      columnDefinition = "TEXT"
  )
  private String note;

  public RoleUser() {
  }

  public Integer getId() {
    return this.id;
  }

  public Integer getUserId() {
    return this.userId;
  }

  public Integer getRoleId() {
    return this.roleId;
  }

  public Role getRole() {
    return this.role;
  }

  public User getUser() {
    return this.user;
  }

  public String getNote() {
    return this.note;
  }

  public void setId(final Integer id) {
    this.id = id;
  }

  public void setUserId(final Integer userId) {
    this.userId = userId;
  }

  public void setRoleId(final Integer roleId) {
    this.roleId = roleId;
  }

  public void setRole(final Role role) {
    this.role = role;
  }

  public void setUser(final User user) {
    this.user = user;
  }

  public void setNote(final String note) {
    this.note = note;
  }

  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    } else if (!(o instanceof RoleUser)) {
      return false;
    } else {
      RoleUser other = (RoleUser) o;
      if (!other.canEqual(this)) {
        return false;
      } else {
        Object this$id = this.getId();
        Object other$id = other.getId();
        if (this$id == null) {
          if (other$id != null) {
            return false;
          }
        } else if (!this$id.equals(other$id)) {
          return false;
        }

        Object this$userId = this.getUserId();
        Object other$userId = other.getUserId();
        if (this$userId == null) {
          if (other$userId != null) {
            return false;
          }
        } else if (!this$userId.equals(other$userId)) {
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

        label62:
        {
          Object this$role = this.getRole();
          Object other$role = other.getRole();
          if (this$role == null) {
            if (other$role == null) {
              break label62;
            }
          } else if (this$role.equals(other$role)) {
            break label62;
          }

          return false;
        }

        label55:
        {
          Object this$user = this.getUser();
          Object other$user = other.getUser();
          if (this$user == null) {
            if (other$user == null) {
              break label55;
            }
          } else if (this$user.equals(other$user)) {
            break label55;
          }

          return false;
        }

        Object this$note = this.getNote();
        Object other$note = other.getNote();
        if (this$note == null) {
          if (other$note != null) {
            return false;
          }
        } else if (!this$note.equals(other$note)) {
          return false;
        }

        return true;
      }
    }
  }

  protected boolean canEqual(final Object other) {
    return other instanceof RoleUser;
  }

  public int hashCode() {
    boolean PRIME = true;
    int result = 1;
    Object $id = this.getId();
    result = result * 59 + ($id == null ? 43 : $id.hashCode());
    Object $userId = this.getUserId();
    result = result * 59 + ($userId == null ? 43 : $userId.hashCode());
    Object $roleId = this.getRoleId();
    result = result * 59 + ($roleId == null ? 43 : $roleId.hashCode());
    Object $role = this.getRole();
    result = result * 59 + ($role == null ? 43 : $role.hashCode());
    Object $user = this.getUser();
    result = result * 59 + ($user == null ? 43 : $user.hashCode());
    Object $note = this.getNote();
    result = result * 59 + ($note == null ? 43 : $note.hashCode());
    return result;
  }

  public String toString() {
    return "RoleUser(id=" + this.getId() + ", userId=" + this.getUserId() + ", roleId=" + this
        .getRoleId() + ", role=" + this.getRole() + ", user=" + this.getUser() + ", note=" + this
        .getNote() + ")";
  }
}