package com.example.authservice.repo;

import com.example.authservice.entities.application.ClientService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author nguyen
 * @created_date 03/08/2020
 */
public interface ClientServiceRepository extends JpaRepository<ClientService, Integer> {

  Optional<ClientService> findByClientIdAndServiceIdAndIsDeletedFalse(Integer clientId,
      Integer serviceId);

  List<ClientService> findAllByClientIdAndIsDeletedFalse(@Param("clientId") Integer clientId);

}
