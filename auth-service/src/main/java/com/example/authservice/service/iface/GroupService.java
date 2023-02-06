//package com.example.authservice.service.iface;
//
//
//import com.example.authservice.utils.exception.DuplicateEntityException;
//import com.example.authservice.utils.exception.IdentifyBlankException;
//import com.example.authservice.utils.exception.OperationNotImplementException;
//import com.example.authservice.utils.exception.ResourceNotFoundException;
//import org.springframework.data.domain.Pageable;
//
//import java.util.List;
//
//public interface GroupService {
//
//  GroupDto createGroup(GroupDto groupDto, Integer creatorUserId)
//      throws DuplicateEntityException, ResourceNotFoundException;
//
//  GroupDto updateGroupDto(GroupDto groupDto, Integer updaterUserId)
//      throws ResourceNotFoundException, IdentifyBlankException, OperationNotImplementException;
//
//  GroupDto findById(Integer groupId)
//      throws ResourceNotFoundException, IdentifyBlankException;
//
//  Boolean deleteGroupById(Integer groupId, Integer deleterUserId)
//      throws IdentifyBlankException, ResourceNotFoundException, OperationNotImplementException;
//
//  GroupDto addUserToGroup(GroupCustomDto groupCustomDto, Integer updaterUserId)
//      throws IdentifyBlankException, ResourceNotFoundException, DuplicateEntityException;
//
//  GroupDto updateUserToGroup(GroupCustomDto groupCustomDto, Integer updateUserId)
//      throws IdentifyBlankException, ResourceNotFoundException;
//
//  Boolean deleteUserFromGroup(Integer groupId, Integer userId, Integer deleterUserId)
//      throws IdentifyBlankException, ResourceNotFoundException, OperationNotImplementException;
//
//  Boolean deleteRoleFromGroup(Integer groupId, Integer roleId, Integer deleterUserId)
//      throws IdentifyBlankException, ResourceNotFoundException, OperationNotImplementException;
//
//  GroupDto updateRoleToGroup(GroupCustomDto groupCustomDto, Integer updaterUserId)
//      throws IdentifyBlankException, ResourceNotFoundException;
//
//  List<UserDto> findAllUserFromGroup(Integer groupId)
//      throws IdentifyBlankException, ResourceNotFoundException;
//
//  List<RoleDto> findAllRoleFromGroup(Integer groupId)
//      throws IdentifyBlankException, ResourceNotFoundException;
//
//  GroupDto addRoleToGroup(GroupCustomDto groupCustomDto, Integer updaterUserId)
//      throws IdentifyBlankException, ResourceNotFoundException, DuplicateEntityException;
//
//  List<RoleCustomDto> getAllRoleAssignedInGroup();
//
//  DataPagingResponse<GroupDto> findAll(String search, String role, String sort,
//      Boolean isDeleted, Pageable pageable);
//
//  List<CustomGroupDto> getAllGroup();
//}
