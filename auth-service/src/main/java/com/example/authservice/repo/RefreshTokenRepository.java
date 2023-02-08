package com.example.authservice.repo;

import com.example.authservice.entities.application.RefreshToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author nguyen
 * @created_date 03/06/2020
 */
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

  Optional<RefreshToken> findByToken(String token);

  List<RefreshToken> findAllByClientIdAndIsDeletedFalse(Integer clientId);

  List<RefreshToken> findAllByClientId(Integer clientId);

  Optional<RefreshToken> findByClientIdAndApiKeyIdAndIpAndIsDeletedFalse(Integer clientId,
      Integer apiKey, String ip);

  Page<RefreshToken> findAll(Specification<RefreshToken> specification, Pageable pageable);
}
