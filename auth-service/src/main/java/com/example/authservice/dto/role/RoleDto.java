package com.example.authservice.dto.role;

import java.util.Date;

/**
 * @author nguyen
 * @create_date 02/09/2022
 */
public class RoleDto {
  private Integer id;
  private String code;
  private String note;
  private String name;
  private String creatorName;
  private Boolean defaultRole;
  private Date createdTime;

  public RoleDto() {
  }

  public Integer getId() {
    return this.id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getCode() {
    return this.code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getNote() {
    return this.note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public String getCreatorName() {
    return this.creatorName;
  }

  public void setCreatorName(String creatorName) {
    this.creatorName = creatorName;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Boolean getDefaultRole() {
    return this.defaultRole;
  }

  public void setDefaultRole(Boolean defaultRole) {
    this.defaultRole = defaultRole;
  }

  public Date getCreatedTime() {
    return this.createdTime;
  }

  public void setCreatedTime(Date createdTime) {
    this.createdTime = createdTime;
  }
}
