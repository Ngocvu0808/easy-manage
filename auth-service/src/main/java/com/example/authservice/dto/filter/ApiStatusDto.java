package com.example.authservice.dto.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author nguyen
 * @created_date 04/08/2020
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiStatusDto {

  private String name;
  private Integer value;

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
}
