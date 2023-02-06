package com.example.authservice.entities.application;

import com.example.authservice.entities.role.Role;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * @author bontk
 * @created_date 31/07/2020
 */
@Data
@Entity
@Table(name = "auth_client_user_permission")
public class ClientUserPermission {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "role_id", insertable = false, updatable = false)
  private Integer roleId;

  @Column(name = "client_user_id", insertable = false, updatable = false)
  private Integer clientUserId;

  @Column(name = "created_time")
  @Temporal(TemporalType.TIMESTAMP)
  @CreationTimestamp
  private Date createdTime;

  @Column(name = "is_deleted")
  private Boolean isDeleted = false;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "role_id", referencedColumnName = "id")
  private Role role;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "client_user_id", referencedColumnName = "id")
  private ClientUser clientUser;

  @Column(name = "deleter_user_id")
  private Integer deleterUserId;

  @Column(name = "creator_user_id")
  private Integer creatorUserId;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getRoleId() {
    return roleId;
  }

  public void setRoleId(Integer roleId) {
    this.roleId = roleId;
  }

  public Integer getClientUserId() {
    return clientUserId;
  }

  public void setClientUserId(Integer clientUserId) {
    this.clientUserId = clientUserId;
  }

  public Date getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(Date createdTime) {
    this.createdTime = createdTime;
  }

  public Boolean getDeleted() {
    return isDeleted;
  }

  public void setDeleted(Boolean deleted) {
    isDeleted = deleted;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public ClientUser getClientUser() {
    return clientUser;
  }

  public void setClientUser(ClientUser clientUser) {
    this.clientUser = clientUser;
  }

  public Integer getDeleterUserId() {
    return deleterUserId;
  }

  public void setDeleterUserId(Integer deleterUserId) {
    this.deleterUserId = deleterUserId;
  }

  public Integer getCreatorUserId() {
    return creatorUserId;
  }

  public void setCreatorUserId(Integer creatorUserId) {
    this.creatorUserId = creatorUserId;
  }
}
