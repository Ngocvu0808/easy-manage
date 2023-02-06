package com.example.authservice.dto.service;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

public class ServiceRequestDto {

  @NotNull(message = "Name not null")
  @Size(max = 255, message = "Name onger than 255 characters")
  private String name;

  @NotNull(message = "Description not null")
  @Size(max = 1000, message = "Description onger than 255 characters")
  private String description;

  @NotNull(message = "Code not null")
  private String code;

  @NotNull(message = "SystemId not null")
  private Integer systemId;

  private List<String> listTagName;

  private Set<Long> tagIds;

  private Integer id;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Set<Long> getTagIds() {
    return tagIds;
  }

  public void setTagIds(Set<Long> tagIds) {
    this.tagIds = tagIds;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getSystemId() {
    return systemId;
  }

  public void setSystemId(Integer systemId) {
    this.systemId = systemId;
  }


  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public List<String> getListTagName() {
    return listTagName;
  }

  public void setListTagName(List<String> listTagName) {
    this.listTagName = listTagName;
  }
}
