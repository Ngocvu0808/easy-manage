package com.example.authservice.repo;

import com.example.authservice.entities.RoleUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface RoleUserRepository extends JpaRepository<RoleUser, Integer> {

  List<RoleUser> findAllByUserId(Integer userId);

  List<RoleUser> findAllByIsDeletedFalse();

  Optional<RoleUser> findByRoleIdAndUserIdAndIsDeletedIsFalse(Integer roleId, Integer userId);

  @Query("SELECT r FROM RoleUser r WHERE r.userId=:userId AND r.isDeleted = FALSE")
  List<RoleUser> findListRoleActive(@Param("userId") Integer userId);

  Optional<RoleUser> findByUserIdAndRoleIdAndIsDeletedFalse(Integer userId, Integer roleId);

  @Transactional
  @Modifying(
      clearAutomatically = true
  )
  @Query("UPDATE RoleUser r SET r.isDeleted=TRUE WHERE r.userId <> 1")
  void deleteAllRoleUser(Integer deleterId);

  List<RoleUser> findAllByRoleIdAndIsDeletedFalse(Integer roleId);
}
