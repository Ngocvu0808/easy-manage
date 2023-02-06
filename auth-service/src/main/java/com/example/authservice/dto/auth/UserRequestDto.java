package com.example.authservice.dto.auth;

import java.util.Set;

public class UserRequestDto {

  private Integer id;
  private String username;
  private String name;
  private String email;
  private String firstName;
  private String lastName;
  private String password;
  private Set<Integer> roles;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Set<Integer> getRoles() {
    return roles;
  }

  public void setRoles(Set<Integer> roles) {
    this.roles = roles;
  }
}
