package com.example.authservice.dto.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author nguyen
 * @created_date 01/04/2020
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserStatusDto {

  private String name;
  private Integer value;
  private String description;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getValue() {
    return value;
  }

  public void setValue(Integer value) {
    this.value = value;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
