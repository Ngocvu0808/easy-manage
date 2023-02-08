package com.example.authservice.repo;

import com.example.authservice.entities.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Integer>,
    JpaSpecificationExecutor<Group> {

  @EntityGraph("GroupRoles")
  Page<Group> findAll(Specification<Group> specification, Pageable pageable);

  Group findByCodeAndIsDeletedFalse(String code);

  @EntityGraph("GroupRoles")
  Optional<Group> findById(Integer id);

  List<Group> findAllByIsDeletedFalse();

  List<Group> findAllByIdInAndIsDeletedFalse(List<Integer> ids);
}
