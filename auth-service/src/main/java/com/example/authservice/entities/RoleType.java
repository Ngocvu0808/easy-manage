package com.example.authservice.entities;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

/**
 * @author nguyen
 * @create_date 02/09/2022
 */
@Entity
@Table(
    name = "base_role_type"
)
public class RoleType {
  @Id
  @GeneratedValue(
      strategy = GenerationType.IDENTITY
  )
  private Integer id;
  private String code;
  private String name;
  private String description;
  @Column(
      name = "is_default",
      nullable = false,
      columnDefinition = "boolean not null default false"
  )
  private Boolean isDefault = false;
  @CreatedDate
  @Temporal(TemporalType.TIMESTAMP)
  @Column(
      name = "created_time",
      updatable = false
  )
  protected Date createdTime;
  @Column(
      name = "is_deleted",
      nullable = false,
      columnDefinition = "boolean not null default false"
  )
  protected Boolean isDeleted = false;

  public RoleType() {
  }

  public Integer getId() {
    return this.id;
  }

  public String getCode() {
    return this.code;
  }

  public String getName() {
    return this.name;
  }

  public String getDescription() {
    return this.description;
  }

  public Boolean getIsDefault() {
    return this.isDefault;
  }

  public Date getCreatedTime() {
    return this.createdTime;
  }

  public Boolean getIsDeleted() {
    return this.isDeleted;
  }

  public void setId(final Integer id) {
    this.id = id;
  }

  public void setCode(final String code) {
    this.code = code;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  public void setIsDefault(final Boolean isDefault) {
    this.isDefault = isDefault;
  }

  public void setCreatedTime(final Date createdTime) {
    this.createdTime = createdTime;
  }

  public void setIsDeleted(final Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    } else if (!(o instanceof RoleType)) {
      return false;
    } else {
      RoleType other = (RoleType)o;
      if (!other.canEqual(this)) {
        return false;
      } else {
        label95: {
          Object this$id = this.getId();
          Object other$id = other.getId();
          if (this$id == null) {
            if (other$id == null) {
              break label95;
            }
          } else if (this$id.equals(other$id)) {
            break label95;
          }

          return false;
        }

        Object this$code = this.getCode();
        Object other$code = other.getCode();
        if (this$code == null) {
          if (other$code != null) {
            return false;
          }
        } else if (!this$code.equals(other$code)) {
          return false;
        }

        Object this$name = this.getName();
        Object other$name = other.getName();
        if (this$name == null) {
          if (other$name != null) {
            return false;
          }
        } else if (!this$name.equals(other$name)) {
          return false;
        }

        label74: {
          Object this$description = this.getDescription();
          Object other$description = other.getDescription();
          if (this$description == null) {
            if (other$description == null) {
              break label74;
            }
          } else if (this$description.equals(other$description)) {
            break label74;
          }

          return false;
        }

        label67: {
          Object this$isDefault = this.getIsDefault();
          Object other$isDefault = other.getIsDefault();
          if (this$isDefault == null) {
            if (other$isDefault == null) {
              break label67;
            }
          } else if (this$isDefault.equals(other$isDefault)) {
            break label67;
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

        Object this$isDeleted = this.getIsDeleted();
        Object other$isDeleted = other.getIsDeleted();
        if (this$isDeleted == null) {
          if (other$isDeleted != null) {
            return false;
          }
        } else if (!this$isDeleted.equals(other$isDeleted)) {
          return false;
        }

        return true;
      }
    }
  }

  protected boolean canEqual(final Object other) {
    return other instanceof RoleType;
  }

  public int hashCode() {
    boolean PRIME = true;
    int result = 1;
    Object $id = this.getId();
    result = result * 59 + ($id == null ? 43 : $id.hashCode());
    Object $code = this.getCode();
    result = result * 59 + ($code == null ? 43 : $code.hashCode());
    Object $name = this.getName();
    result = result * 59 + ($name == null ? 43 : $name.hashCode());
    Object $description = this.getDescription();
    result = result * 59 + ($description == null ? 43 : $description.hashCode());
    Object $isDefault = this.getIsDefault();
    result = result * 59 + ($isDefault == null ? 43 : $isDefault.hashCode());
    Object $createdTime = this.getCreatedTime();
    result = result * 59 + ($createdTime == null ? 43 : $createdTime.hashCode());
    Object $isDeleted = this.getIsDeleted();
    result = result * 59 + ($isDeleted == null ? 43 : $isDeleted.hashCode());
    return result;
  }

  public String toString() {
    return "RoleType(id=" + this.getId() + ", code=" + this.getCode() + ", name=" + this.getName()
        + ", description=" + this.getDescription() + ", isDefault=" + this.getIsDefault()
        + ", createdTime=" + this.getCreatedTime() + ", isDeleted=" + this.getIsDeleted() + ")";
  }
}
