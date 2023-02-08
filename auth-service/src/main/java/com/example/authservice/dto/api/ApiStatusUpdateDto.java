package com.example.authservice.dto.api;


import com.example.authservice.entities.enums.ApiStatus;

import javax.validation.constraints.NotNull;

/**
 * @author nguyen
 * @created_date 05/08/2020
 */
public class ApiStatusUpdateDto {

  @NotNull(message = "status not null")
  private ApiStatus status;

  public ApiStatus getStatus() {
    return status;
  }

  public void setStatus(ApiStatus status) {
    this.status = status;
  }
}
