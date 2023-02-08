package com.example.authservice.dto;


public class SysPermissionDto {

  private Integer id;
  private String api;
  private SysPermissionMethod method;
  private String code;
  private String name;
  private String service;

  public SysPermissionDto() {
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

  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    } else if (!(o instanceof SysPermissionDto)) {
      return false;
    } else {
      SysPermissionDto other = (SysPermissionDto) o;
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

        label62:
        {
          Object this$code = this.getCode();
          Object other$code = other.getCode();
          if (this$code == null) {
            if (other$code == null) {
              break label62;
            }
          } else if (this$code.equals(other$code)) {
            break label62;
          }

          return false;
        }

        label55:
        {
          Object this$name = this.getName();
          Object other$name = other.getName();
          if (this$name == null) {
            if (other$name == null) {
              break label55;
            }
          } else if (this$name.equals(other$name)) {
            break label55;
          }

          return false;
        }

        Object this$service = this.getService();
        Object other$service = other.getService();
        if (this$service == null) {
          if (other$service != null) {
            return false;
          }
        } else if (!this$service.equals(other$service)) {
          return false;
        }

        return true;
      }
    }
  }

  protected boolean canEqual(final Object other) {
    return other instanceof SysPermissionDto;
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
    return result;
  }

  public String toString() {
    return "SysPermissionDto(id=" + this.getId() + ", api=" + this.getApi() + ", method="
        + this.getMethod() + ", code=" + this.getCode() + ", name=" + this.getName() + ", service="
        + this.getService() + ")";
  }
}