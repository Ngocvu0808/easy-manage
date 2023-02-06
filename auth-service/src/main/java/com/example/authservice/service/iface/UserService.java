package com.example.authservice.service.iface;


import com.example.authservice.dto.auth.ChangePassRequestDto;
import com.example.authservice.dto.auth.DeleteUserListDto;
import com.example.authservice.dto.auth.RegisterRequestDto;
import com.example.authservice.dto.auth.UpdateStatusUserListDto;
import com.example.authservice.dto.filter.UserStatusDto;
import com.example.authservice.dto.group.GroupAndUserDto;
import com.example.authservice.dto.role.RoleCustomDto;
import com.example.authservice.dto.role.RoleDto;
import com.example.authservice.dto.user.UserDto;
import com.example.authservice.entities.UserStatus;
import com.example.authservice.utils.exception.*;
import com.example.authservice.utils.permission.Permission;
import com.example.authservice.utils.response.DataPagingResponse;

import java.util.List;

public interface UserService {

  UserDto createUser(RegisterRequestDto request, Integer userId)
          throws DuplicateEntityException, IdentifyBlankException;

  void deleteUser(Integer userId, Integer deleterId) throws ResourceNotFoundException;

  UserDto updateStatusUser(Integer userId, Integer updaterId, UserStatus status)
          throws ResourceNotFoundException;

  List<RoleDto> getRolesUser(Integer userId);

  void addRoleUser(Integer userId, Integer creatorId, List<Integer> roleIds)
          throws ResourceNotFoundException, DuplicateEntityException;

  void updateRoleUser(Integer userId, Integer updaterId, List<Integer> roleIds)
          throws ResourceNotFoundException, DuplicateEntityException;

  void deleteRoleUser(Integer userId, Integer deleterId, List<Integer> roleIds)
          throws ResourceNotFoundException, DuplicateEntityException;

  UserDto getUserById(String token, Integer userId)
          throws ResourceNotFoundException, DuplicateEntityException, IdentifyBlankException;

  UserDto updateUser(UserDto userDto, Integer updaterUserId)
          throws ResourceNotFoundException, IdentifyBlankException, DuplicateEntityException;

  List<GroupAndUserDto> getAllGroupAndUser(String filter);

  List<RoleCustomDto> getAllRoleAssignedUser();

//  List<GroupUserCustomResponseDto> getAllGroupAssignedUser();

  DataPagingResponse<UserDto> getAll(Integer page, Integer limit, String status,
                                     String roles, String groups, String search, String sort);

  void updateStatusListUser(UpdateStatusUserListDto dto, Integer updaterId);

  void deleteUserList(DeleteUserListDto dto, Integer deleterId) throws ResourceNotFoundException;

  List<UserStatusDto> getListStatusUser();

  void deleteAll(Integer deleterId) throws ResourceNotFoundException;

  void updateStatusAll(Integer updaterId, UserStatus status)
          throws ResourceNotFoundException;

  Permission getPermissionsOfUser(Integer userId) throws IdentifyBlankException;

  UserDto resetPassword(Integer userId, Integer updaterId) throws ResourceNotFoundException;

  UserDto changePass(Integer userId, ChangePassRequestDto dto)
          throws ResourceNotFoundException, CryptoException, OperationNotImplementException;
}
