package com.example.authservice.dto.appservice;

import lombok.Data;

/**
 * @author bontk
 * @created_date 03/08/2020
 */
@Data
public class ClientApiAddDto {

  private Long id;
  private String purpose;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getPurpose() {
    return purpose;
  }

  public void setPurpose(String purpose) {
    this.purpose = purpose;
  }
}
