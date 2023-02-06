package com.example.authservice.dto.service;


import com.example.authservice.entities.enums.ServiceStatus;

public class ServiceRequestUpdateStatusDto {

  private Integer id;

  private ServiceStatus status;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public ServiceStatus getStatus() {
    return status;
  }

  public void setStatus(ServiceStatus status) {
    this.status = status;
  }
}
