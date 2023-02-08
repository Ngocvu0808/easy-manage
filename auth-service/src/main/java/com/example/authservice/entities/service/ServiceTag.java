package com.example.authservice.entities.service;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author nguyen
 * @created_date 03/08/2020
 */
@Data
@Entity
@Table(name = "service_tag")
public class ServiceTag implements Serializable {

  public static final long serialVersionUID = 4542423407538593770L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "service_id", insertable = false, updatable = false)
  private Integer serviceId;

  @Column(name = "tag_id", insertable = false, updatable = false)
  private Long tagId;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "service_id", referencedColumnName = "id")
  private Service service;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "tag_id", referencedColumnName = "id")
  private Tag tag;

  @Column(name = "is_deleted")
  private Boolean isDeleted = false;

  @Column(name = "created_time")
  @Temporal(TemporalType.TIMESTAMP)
  @CreationTimestamp
  private Date createdTime;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getServiceId() {
    return serviceId;
  }

  public void setServiceId(Integer serviceId) {
    this.serviceId = serviceId;
  }

  public Long getTagId() {
    return tagId;
  }

  public void setTagId(Long tagId) {
    this.tagId = tagId;
  }

  public Service getService() {
    return service;
  }

  public void setService(Service service) {
    this.service = service;
  }

  public Tag getTag() {
    return tag;
  }

  public void setTag(Tag tag) {
    this.tag = tag;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Boolean deleted) {
    isDeleted = deleted;
  }

  public Date getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(Date createdTime) {
    this.createdTime = createdTime;
  }
}
