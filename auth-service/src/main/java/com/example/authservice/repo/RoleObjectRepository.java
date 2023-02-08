package com.example.authservice.repo;

import com.example.authservice.entities.role.RoleObject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleObjectRepository extends JpaRepository<RoleObject, Integer> {

  List<RoleObject> findAllByUserIdAndIsDeletedFalse(Integer userId);

  Optional<RoleObject> findByServiceNameAndObjectIdAndUserIdAndRoleIdAndIsDeletedFalse(
      String serviceName, Integer objectId, Integer userId, Integer roleId);

  List<RoleObject> findAllByServiceNameAndObjectIdAndUserIdAndIsDeletedFalse(String serviceName,
      Integer objectId, Integer userId);

  List<RoleObject> findAllByServiceNameAndObjectIdAndUserIdAndRoleIdInAndIsDeletedFalse(
      String serviceName, Integer objectId, Integer userId, List<Integer> roleIds);
}