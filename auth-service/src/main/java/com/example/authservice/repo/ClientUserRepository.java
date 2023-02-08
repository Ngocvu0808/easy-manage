package com.example.authservice.repo;

import com.example.authservice.entities.application.ClientUser;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author nguyen
 * @created_date 01/08/2020
 */
public interface ClientUserRepository extends JpaRepository<ClientUser, Integer>,
    JpaSpecificationExecutor<ClientUser> {

  Optional<ClientUser> findByClientIdAndUserIdAndIsDeletedFalse(Integer clientId, Integer userId);

  List<ClientUser> findAllByClientIdAndIsDeletedFalse(Integer clientId);

  @Query("SELECT distinct cu.id FROM ClientUser cu WHERE cu.clientId=:clientId AND cu.isDeleted=FALSE")
  List<Integer> findAllIdByClientId(@Param("clientId") Integer clientId);

  @EntityGraph(value = "graph.clientUser.permissions", type = EntityGraphType.LOAD)
  List<ClientUser> findAllByUserIdInAndIsDeletedFalse(List<Integer> ids);

  List<ClientUser> findAllByUserIdAndIsDeletedFalse(Integer userId);

  @EntityGraph(value = "graph.clientUser.permissions", type = EntityGraphType.LOAD)
  List<ClientUser> findAll(Specification<ClientUser> specification);

  @Query("SELECT DISTINCT cu.clientId from ClientUser  cu WHERE cu.userId=:userId")
  List<Integer> findListClientIdByUserId(@Param("userId") Integer userId);
}
