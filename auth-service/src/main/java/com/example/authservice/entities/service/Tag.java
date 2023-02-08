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
@Table(name = "tag")
public class Tag implements Serializable {

  public static final long serialVersionUID = -2903891462111686330L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String tag;

  @Column(name = "created_time")
  @Temporal(TemporalType.TIMESTAMP)
  @CreationTimestamp
  private Date createdTime;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public Date getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(Date createdTime) {
    this.createdTime = createdTime;
  }
}
