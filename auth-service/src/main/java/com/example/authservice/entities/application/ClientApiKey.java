package com.example.authservice.entities.application;

import com.example.authservice.entities.BaseEntity;
import com.example.authservice.entities.enums.ClientApiKeyStatus;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author nguyen
 * @created_date 17/09/2020
 */
@Table(name = "auth_client_api_key")
@Entity
@Data
public class ClientApiKey extends BaseEntity<Integer> implements Serializable {

  private static final long serialVersionUID = -3247116790931476746L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "api_key")
  private String apiKey;

  @Column(name = "client_id", insertable = false, updatable = false)
  private Integer clientId;

  @ManyToOne(cascade = CascadeType.MERGE)
  @OnDelete(action = OnDeleteAction.NO_ACTION)
  @JoinColumn(name = "client_id", referencedColumnName = "id")
  private Client client;

  @Enumerated(EnumType.STRING)
  private ClientApiKeyStatus status;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getApiKey() {
    return apiKey;
  }

  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
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

  public ClientApiKeyStatus getStatus() {
    return status;
  }

  public void setStatus(ClientApiKeyStatus status) {
    this.status = status;
  }
}
