package com.example.authservice.dto.appservice;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author bontk
 * @created_date 03/08/2020
 */
@Data
public class ClientServiceAddDto {

  @JsonProperty("service_id")
  private Integer serviceId;

  public Integer getServiceId() {
    return serviceId;
  }

  public void setServiceId(Integer serviceId) {
    this.serviceId = serviceId;
  }
}
