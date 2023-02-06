package com.example.authservice.entities.application;

import com.example.authservice.entities.user.User;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @author bontk
 * @created_date 31/07/2020
 */
@Data
@Entity
@Table(name = "auth_client_user")
@NamedEntityGraphs(
    {
        @NamedEntityGraph(
            name = "graph.clientUser.permissions",
            attributeNodes = {
                @NamedAttributeNode(value = "permissions", subgraph = "sub-role")
            },
            subgraphs = {
                @NamedSubgraph(
                    name = "sub-role",
                    attributeNodes = {
                        @NamedAttributeNode("role")

                    }
                )
            }

        )
    }
)
public class ClientUser {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "client_id", insertable = false, updatable = false)
  private Integer clientId;

  @Column(name = "user_id", insertable = false, updatable = false)
  private Integer userId;

  @Column(name = "creator_user_id")
  private Integer creatorUserId;

  @Column(name = "deleter_user_id")
  private Integer deleterUserId;

  @Column(name = "created_time")
  @Temporal(TemporalType.TIMESTAMP)
  @CreationTimestamp
  private Date createdTime;

  @Column(name = "is_deleted")
  private Boolean isDeleted = false;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "client_id", referencedColumnName = "id")
  private Client client;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  @OneToMany(mappedBy = "clientUser", cascade = CascadeType.ALL)
  private List<ClientUserPermission> permissions;

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

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
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

  public Client getClient() {
    return client;
  }

  public void setClient(Client client) {
    this.client = client;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public List<ClientUserPermission> getPermissions() {
    return permissions;
  }

  public void setPermissions(List<ClientUserPermission> permissions) {
    this.permissions = permissions;
  }
}
