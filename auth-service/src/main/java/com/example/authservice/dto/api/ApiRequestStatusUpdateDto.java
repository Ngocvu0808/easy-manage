package com.example.authservice.dto.api;


import com.example.authservice.entities.enums.ApiRequestStatus;

import javax.validation.constraints.NotNull;

/**
 * @author nguyen
 * @created_date 05/08/2020
 */
public class ApiRequestStatusUpdateDto {

  @NotNull(message = "status not null")
  private ApiRequestStatus status;

  public ApiRequestStatus getStatus() {
    return status;
  }

  public void setStatus(ApiRequestStatus status) {
    this.status = status;
  }
}
