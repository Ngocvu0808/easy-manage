package com.example.authservice.dto.role;

/**
 * @author nguyen
 * @create_date 02/09/2022
 */
public class RoleTypeFilterDto {

  private String name;
  private String code;
  private String description;
  private Integer id;

  public RoleTypeFilterDto() {
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getId() {
    return this.id;
  }

  public void setId(Integer id) {
    this.id = id;
  }
}
