package com.example.authservice.entities.role;

import com.example.authservice.entities.BaseEntity;
import com.example.authservice.entities.RoleType;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author nguyen
 * @create_date 02/09/2022
 */
@Entity
@Table(
    name = "base_role"
)
public class Role extends BaseEntity<Integer> implements Serializable {

  private static final long serialVersionUID = -2644725583225637759L;
  @Id
  @GeneratedValue(
      strategy = GenerationType.IDENTITY
  )
  private Integer id;
  private String code;
  private String name;
  @Column(
      columnDefinition = "TEXT"
  )
  private String note;
  @Column(
      name = "type_id",
      insertable = false,
      updatable = false
  )
  private Integer typeId;
  @ManyToOne(
      cascade = {CascadeType.MERGE}
  )
  @JoinColumn(
      name = "type_id",
      referencedColumnName = "id"
  )
  private RoleType type;
  @Column(
      name = "is_default",
      columnDefinition = "boolean default false"
  )
  private Boolean defaultRole = false;
  @Column(
      name = "is_system_role",
      columnDefinition = "boolean default false"
  )
  private Boolean isSystemRole = false;

  public Role() {
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

  public String getNote() {
    return this.note;
  }

  public Integer getTypeId() {
    return this.typeId;
  }

  public RoleType getType() {
    return this.type;
  }

  public Boolean getDefaultRole() {
    return this.defaultRole;
  }

  public Boolean getIsSystemRole() {
    return this.isSystemRole;
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

  public void setNote(final String note) {
    this.note = note;
  }

  public void setTypeId(final Integer typeId) {
    this.typeId = typeId;
  }

  public void setType(final RoleType type) {
    this.type = type;
  }

  public void setDefaultRole(final Boolean defaultRole) {
    this.defaultRole = defaultRole;
  }

  public void setIsSystemRole(final Boolean isSystemRole) {
    this.isSystemRole = isSystemRole;
  }

  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    } else if (!(o instanceof Role)) {
      return false;
    } else {
      Role other = (Role) o;
      if (!other.canEqual(this)) {
        return false;
      } else {
        label107:
        {
          Object this$id = this.getId();
          Object other$id = other.getId();
          if (this$id == null) {
            if (other$id == null) {
              break label107;
            }
          } else if (this$id.equals(other$id)) {
            break label107;
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

        label86:
        {
          Object this$note = this.getNote();
          Object other$note = other.getNote();
          if (this$note == null) {
            if (other$note == null) {
              break label86;
            }
          } else if (this$note.equals(other$note)) {
            break label86;
          }

          return false;
        }

        label79:
        {
          Object this$typeId = this.getTypeId();
          Object other$typeId = other.getTypeId();
          if (this$typeId == null) {
            if (other$typeId == null) {
              break label79;
            }
          } else if (this$typeId.equals(other$typeId)) {
            break label79;
          }

          return false;
        }

        label72:
        {
          Object this$type = this.getType();
          Object other$type = other.getType();
          if (this$type == null) {
            if (other$type == null) {
              break label72;
            }
          } else if (this$type.equals(other$type)) {
            break label72;
          }

          return false;
        }

        Object this$defaultRole = this.getDefaultRole();
        Object other$defaultRole = other.getDefaultRole();
        if (this$defaultRole == null) {
          if (other$defaultRole != null) {
            return false;
          }
        } else if (!this$defaultRole.equals(other$defaultRole)) {
          return false;
        }

        Object this$isSystemRole = this.getIsSystemRole();
        Object other$isSystemRole = other.getIsSystemRole();
        if (this$isSystemRole == null) {
          if (other$isSystemRole != null) {
            return false;
          }
        } else if (!this$isSystemRole.equals(other$isSystemRole)) {
          return false;
        }

        return true;
      }
    }
  }

  protected boolean canEqual(final Object other) {
    return other instanceof Role;
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
    Object $note = this.getNote();
    result = result * 59 + ($note == null ? 43 : $note.hashCode());
    Object $typeId = this.getTypeId();
    result = result * 59 + ($typeId == null ? 43 : $typeId.hashCode());
    Object $type = this.getType();
    result = result * 59 + ($type == null ? 43 : $type.hashCode());
    Object $defaultRole = this.getDefaultRole();
    result = result * 59 + ($defaultRole == null ? 43 : $defaultRole.hashCode());
    Object $isSystemRole = this.getIsSystemRole();
    result = result * 59 + ($isSystemRole == null ? 43 : $isSystemRole.hashCode());
    return result;
  }

  public String toString() {
    return "Role(id=" + this.getId() + ", code=" + this.getCode() + ", name=" + this.getName()
        + ", note=" + this.getNote() + ", typeId=" + this.getTypeId() + ", type=" + this.getType()
        + ", defaultRole=" + this.getDefaultRole() + ", isSystemRole=" + this.getIsSystemRole()
        + ")";
  }
}
