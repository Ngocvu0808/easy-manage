package com.example.authservice.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Set;

@Data
public class RegisterRequestDto {

  private String username;
  private String email;
  @JsonProperty("first_name")
  private String firstName;
  @JsonProperty("last_name")
  private String lastName;
  private Set<Integer> roles;
  @JsonProperty("is_internal")
  private Boolean isInternal;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Set<Integer> getRoles() {
    return roles;
  }

  public void setRoles(Set<Integer> roles) {
    this.roles = roles;
  }

  public Boolean getInternal() {
    return isInternal;
  }

  public void setInternal(Boolean internal) {
    isInternal = internal;
  }
}
