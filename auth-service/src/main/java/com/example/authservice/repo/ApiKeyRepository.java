package com.example.authservice.repo;

import com.example.authservice.entities.application.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * @author nguyen
 * @created_date 03/06/2020
 */
public interface ApiKeyRepository extends JpaRepository<ApiKey, Integer> {

  @Query("SELECT token FROM ApiKey token WHERE token.apiKey=:apiKey AND token.isDeleted=FALSE  AND token.user.isDeleted=FALSE AND token.user.status='ACTIVE'")
  Optional<ApiKey> findByApiKey(@Param("apiKey") String apiKey);

  Optional<ApiKey> findByUserIdAndIsDeletedFalse(Integer userId);

  ApiKey findByIdAndIsDeletedFalse(Integer id);
}
