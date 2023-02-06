package com.example.authservice.entities;

import com.example.authservice.entities.user.User;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * @author nguyen
 * @create_date 02/09/2022
 */
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
public class BaseEntity<T> {
  @Column(
      name = "creator_id",
      updatable = false,
      insertable = false
  )
  protected T creatorUserId;
  @Column(
      name = "updater_id",
      updatable = false,
      insertable = false
  )
  protected T updaterUserId;
  @Column(
      name = "deleter_id",
      updatable = false,
      insertable = false
  )
  protected T deleterUserId;
  @Column(
      name = "csid"
  )
  protected T businessId;
  @LastModifiedDate
  @Temporal(TemporalType.TIMESTAMP)
  @Column(
      name = "modified_time"
  )
  protected Date modifiedTime;
  @CreatedDate
  @Temporal(TemporalType.TIMESTAMP)
  @Column(
      name = "created_time"
  )
  protected Date createdTime;
  @Column(
      name = "is_deleted",
      nullable = false,
      columnDefinition = "boolean not null default false"
  )
  protected Boolean isDeleted = false;
  @ManyToOne(
      fetch = FetchType.LAZY
  )
  @JoinColumn(
      name = "creator_id"
  )
  protected User creatorUser;
  @ManyToOne(
      fetch = FetchType.LAZY
  )
  @JoinColumn(
      name = "updater_id"
  )
  protected User updaterUser;
  @ManyToOne(
      fetch = FetchType.LAZY
  )
  @JoinColumn(
      name = "deleter_id"
  )
  protected User deleterUser;

