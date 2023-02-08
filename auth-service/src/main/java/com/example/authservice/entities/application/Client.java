package com.example.authservice.entities.application;

import com.example.authservice.entities.enums.ClientAuthType;
import com.example.authservice.entities.enums.ClientStatus;
import com.example.authservice.entities.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
 * @author nguyen
 * @created_date 02/06/2020
 */
@Data
@Entity
@Table(name = "auth_client")
public class Client implements Serializable {

  private static final long serialVersionUID = 6215844849520409423L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;

  @Column(name = "client_id")
  private String clientId;

  @JsonIgnore
  @Column(name = "client_secret")
  private String clientSecret;

  private String description;

  @Column(name = "created_time")
  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdTime;

  @Column(name = "modified_time")
  @UpdateTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  private Date lastModifiedTime;

  @Column(name = "is_deleted")
  private Boolean isDeleted;

  @Column(name = "creator_id", insertable = false, updatable = false)
  private Integer creatorId;

  @Column(name = "owner_id", insertable = false, updatable = false)
  private Integer ownerId;

  @ManyToOne(cascade = CascadeType.MERGE)
  @OnDelete(action = OnDeleteAction.NO_ACTION)
  @JoinColumn(name = "creator_id", referencedColumnName = "id")
  private User creatorUser;

  @ManyToOne(cascade = CascadeType.MERGE)
  @OnDelete(action = OnDeleteAction.NO_ACTION)
  @JoinColumn(name = "owner_id", referencedColumnName = "id")
  private User owner;

  @Enumerated(EnumType.STRING)
  private ClientStatus status;

  @Column(name = "approve_require", columnDefinition = "boolean not null default true")
  private Boolean approveRequire = true;

  @Column(name = "share_token", columnDefinition = "boolean not null default false")
  private Boolean shareToken = false;

  @Enumerated(EnumType.STRING)
  @Column(name = "auth_type")
  private ClientAuthType authType;

  @OneToMany(mappedBy = "client")
  private List<RefreshToken> refreshTokens;

  @OneToMany(mappedBy = "client")
  private List<ClientApi> apis;

  @OneToMany(mappedBy = "client")
  private List<ClientApiKey> clientApiKey;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getClientSecret() {
    return clientSecret;
  }

  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String redirectUrls) {
    this.description = redirectUrls;
  }

  public static long getSerialVersionUID() {
    return serialVersionUID;
  }

  public Date getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(Date creationTime) {
    this.createdTime = creationTime;
  }

  public Date getLastModifiedTime() {
    return lastModifiedTime;
  }

  public void setLastModifiedTime(Date lastModifiedTime) {
    this.lastModifiedTime = lastModifiedTime;
  }

  public Boolean getDeleted() {
    return isDeleted;
  }

  public void setDeleted(Boolean deleted) {
    isDeleted = deleted;
  }

  public Integer getCreatorId() {
    return creatorId;
  }

  public void setCreatorId(Integer creatorId) {
    this.creatorId = creatorId;
  }

  public ClientStatus getStatus() {
    return status;
  }

  public void setStatus(ClientStatus status) {
    this.status = status;
  }

  public User getCreatorUser() {
    return creatorUser;
  }

  public void setCreatorUser(User creatorUser) {
    this.creatorUser = creatorUser;
  }

  public List<RefreshToken> getRefreshTokens() {
    return refreshTokens;
  }

  public void setRefreshTokens(List<RefreshToken> refreshTokens) {
    this.refreshTokens = refreshTokens;
  }

  public Integer getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(Integer ownerId) {
    this.ownerId = ownerId;
  }

  public User getOwner() {
    return owner;
  }

  public void setOwner(User owner) {
    this.owner = owner;
  }

  public List<ClientApi> getApis() {
    return apis;
  }

  public void setApis(List<ClientApi> apis) {
    this.apis = apis;
  }

  public Boolean getApproveRequire() {
    return approveRequire;
  }

  public void setApproveRequire(Boolean approveRequire) {
    this.approveRequire = approveRequire;
  }

  public Boolean getShareToken() {
    return shareToken;
  }

  public void setShareToken(Boolean shareToken) {
    this.shareToken = shareToken;
  }

  public ClientAuthType getAuthType() {
    return authType;
  }

  public void setAuthType(ClientAuthType authType) {
    this.authType = authType;
  }

  public List<ClientApiKey> getClientApiKey() {
    return clientApiKey;
  }

  public void setClientApiKey(List<ClientApiKey> clientApiKey) {
    this.clientApiKey = clientApiKey;
  }
}
