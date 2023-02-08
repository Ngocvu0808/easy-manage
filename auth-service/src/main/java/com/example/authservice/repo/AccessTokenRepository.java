package com.example.authservice.repo;


import com.example.authservice.entities.application.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author nguyen
 * @created_date 03/06/2020
 */
public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {

  List<AccessToken> findAllByRefreshTokenIdAndExpireTimeAfter(Long refreshTokenId, Long time);

  List<AccessToken> findAllByRefreshTokenIdAndIsDeletedFalse(Long refreshTokenId);

  List<AccessToken> findAllByRefreshTokenIdInAndExpireTimeAfter(List<Long> idList, Long time);

  AccessToken findByIdAndIsDeletedFalse(Long id);

  AccessToken findByTokenAndIsDeletedFalse(String token);

  @Query("SELECT at FROM AccessToken at, RefreshToken rf WHERE at.refreshTokenId=rf.id "
      + "AND at.isDeleted = FALSE AND rf.clientId=:clientId")
  List<AccessToken> findAllByClientId(Integer clientId);

  List<AccessToken> findByToken(String token);
}
