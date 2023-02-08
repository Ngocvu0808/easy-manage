package com.example.authservice.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(
    name = "sys_permission"
)
public class SysPermission extends BaseEntity<Integer> implements Serializable {

  private static final long serialVersionUID = 6976935919881542757L;
  @Id
  @GeneratedValue(
      strategy = GenerationType.IDENTITY
  )
  private Integer id;
  private String api;
  @Enumerated(EnumType.STRING)
  private SysPermissionMethod method;
  private String code;
  private String name;
  private String service;
  @Column(
      name = "object_code"
  )
  private String objectCode;
  @Column(
      name = "object_name"
  )
  private String objectName;

  public SysPermission() {
  }

  public Integer getId() {
    return this.id;
  }

  public String getApi() {
    return this.api;
  }

  public SysPermissionMethod getMethod() {
    return this.method;
  }

  public String getCode() {
    return this.code;
  }

  public String getName() {
    return this.name;
  }

  public String getService() {
    return this.service;
  }

  public String getObjectCode() {
    return this.objectCode;
  }

  public String getObjectName() {
    return this.objectName;
  }

  public void setId(final Integer id) {
    this.id = id;
  }

  public void setApi(final String api) {
    this.api = api;
  }

  public void setMethod(final SysPermissionMethod method) {
    this.method = method;
  }

  public void setCode(final String code) {
    this.code = code;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public void setService(final String service) {
    this.service = service;
  }

  public void setObjectCode(final String objectCode) {
    this.objectCode = objectCode;
  }

  public void setObjectName(final String objectName) {
    this.objectName = objectName;
  }

  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    } else if (!(o instanceof SysPermission)) {
      return false;
    } else {
      SysPermission other = (SysPermission) o;
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

        Object this$api = this.getApi();
        Object other$api = other.getApi();
        if (this$api == null) {
          if (other$api != null) {
            return false;
          }
        } else if (!this$api.equals(other$api)) {
          return false;
        }

        Object this$method = this.getMethod();
        Object other$method = other.getMethod();
        if (this$method == null) {
          if (other$method != null) {
            return false;
          }
        } else if (!this$method.equals(other$method)) {
          return false;
        }

        label86:
        {
          Object this$code = this.getCode();
          Object other$code = other.getCode();
          if (this$code == null) {
            if (other$code == null) {
              break label86;
            }
          } else if (this$code.equals(other$code)) {
            break label86;
          }

          return false;
        }

        label79:
        {
          Object this$name = this.getName();
          Object other$name = other.getName();
          if (this$name == null) {
            if (other$name == null) {
              break label79;
            }
          } else if (this$name.equals(other$name)) {
            break label79;
          }

          return false;
        }

        label72:
        {
          Object this$service = this.getService();
          Object other$service = other.getService();
          if (this$service == null) {
            if (other$service == null) {
              break label72;
            }
          } else if (this$service.equals(other$service)) {
            break label72;
          }

          return false;
        }

        Object this$objectCode = this.getObjectCode();
        Object other$objectCode = other.getObjectCode();
        if (this$objectCode == null) {
          if (other$objectCode != null) {
            return false;
          }
        } else if (!this$objectCode.equals(other$objectCode)) {
          return false;
        }

        Object this$objectName = this.getObjectName();
        Object other$objectName = other.getObjectName();
        if (this$objectName == null) {
          if (other$objectName != null) {
            return false;
          }
        } else if (!this$objectName.equals(other$objectName)) {
          return false;
        }

        return true;
      }
    }
  }

  protected boolean canEqual(final Object other) {
    return other instanceof SysPermission;
  }

  public int hashCode() {
    boolean PRIME = true;
    int result = 1;
    Object $id = this.getId();
    result = result * 59 + ($id == null ? 43 : $id.hashCode());
    Object $api = this.getApi();
    result = result * 59 + ($api == null ? 43 : $api.hashCode());
    Object $method = this.getMethod();
    result = result * 59 + ($method == null ? 43 : $method.hashCode());
    Object $code = this.getCode();
    result = result * 59 + ($code == null ? 43 : $code.hashCode());
    Object $name = this.getName();
    result = result * 59 + ($name == null ? 43 : $name.hashCode());
    Object $service = this.getService();
    result = result * 59 + ($service == null ? 43 : $service.hashCode());
    Object $objectCode = this.getObjectCode();
    result = result * 59 + ($objectCode == null ? 43 : $objectCode.hashCode());
    Object $objectName = this.getObjectName();
    result = result * 59 + ($objectName == null ? 43 : $objectName.hashCode());
    return result;
  }

  public String toString() {
    return "SysPermission(id=" + this.getId() + ", api=" + this.getApi() + ", method="
        + this.getMethod() + ", code=" + this.getCode() + ", name=" + this.getName() + ", service="
        + this.getService() + ", objectCode=" + this.getObjectCode() + ", objectName="
        + this.getObjectName() + ")";
  }
}
