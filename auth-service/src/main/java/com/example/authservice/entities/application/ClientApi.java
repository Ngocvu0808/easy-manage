package com.example.authservice.entities.application;

import com.example.authservice.entities.enums.ClientApiStatus;
import com.example.authservice.entities.service.ExternalApi;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author bontk
 * @created_date 31/07/2020
 */
@Data
@Entity
@Table(name = "auth_client_api")
public class ClientApi implements Serializable {

  public static final long serialVersionUID = 4495789663159792475L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "client_id", insertable = false, updatable = false)
  private Integer clientId;

  @Column(name = "api_id", insertable = false, updatable = false)
  private Long apiId;

  @Column(name = "is_deleted")
  private Boolean isDeleted = false;

  @Column(name = "created_time")
  @Temporal(TemporalType.TIMESTAMP)
  @CreationTimestamp
  private Date createdTime;

  @Column(name = "creator_user_id")
  private Integer creatorUserId;

  @Column(name = "updater_user_id")
  private Integer updaterUserId;

  @Column(name = "deleterUserId")
  private Integer deleterUserId;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "client_id", referencedColumnName = "id")
  private Client client;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "api_id", referencedColumnName = "id")
  private ExternalApi api;

  @Enumerated(EnumType.STRING)
  private ClientApiStatus status;

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

  public Long getApiId() {
    return apiId;
  }

  public void setApiId(Long apiId) {
    this.apiId = apiId;
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

  public void setCreatedTime(Date createdTime) {
    this.createdTime = createdTime;
  }

  public Integer getCreatorUserId() {
    return creatorUserId;
  }

  public void setCreatorUserId(Integer creatorUserId) {
    this.creatorUserId = creatorUserId;
  }

  public Integer getDeleterUserId() {
    return deleterUserId;
  }

  public void setDeleterUserId(Integer deleterUserId) {
    this.deleterUserId = deleterUserId;
  }

  public Client getClient() {
    return client;
  }

  public void setClient(Client client) {
    this.client = client;
  }

  public ExternalApi getApi() {
    return api;
  }

  public void setApi(ExternalApi api) {
    this.api = api;
  }

  public ClientApiStatus getStatus() {
    return status;
  }

  public void setStatus(ClientApiStatus status) {
    this.status = status;
  }

  public Integer getUpdaterUserId() {
    return updaterUserId;
  }

  public void setUpdaterUserId(Integer updaterUserId) {
    this.updaterUserId = updaterUserId;
  }
}
