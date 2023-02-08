package com.example.authservice.repo;

import com.example.authservice.entities.application.ApiRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ApiRequestRepository extends JpaRepository<ApiRequest, Long>,
    JpaSpecificationExecutor<ApiRequest> {

  Page<ApiRequest> findAll(Specification<ApiRequest> specification, Pageable pageable);

  Optional<ApiRequest> findByClientIdAndApiIdAndIsDeletedFalse(Integer clientId, Long apiId);

}
