package com.example.authservice.entities.application;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author bontk
 * @created_date 05/06/2020
 */
@Data
@Entity
@Table(name = "auth_client_whitelist")
public class ClientWhiteList implements Serializable {

  private static final long serialVersionUID = -2484812750493330888L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "client_id", insertable = false, updatable = false)
  private Integer clientId;

  @ManyToOne(cascade = CascadeType.MERGE)
  @OnDelete(action = OnDeleteAction.NO_ACTION)
  @JoinColumn(name = "client_id", referencedColumnName = "id")
  private Client client;

  private String ip;

  @Column(name = "is_deleted")
  private Boolean isDeleted = false;

  @Column(name = "created_time")
  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdTime;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getClientId() {
    return clientId;
  }

  public void setClientId(Integer clientId) {
    this.clientId = clientId;
  }

  public Client getClient() {
    return client;
  }

  public void setClient(Client client) {
    this.client = client;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public Boolean getDeleted() {
    return isDeleted;
  }

  public void setDeleted(Boolean deleted) {
    isDeleted = deleted;
  }

  public Date getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(Date creationTime) {
    this.createdTime = creationTime;
  }
}
