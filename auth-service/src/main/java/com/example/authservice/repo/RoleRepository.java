package com.example.authservice.repo;

import com.example.authservice.entities.RoleType;
import com.example.authservice.entities.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer>, JpaSpecificationExecutor<Role> {
    List<Role> findAllByCodeIn(List<String> roles);

    List<Role> findAllByIdIn(List<Integer> ids);

    Optional<Role> findByCodeAndIsDeletedFalse(String code);

    Optional<Role> findByIdAndIsDeletedFalse(Integer id);

    List<Role> findAllByIsDeletedFalse();

    List<Role> findAllByIsDeletedFalseAndDefaultRoleTrue();

    List<Role> findAllByIsDeletedFalseAndIdIn(List<Integer> ids);

    List<Role> findAllByIsDeletedFalseAndType(RoleType roleType);
}