  public BaseEntity() {
  }

  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    } else if (!(o instanceof BaseEntity)) {
      return false;
    } else {
      BaseEntity<?> other = (BaseEntity)o;
      if (!other.canEqual(this)) {
        return false;
      } else {
        Object this$creatorUserId = this.getCreatorUserId();
        Object other$creatorUserId = other.getCreatorUserId();
        if (this$creatorUserId == null) {
          if (other$creatorUserId != null) {
            return false;
          }
        } else if (!this$creatorUserId.equals(other$creatorUserId)) {
          return false;
        }

        Object this$updaterUserId = this.getUpdaterUserId();
        Object other$updaterUserId = other.getUpdaterUserId();
        if (this$updaterUserId == null) {
          if (other$updaterUserId != null) {
            return false;
          }
        } else if (!this$updaterUserId.equals(other$updaterUserId)) {
          return false;
        }

        Object this$deleterUserId = this.getDeleterUserId();
        Object other$deleterUserId = other.getDeleterUserId();
        if (this$deleterUserId == null) {
          if (other$deleterUserId != null) {
            return false;
          }
        } else if (!this$deleterUserId.equals(other$deleterUserId)) {
          return false;
        }

        label110: {
          Object this$businessId = this.getBusinessId();
          Object other$businessId = other.getBusinessId();
          if (this$businessId == null) {
            if (other$businessId == null) {
              break label110;
            }
          } else if (this$businessId.equals(other$businessId)) {
            break label110;
          }

          return false;
        }

        label103: {
          Object this$modifiedTime = this.getModifiedTime();
          Object other$modifiedTime = other.getModifiedTime();
          if (this$modifiedTime == null) {
            if (other$modifiedTime == null) {
              break label103;
            }
          } else if (this$modifiedTime.equals(other$modifiedTime)) {
            break label103;
          }

          return false;
        }

        Object this$createdTime = this.getCreatedTime();
        Object other$createdTime = other.getCreatedTime();
        if (this$createdTime == null) {
          if (other$createdTime != null) {
            return false;
          }
        } else if (!this$createdTime.equals(other$createdTime)) {
          return false;
        }

        label89: {
          Object this$isDeleted = this.getIsDeleted();
          Object other$isDeleted = other.getIsDeleted();
          if (this$isDeleted == null) {
            if (other$isDeleted == null) {
              break label89;
            }
          } else if (this$isDeleted.equals(other$isDeleted)) {
            break label89;
          }

          return false;
        }

        label82: {
          Object this$creatorUser = this.getCreatorUser();
          Object other$creatorUser = other.getCreatorUser();
          if (this$creatorUser == null) {
            if (other$creatorUser == null) {
              break label82;
            }
          } else if (this$creatorUser.equals(other$creatorUser)) {
            break label82;
          }

          return false;
        }

        Object this$updaterUser = this.getUpdaterUser();
        Object other$updaterUser = other.getUpdaterUser();
        if (this$updaterUser == null) {
          if (other$updaterUser != null) {
            return false;
          }
        } else if (!this$updaterUser.equals(other$updaterUser)) {
          return false;
        }

        Object this$deleterUser = this.getDeleterUser();
        Object other$deleterUser = other.getDeleterUser();
        if (this$deleterUser == null) {
          if (other$deleterUser != null) {
            return false;
          }
        } else if (!this$deleterUser.equals(other$deleterUser)) {
          return false;
        }

        return true;
      }
    }
  }

  protected boolean canEqual(final Object other) {
    return other instanceof BaseEntity;
  }

  public int hashCode() {
    boolean PRIME = true;
    int result = 1;
    Object $creatorUserId = this.getCreatorUserId();
    result = result * 59 + ($creatorUserId == null ? 43 : $creatorUserId.hashCode());
    Object $updaterUserId = this.getUpdaterUserId();
    result = result * 59 + ($updaterUserId == null ? 43 : $updaterUserId.hashCode());
    Object $deleterUserId = this.getDeleterUserId();
    result = result * 59 + ($deleterUserId == null ? 43 : $deleterUserId.hashCode());
    Object $businessId = this.getBusinessId();
    result = result * 59 + ($businessId == null ? 43 : $businessId.hashCode());
    Object $modifiedTime = this.getModifiedTime();
    result = result * 59 + ($modifiedTime == null ? 43 : $modifiedTime.hashCode());
    Object $createdTime = this.getCreatedTime();
    result = result * 59 + ($createdTime == null ? 43 : $createdTime.hashCode());
    Object $isDeleted = this.getIsDeleted();
    result = result * 59 + ($isDeleted == null ? 43 : $isDeleted.hashCode());
    Object $creatorUser = this.getCreatorUser();
    result = result * 59 + ($creatorUser == null ? 43 : $creatorUser.hashCode());
    Object $updaterUser = this.getUpdaterUser();
    result = result * 59 + ($updaterUser == null ? 43 : $updaterUser.hashCode());
    Object $deleterUser = this.getDeleterUser();
    result = result * 59 + ($deleterUser == null ? 43 : $deleterUser.hashCode());
    return result;
  }

  public String toString() {
    return "BaseEntity(creatorUserId=" + this.getCreatorUserId() + ", updaterUserId=" + this.getUpdaterUserId() + ", deleterUserId=" + this.getDeleterUserId() + ", businessId=" + this.getBusinessId() + ", modifiedTime=" + this.getModifiedTime() + ", createdTime=" + this.getCreatedTime() + ", isDeleted=" + this.getIsDeleted() + ", creatorUser=" + this.getCreatorUser() + ", updaterUser=" + this.getUpdaterUser() + ", deleterUser=" + this.getDeleterUser() + ")";
  }

  public T getCreatorUserId() {
    return this.creatorUserId;
  }

  public T getUpdaterUserId() {
    return this.updaterUserId;
  }

  public T getDeleterUserId() {
    return this.deleterUserId;
  }

  public T getBusinessId() {
    return this.businessId;
  }

  public Date getModifiedTime() {
    return this.modifiedTime;
  }

  public Date getCreatedTime() {
    return this.createdTime;
  }

  public Boolean getIsDeleted() {
    return this.isDeleted;
  }

  public User getCreatorUser() {
    return this.creatorUser;
  }

  public User getUpdaterUser() {
    return this.updaterUser;
  }

  public User getDeleterUser() {
    return this.deleterUser;
  }

  public void setCreatorUserId(final T creatorUserId) {
    this.creatorUserId = creatorUserId;
  }

  public void setUpdaterUserId(final T updaterUserId) {
    this.updaterUserId = updaterUserId;
  }

  public void setDeleterUserId(final T deleterUserId) {
    this.deleterUserId = deleterUserId;
  }

  public void setBusinessId(final T businessId) {
    this.businessId = businessId;
  }

  public void setModifiedTime(final Date modifiedTime) {
    this.modifiedTime = modifiedTime;
  }

  public void setCreatedTime(final Date createdTime) {
    this.createdTime = createdTime;
  }

  public void setIsDeleted(final Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public void setCreatorUser(final User creatorUser) {
    this.creatorUser = creatorUser;
  }

  public void setUpdaterUser(final User updaterUser) {
    this.updaterUser = updaterUser;
  }

  public void setDeleterUser(final User deleterUser) {
    this.deleterUser = deleterUser;
  }
}
