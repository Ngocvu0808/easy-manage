package com.example.authservice.dto.app;

import com.example.authservice.entities.enums.RefreshTokenStatus;
import lombok.Data;

/**
 * @author bontk
 * @created_date 08/07/2020
 */
@Data
public class ChangeTokenStatusRequestDto {

  private RefreshTokenStatus status;

  public RefreshTokenStatus getStatus() {
    return status;
  }

  public void setStatus(RefreshTokenStatus status) {
    this.status = status;
  }
}
