package com.example.authservice.repo;

import com.example.authservice.entities.role.RoleDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoleDetailRepository extends JpaRepository<RoleDetail, Integer> {
    List<RoleDetail> findAllByRoleIdIn(@Param("ids") List<Integer> ids);

    List<RoleDetail> findAllByRoleIdInAndIsDeletedFalse(@Param("ids") List<Integer> ids);

    List<RoleDetail> findAllByRoleId(Integer roleId);

    Optional<RoleDetail> findByRoleIdAndPermissionIdAndIsDeletedFalse(Integer roleId, Integer permissionId);

    @Query("SELECT DISTINCT m.roleId FROM RoleDetail m WHERE m.isDeleted = FALSE ")
    List<Integer> findDistinctRoleId();

    @Query(
            value = "(SELECT rd.role_id FROM base_role_detail rd,sys_permission sp WHERE rd.permission_id=sp.id AND sp.object_code =:objectCode) EXCEPT ( SELECT rd.role_id FROM base_role_detail rd,sys_permission sp WHERE rd.permission_id=sp.id AND sp.object_code <> :objectCode)",
            nativeQuery = true
    )
    List<Integer> findRoleOnlyContainsPermissionObject(@Param("objectCode") String objectCode);
}
