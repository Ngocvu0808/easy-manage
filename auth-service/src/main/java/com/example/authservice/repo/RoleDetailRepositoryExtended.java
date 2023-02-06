package com.example.authservice.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author bontk
 * @created_date 04/09/2020
 */
public interface RoleDetailRepositoryExtended extends RoleDetailRepository {

  @Query(nativeQuery = true,
      value =
          "(SELECT rd.role_id FROM base_role_detail rd,sys_permission sp WHERE rd.permission_id=sp.id AND sp.object_code IN (:objectCode) AND rd.is_deleted IS FALSE)"
              + " EXCEPT ( SELECT rd.role_id FROM base_role_detail rd,sys_permission sp WHERE rd.permission_id=sp.id AND sp.object_code NOT IN (:objectCode) AND rd.is_deleted IS FALSE)")
  List<Integer> findRoleOnlyContainsPermissionObject(@Param("objectCode") List<String> objectCode);

}
