package com.example.authservice.dto.app;

import com.example.authservice.entities.enums.ClientStatus;
import lombok.Data;

/**
 * @author nguyen
 * @created_date 06/07/2020
 */
@Data
public class ChangeClientStatusRequestDto {

  private ClientStatus status;

  public ClientStatus getStatus() {
    return status;
  }

  public void setStatus(ClientStatus status) {
    this.status = status;
  }
}
