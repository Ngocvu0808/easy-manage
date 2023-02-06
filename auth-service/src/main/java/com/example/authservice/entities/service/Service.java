package com.example.authservice.entities.service;

import com.example.authservice.entities.BaseEntity;
import com.example.authservice.entities.enums.ServiceStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author bontk
 * @created_date 31/07/2020
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "service")
public class Service extends BaseEntity<Integer> implements Serializable {

  private static final long serialVersionUID = -3646163569250044214L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String code;

  private String name;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(name = "system_id", insertable = false, updatable = false)
  private Integer systemId;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "system_id", referencedColumnName = "id")
  private System system;

  @Enumerated(EnumType.STRING)
  private ServiceStatus status;

  private String tag;

  @OneToMany(mappedBy = "service")
  private List<ExternalApi> apis;


  @OneToMany(mappedBy = "service")
  private List<ServiceTag> serviceTags;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
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

  public System getSystem() {
    return system;
  }

  public void setSystem(System system) {
    this.system = system;
  }

  public ServiceStatus getStatus() {
    return status;
  }

  public void setStatus(ServiceStatus status) {
    this.status = status;
  }

  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public List<ExternalApi> getApis() {
    return apis;
  }

  public void setApis(List<ExternalApi> apis) {
    this.apis = apis;
  }

  public List<ServiceTag> getServiceTags() {
    return serviceTags;
  }

  public void setServiceTags(List<ServiceTag> serviceTags) {
    this.serviceTags = serviceTags;
  }
}
