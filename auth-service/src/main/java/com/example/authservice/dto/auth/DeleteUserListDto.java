package com.example.authservice.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author bontk
 * @created_date 24/03/2020
 */
public class DeleteUserListDto {

  private List<Integer> ids;

  @JsonProperty("is_blacklist")
  private Boolean isBlacklist;

  public List<Integer> getIds() {
    return ids;
  }

  public void setIds(List<Integer> ids) {
    this.ids = ids;
  }

  public Boolean getIsBlacklist() {
    return isBlacklist;
  }

  public void setIsBlacklist(Boolean isBlacklist) {
    this.isBlacklist = isBlacklist;
  }
}
