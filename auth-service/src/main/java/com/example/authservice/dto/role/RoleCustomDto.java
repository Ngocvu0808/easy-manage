package com.example.authservice.dto.role;

/**
 * @author nguyen
 * @create_date 02/09/2022
 */
public class RoleCustomDto {

  private String name;
  private Integer id;

  public RoleCustomDto() {
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
