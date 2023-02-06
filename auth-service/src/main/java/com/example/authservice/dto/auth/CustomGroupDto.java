package com.example.authservice.dto.auth;

/**
 * @author bontk
 * @created_date 01/04/2020
 */
public class CustomGroupDto {
  private Integer id;

  private String code;

  private String name;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
