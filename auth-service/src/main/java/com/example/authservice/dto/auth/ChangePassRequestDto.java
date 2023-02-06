package com.example.authservice.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author bontk
 * @created_date 15/07/2020
 */
@Data
public class ChangePassRequestDto {

  private String password;
  @JsonProperty("new_pass")
  private String newPass;
  @JsonProperty("new_pass_cf")
  private String newPassConfirm;

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getNewPass() {
    return newPass;
  }

  public void setNewPass(String newPass) {
    this.newPass = newPass;
  }

  public String getNewPassConfirm() {
    return newPassConfirm;
  }

  public void setNewPassConfirm(String newPassConfirm) {
    this.newPassConfirm = newPassConfirm;
  }
}
