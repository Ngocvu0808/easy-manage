package com.example.authservice.service.iface;


import com.example.authservice.dto.auth.*;
import com.example.authservice.dto.role.RoleCustomDto;
import com.example.authservice.dto.role.RoleDto;
import com.example.authservice.utils.exception.DuplicateEntityException;
import com.example.authservice.utils.exception.IdentifyBlankException;
import com.example.authservice.utils.exception.OperationNotImplementException;
import com.example.authservice.utils.exception.ResourceNotFoundException;
import com.example.authservice.utils.response.DataPagingResponse;

import java.util.List;

public interface RoleService {

  List<String> findAllCodePermission(List<String> roles);

  RoleResponseDto addRole(Integer creatorId, RoleDtoRequest dto)
          throws ResourceNotFoundException, DuplicateEntityException, IdentifyBlankException;

  RoleResponseDto getRole(Integer roleId) throws ResourceNotFoundException;

  RoleResponseDto updateRole(Integer updaterId, Integer roleId, RoleDtoRequest dto)
          throws ResourceNotFoundException, OperationNotImplementException, IdentifyBlankException;

  void deleteRole(Integer deleterId, Integer roleId)
          throws ResourceNotFoundException, OperationNotImplementException;

  List<RolePermissionDto> findAllSysPermission();

  List<RoleDto> findAllRole();

  DataPagingResponse<RoleDtoExtended> getAllRole(Integer page, Integer limit, String search, String sort,Boolean isSystemRole);

  List<RoleCustomDto> getRoleByType(String type);

  /**
   * Lấy ra danh sách permission theo danh sách vai trò và object code
   *
   * @param roleIds
   * @param objectCodes
   * @return
   */
  List<RolePermissionDto> findListPermission(List<Integer> roleIds, List<String> objectCodes);

  List<RoleDto> findListRoleByListObjectCode(String objectCodeList);
}
