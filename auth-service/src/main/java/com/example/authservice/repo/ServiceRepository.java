package com.example.authservice.repo;

import com.example.authservice.entities.service.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

/**
 * @author bontk
 * @created_date 03/08/2020
 */
public interface ServiceRepository extends JpaRepository<Service, Integer>,
    JpaSpecificationExecutor<Service> {

  Service findByCodeAndIsDeletedFalse(String code);

  Service findByIdAndIsDeletedFalse(Integer id);

  List<Service> findAllByIsDeletedFalse();

  List<Service> findAllByIdNotInAndIsDeletedFalse(List<Integer> ids);

  Optional<Service> findByCodeAndSystemIdAndIsDeletedFalse(String code, Integer systemId);
}
