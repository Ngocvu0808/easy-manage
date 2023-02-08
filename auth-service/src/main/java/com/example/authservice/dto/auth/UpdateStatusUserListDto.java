package com.example.authservice.dto.auth;

import com.example.authservice.entities.UserStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author nguyen
 * @created_date 24/03/2020
 */
public class UpdateStatusUserListDto {

  private List<Integer> ids;
  @JsonProperty("is_blacklist")
  private Boolean isBlacklist;
  private UserStatus status;

  public List<Integer> getIds() {
    return ids;
  }

  public void setIds(List<Integer> ids) {
    this.ids = ids;
  }

  public UserStatus getStatus() {
    return status;
  }

  public void setStatus(UserStatus status) {
    this.status = status;
  }

  public Boolean getIsBlacklist() {
    return isBlacklist;
  }

  public void setIsBlacklist(Boolean isBlacklist) {
    this.isBlacklist = isBlacklist;
  }
}
