package com.example.authservice.repo;

import com.example.authservice.entities.RoleGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface RoleGroupRepository extends JpaRepository<RoleGroup, Integer>,
    JpaSpecificationExecutor<RoleGroup> {

  RoleGroup findByRoleIdAndGroupId(Integer roleId, Integer groupId);

  List<RoleGroup> findByGroupIdAndRoleIdAndIsDeletedFalse(Integer groupId, Integer roleId);

  List<RoleGroup> findByGroupIdAndIsDeletedFalse(Integer groupId);

  List<RoleGroup> findAllByIsDeletedFalseAndGroupIdIn(List<Integer> idList);

  List<RoleGroup> findAllByIsDeletedFalse();

  List<RoleGroup> findAllByRoleIdAndIsDeletedFalse(Integer roleId);
}
