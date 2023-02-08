package com.example.authservice.repo;

import com.example.authservice.entities.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author nguyen
 * @created_date 17/08/2020
 */
public interface RoleTypeRepository extends JpaRepository<RoleType, Integer> {

  Optional<RoleType> findByCode(String code);

  List<RoleType> findAllByCodeInAndIsDeletedFalse(List<String> types);
}
