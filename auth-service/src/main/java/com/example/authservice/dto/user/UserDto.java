package com.example.authservice.dto.user;

import com.example.authservice.dto.group.GroupUserCustomDto;
import com.example.authservice.dto.role.RoleCustomDto;
import com.example.authservice.entities.UserStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class UserDto {

  private Integer id;
  private String username;
  private String name;
  private String email;
  private String firstName;
  private String lastName;
  @JsonIgnore
  private String password;
  private UserStatus status;
  private List<RoleCustomDto> roles;
  private List<GroupUserCustomDto> groups;

  public List<GroupUserCustomDto> getGroups() {
    return groups;
  }

  public void setGroups(List<GroupUserCustomDto> groups) {
    this.groups = groups;
  }

  public UserDto() {
  }

  public Integer getId() {
    return this.id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getUsername() {
    return this.username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getLastName() {
    return this.lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getFirstName() {
    return this.firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public UserStatus getStatus() {
    return this.status;
  }

  public void setStatus(UserStatus status) {
    this.status = status;
  }

  public List<RoleCustomDto> getRoles() {
    return this.roles;
  }

  public void setRoles(List<RoleCustomDto> roles) {
    this.roles = roles;
  }
}
