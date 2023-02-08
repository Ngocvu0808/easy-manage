package com.example.authservice.repo;


import com.example.authservice.entities.role.Role;

import java.util.List;

/**
 * @author nguyen
 * @created_date 17/08/2020
 */
public interface RoleExtendedRepository extends RoleRepository {

  List<Role> findAllByIsDeletedFalseAndTypeId(Integer type);

}
