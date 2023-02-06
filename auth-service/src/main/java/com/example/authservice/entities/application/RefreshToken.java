package com.example.authservice.entities.application;

import com.example.authservice.entities.enums.RefreshTokenStatus;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author bontk
 * @created_date 03/06/2020
 */

@Data
@Entity
@Table(name = "auth_refresh_token")
public class RefreshToken implements Serializable {

  private static final long serialVersionUID = -6638580153301953710L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String token;

  @Column(name = "expire_time")
  private Long expireTime;

  @Column(name = "created_time")
  @Temporal(TemporalType.TIMESTAMP)
  @CreationTimestamp
  private Date createdTime;

  private Boolean approved;

  @Column(name = "client_id", insertable = false, updatable = false)
  private Integer clientId;

  @ManyToOne(cascade = CascadeType.MERGE)
  @OnDelete(action = OnDeleteAction.NO_ACTION)
  @JoinColumn(name = "client_id", referencedColumnName = "id")
  private Client client;

  @Column(name = "api_key_id", insertable = false, updatable = false)
  private Integer apiKeyId;

  @ManyToOne(cascade = CascadeType.MERGE)
  @OnDelete(action = OnDeleteAction.NO_ACTION)
  @JoinColumn(name = "api_key_id", referencedColumnName = "id")
  private ApiKey apiKey;

  @Column(name = "is_deleted")
  private Boolean isDeleted;

  @Enumerated(EnumType.STRING)
  private RefreshTokenStatus status;

  private String ip;

  @Column(name = "modified_time")
  @Temporal(TemporalType.TIMESTAMP)
  @UpdateTimestamp
  private Date updateTime;

  @OneToMany(mappedBy = "refreshToken")
  private List<AccessToken> accessTokens;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Long getExpireTime() {
    return expireTime;
  }

  public void setExpireTime(Long expireTime) {
    this.expireTime = expireTime;
  }

  public Date getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(Date createdTime) {
    this.createdTime = createdTime;
  }

  public Boolean getApproved() {
    return approved;
  }

  public void setApproved(Boolean approved) {
    this.approved = approved;
  }

  public Integer getClientId() {
    return clientId;
  }

  public void setClientId(Integer clientId) {
    this.clientId = clientId;
  }

  public Integer getApiKeyId() {
    return apiKeyId;
  }

  public void setApiKeyId(Integer apiKey) {
    this.apiKeyId = apiKey;
  }

  public ApiKey getApiKey() {
    return apiKey;
  }

  public void setApiKey(ApiKey apiKey) {
    this.apiKey = apiKey;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setDeleted(Boolean deleted) {
    isDeleted = deleted;
  }

  public RefreshTokenStatus getStatus() {
    return status;
  }

  public void setStatus(RefreshTokenStatus status) {
    this.status = status;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public Client getClient() {
    return client;
  }

  public void setClient(Client client) {
    this.client = client;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }


  public Boolean getDeleted() {
    return isDeleted;
  }

  public List<AccessToken> getAccessTokens() {
    return accessTokens;
  }

  public void setAccessTokens(List<AccessToken> accessTokens) {
    this.accessTokens = accessTokens;
  }


}
