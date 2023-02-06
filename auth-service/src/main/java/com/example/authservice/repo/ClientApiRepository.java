package com.example.authservice.repo;

import com.example.authservice.entities.application.ClientApi;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author bontk
 * @created_date 04/08/2020
 */
public interface ClientApiRepository extends JpaRepository<ClientApi, Integer>,
    JpaSpecificationExecutor<ClientApi> {

  Optional<ClientApi> findByClientIdAndApiIdAndIsDeletedFalse(Integer clientId, Long apiId);

  List<ClientApi> findAll(Specification<ClientApi> specification);

  @Query("SELECT ca FROM ClientApi ca, ExternalApi  ea WHERE ca.apiId=ea.id AND ca.clientId=:clientId AND ea.serviceId=:serviceId AND ea.isDeleted=FALSE ")
  List<ClientApi> findAllByClientIdAndServiceId(@Param("clientId") Integer clientId,
      @Param("serviceId") Integer serviceId);

  List<ClientApi> findAllByClientIdAndIsDeletedFalse(Integer clientId);
}
