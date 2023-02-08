package com.example.authservice.entities;


import com.example.authservice.entities.user.User;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author nguyen
 * @create_date 02/09/2022
 */
@Entity
@Table(
    name = "base_group_user"
)
public class GroupUser extends BaseEntity<Integer> implements Serializable {

  private static final long serialVersionUID = -4019458272016483273L;
  @Id
  @GeneratedValue(
      strategy = GenerationType.IDENTITY
  )
  private Integer id;
  @Column(
      name = "group_id",
      updatable = false,
      insertable = false
  )
  private Integer groupId;
  @Column(
      name = "user_id",
      updatable = false,
      insertable = false
  )
  private Integer userId;
  @ManyToOne(
      fetch = FetchType.LAZY
  )
  @JoinColumn(
      name = "group_id"
  )
  private Group group;
  @ManyToOne(
      fetch = FetchType.LAZY
  )
  @JoinColumn(
      name = "user_id"
  )
  private User user;

  public GroupUser() {
  }

  public Integer getId() {
    return this.id;
  }

  public Integer getGroupId() {
    return this.groupId;
  }

  public Integer getUserId() {
    return this.userId;
  }

  public Group getGroup() {
    return this.group;
  }

  public User getUser() {
    return this.user;
  }

  public void setId(final Integer id) {
    this.id = id;
  }

  public void setGroupId(final Integer groupId) {
    this.groupId = groupId;
  }

  public void setUserId(final Integer userId) {
    this.userId = userId;
  }

  public void setGroup(final Group group) {
    this.group = group;
  }

  public void setUser(final User user) {
    this.user = user;
  }

  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    } else if (!(o instanceof GroupUser)) {
      return false;
    } else {
      GroupUser other = (GroupUser) o;
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

        Object this$groupId = this.getGroupId();
        Object other$groupId = other.getGroupId();
        if (this$groupId == null) {
          if (other$groupId != null) {
            return false;
          }
        } else if (!this$groupId.equals(other$groupId)) {
          return false;
        }

        label57:
        {
          Object this$userId = this.getUserId();
          Object other$userId = other.getUserId();
          if (this$userId == null) {
            if (other$userId == null) {
              break label57;
            }
          } else if (this$userId.equals(other$userId)) {
            break label57;
          }

          return false;
        }

        Object this$group = this.getGroup();
        Object other$group = other.getGroup();
        if (this$group == null) {
          if (other$group != null) {
            return false;
          }
        } else if (!this$group.equals(other$group)) {
          return false;
        }

        Object this$user = this.getUser();
        Object other$user = other.getUser();
        if (this$user == null) {
          if (other$user == null) {
            return true;
          }
        } else if (this$user.equals(other$user)) {
          return true;
        }

        return false;
      }
    }
  }

  protected boolean canEqual(final Object other) {
    return other instanceof GroupUser;
  }

  public int hashCode() {
    boolean PRIME = true;
    int result = 1;
    Object $id = this.getId();
    result = result * 59 + ($id == null ? 43 : $id.hashCode());
    Object $groupId = this.getGroupId();
    result = result * 59 + ($groupId == null ? 43 : $groupId.hashCode());
    Object $userId = this.getUserId();
    result = result * 59 + ($userId == null ? 43 : $userId.hashCode());
    Object $group = this.getGroup();
    result = result * 59 + ($group == null ? 43 : $group.hashCode());
    Object $user = this.getUser();
    result = result * 59 + ($user == null ? 43 : $user.hashCode());
    return result;
  }

  public String toString() {
    return "GroupUser(id=" + this.getId() + ", groupId=" + this.getGroupId() + ", userId="
        + this.getUserId() + ", group=" + this.getGroup() + ", user=" + this.getUser() + ")";
  }
}
