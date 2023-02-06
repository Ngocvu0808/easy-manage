package com.example.authservice.entities.application;


import com.example.authservice.entities.BaseEntity;
import com.example.authservice.entities.enums.TokenStatus;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author bontk
 * @created_date 03/06/2020
 */

@Data
@Entity
@Table(name = "auth_access_token")
public class AccessToken extends BaseEntity<Integer> implements Serializable {

  private static final long serialVersionUID = 2239312902856105441L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String token;

  @Column(name = "refresh_token_id", insertable = false, updatable = false)
  private Long refreshTokenId;

  @ManyToOne(cascade = CascadeType.MERGE)
  @OnDelete(action = OnDeleteAction.NO_ACTION)
  @JoinColumn(name = "refresh_token_id", referencedColumnName = "id")
  private RefreshToken refreshToken;

  @Column(name = "expire_time")
  private Long expireTime;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private TokenStatus status;

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

  public Long getRefreshTokenId() {
    return refreshTokenId;
  }

  public void setRefreshTokenId(Long refreshTokenId) {
    this.refreshTokenId = refreshTokenId;
  }

  public RefreshToken getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(RefreshToken refreshToken) {
    this.refreshToken = refreshToken;
  }

  public Long getExpireTime() {
    return expireTime;
  }

  public void setExpireTime(Long expireTime) {
    this.expireTime = expireTime;
  }

  public TokenStatus getStatus() {
    return status;
  }

  public void setStatus(TokenStatus status) {
    this.status = status;
  }
}
