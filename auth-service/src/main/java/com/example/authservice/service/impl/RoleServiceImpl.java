package com.example.authservice.service.impl;


import com.example.authservice.dto.SysPermissionDto;
import com.example.authservice.dto.auth.RoleDtoExtended;
import com.example.authservice.dto.auth.RoleDtoRequest;
import com.example.authservice.dto.auth.RolePermissionDto;
import com.example.authservice.dto.auth.RoleResponseDto;
import com.example.authservice.dto.role.RoleCustomDto;
import com.example.authservice.dto.role.RoleDto;
import com.example.authservice.entities.*;
import com.example.authservice.entities.role.Role;
import com.example.authservice.entities.role.RoleDetail;
import com.example.authservice.entities.user.User;
import com.example.authservice.exception.AuthServiceMessageCode;
import com.example.authservice.filter.RoleFilter;
import com.example.authservice.mapper.RoleMapper;
import com.example.authservice.mapper.SysPermissionMapper;
import com.example.authservice.repo.*;
import com.example.authservice.service.iface.RoleService;
import com.example.authservice.utils.ServiceInfo;
import com.example.authservice.utils.SortingUtils;
import com.example.authservice.utils.cache.CacheRedisService;
import com.example.authservice.utils.exception.DuplicateEntityException;
import com.example.authservice.utils.exception.IdentifyBlankException;
import com.example.authservice.utils.exception.OperationNotImplementException;
import com.example.authservice.utils.exception.ResourceNotFoundException;
import com.example.authservice.utils.response.DataPagingResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

  private final RoleExtendedRepository roleRepository;
  private final SysPermissionRepository sysPermissionRepository;
  private final RoleDetailRepositoryExtended roleDetailRepository;
  private final UserRepository userRepository;
  private final RoleUserRepository roleUserRepository;
  private final RoleGroupRepository roleGroupRepository;
  private final GroupUserRepository groupUserRepository;
  private final RoleTypeRepository roleTypeRepository;

  private final SysPermissionMapper sysPermissionMapper;
  private final RoleMapper roleMapper;

  private final CacheRedisService redisService;

  @Value("${auth.code.prefix:auth:jwt:}")
  private String prefix;

  public RoleServiceImpl(RoleExtendedRepository roleRepository,
      SysPermissionRepository sysPermissionRepository,
      RoleDetailRepositoryExtended roleDetailRepository,
      UserRepository userRepository,
      RoleUserRepository roleUserRepository,
      RoleGroupRepository roleGroupRepository,
      GroupUserRepository groupUserRepository,
      RoleTypeRepository roleTypeRepository,
      SysPermissionMapper sysPermissionMapper,
      RoleMapper roleMapper,
      CacheRedisService redisService) {
    this.roleRepository = roleRepository;
    this.sysPermissionRepository = sysPermissionRepository;
    this.roleDetailRepository = roleDetailRepository;
    this.userRepository = userRepository;
    this.roleUserRepository = roleUserRepository;
    this.roleGroupRepository = roleGroupRepository;
    this.groupUserRepository = groupUserRepository;
    this.roleTypeRepository = roleTypeRepository;
    this.sysPermissionMapper = sysPermissionMapper;
    this.roleMapper = roleMapper;
    this.redisService = redisService;
  }

  @Override
  public List<String> findAllCodePermission(List<String> roles) {
    List<Role> roleList = roleRepository.findAllByCodeIn(roles);
    List<Integer> ids = roleList.stream().map(Role::getId).collect(Collectors.toList());
    List<SysPermission> permissionList = findAllSysPermissionByRoleIds(ids);
    return permissionList.stream().map(SysPermission::getCode).collect(Collectors.toList());
  }

  @Override
  public RoleResponseDto addRole(Integer creatorId, RoleDtoRequest dto)
      throws ResourceNotFoundException, DuplicateEntityException, IdentifyBlankException {
    Optional<User> user = userRepository.findById(creatorId);
    if (user.isEmpty()) {
      throw new ResourceNotFoundException("User doesn't exist",
          ServiceInfo.getId() + AuthServiceMessageCode.USER_NOT_EXIST);
    }
    RoleDto roleDto = new RoleDto();
    roleDto.setCode(dto.getCode());
    Optional<Role> roleOptional = roleRepository.findByCodeAndIsDeletedFalse(dto.getCode());
    if (roleOptional.isPresent()) {
      throw new DuplicateEntityException("Role already exist",
          ServiceInfo.getId() + AuthServiceMessageCode.ROLE_ALREADY_EXIST);
    }
    Role r = new Role();
    r.setCode(dto.getCode());
    r.setName(dto.getName());
    r.setNote(dto.getNote());
    r.setDefaultRole(dto.getDefaultRole());
    r.setCreatorUser(user.get());
    Role role = roleRepository.save(r);
    List<Integer> sysPermissionIds = dto.getPermissions();
    if (sysPermissionIds == null || sysPermissionIds.isEmpty()) {
      throw new IdentifyBlankException("Permissions not null",
          ServiceInfo.getId() + AuthServiceMessageCode.PERMISSION_NOT_NULL);
    }
    addPermissionRole(creatorId, role, sysPermissionIds);
    return getRoleResponseDto(role);
  }

  public RoleResponseDto getRoleResponseDto(Role role) {
    RoleResponseDto res = new RoleResponseDto();
    res.setId(role.getId());
    res.setCode(role.getCode());
    res.setName(role.getName());
    res.setDefaultRole(role.getDefaultRole());
    res.setPermissions(findAllPermissionOfRoleList(Collections.singletonList(role)));
    return res;
  }

  private void addPermissionRole(Integer creatorId, Role role, List<Integer> ids)
      throws ResourceNotFoundException {
    List<RoleDetail> roleDetailList = new ArrayList<>();
    for (Integer i : ids) {
      Optional<SysPermission> sysPermissionOptional = sysPermissionRepository.findById(i);
      if (sysPermissionOptional.isEmpty()) {
        throw new ResourceNotFoundException("Permission doesn't exist",
            ServiceInfo.getId() + AuthServiceMessageCode.PERMISSION_NOT_EXIST);
      }
      SysPermission permission = sysPermissionOptional.get();
      RoleDetail roleDetail = new RoleDetail();
      roleDetail.setCreatorUserId(creatorId);
      roleDetail.setPermission(permission);
      roleDetail.setRole(role);
      roleDetailList.add(roleDetail);
    }
    roleDetailRepository.saveAll(roleDetailList);
  }

  @Override
  public RoleResponseDto getRole(Integer roleId) throws ResourceNotFoundException {
    Optional<Role> optional = roleRepository.findByIdAndIsDeletedFalse(roleId);
    if (optional.isEmpty() || optional.get().getIsDeleted().equals(Boolean.TRUE)) {
      throw new ResourceNotFoundException("Role doesn't exist",
          ServiceInfo.getId() + AuthServiceMessageCode.ROLE_NOT_EXIST);
    }
    Role role = optional.get();
    return getRoleResponseDto(role);
  }

  @Override
  public RoleResponseDto updateRole(Integer updaterId, Integer roleId, RoleDtoRequest dto)
      throws ResourceNotFoundException, OperationNotImplementException, IdentifyBlankException {
    Optional<Role> optional = roleRepository.findByIdAndIsDeletedFalse(roleId);
    if (optional.isEmpty() || optional.get().getIsDeleted().equals(Boolean.TRUE)) {
      throw new ResourceNotFoundException("Role doesn't exist",
          ServiceInfo.getId() + AuthServiceMessageCode.ROLE_NOT_EXIST);
    }
    Role role = optional.get();
    if (role.getIsSystemRole().equals(Boolean.TRUE) ||
        (dto.getCode() != null && !dto.getCode().equals(role.getCode()))) {
      throw new OperationNotImplementException("Can't edit code role",
          ServiceInfo.getId() + AuthServiceMessageCode.ROLE_CODE_CANNOT_MODIFY);
    }
    List<Integer> permissionIds = dto.getPermissions();
    if (permissionIds == null || permissionIds.isEmpty()) {
      throw new IdentifyBlankException("Permissions not null",
          ServiceInfo.getId() + AuthServiceMessageCode.PERMISSION_NOT_NULL);
    }
    for (Integer i : permissionIds) {
      Optional<SysPermission> sysPermissionOptional = sysPermissionRepository.findById(i);
      if (sysPermissionOptional.isEmpty()) {
        throw new ResourceNotFoundException("Permission doesn't exist",
            ServiceInfo.getId() + AuthServiceMessageCode.PERMISSION_NOT_EXIST);
      }
    }
    if (dto.getName() != null && !dto.getName().isBlank()) {
      role.setName(dto.getName());
    }
    if (dto.getDefaultRole() != null) {
      role.setDefaultRole(dto.getDefaultRole());
    }
    Role marRole = roleRepository.save(role);

    // update permission
    List<RoleDetail> roleDetailList = roleDetailRepository.findAllByRoleId(roleId);
    List<Integer> idListActive = roleDetailList.stream()
        .filter(r -> r.getIsDeleted().equals(Boolean.FALSE))
        .map(RoleDetail::getPermissionId).collect(Collectors.toList());
    List<Integer> listIdAdd = permissionIds.stream().distinct()
        .filter(id -> !idListActive.contains(id))
        .collect(Collectors.toList());
    List<Integer> listIdRemove = idListActive.stream().distinct()
        .filter(id -> !permissionIds.contains(id))
        .collect(Collectors.toList());
    for (Integer id : listIdRemove) {
      Optional<RoleDetail> roleDetailOptional = roleDetailRepository
          .findByRoleIdAndPermissionIdAndIsDeletedFalse(roleId, id);
      if (roleDetailOptional.isPresent()) {
        RoleDetail r = roleDetailOptional.get();
        r.setIsDeleted(Boolean.TRUE);
        r.setDeleterUserId(updaterId);
        roleDetailRepository.save(r);
      }
    }
    addPermissionRole(updaterId, role, listIdAdd);
//    if (idListActive.size() != permissionIds.size() || !idListActive.containsAll(permissionIds)) {
//      this.forceLogout(marRole);
//    }
    return getRoleResponseDto(marRole);
  }

  @Override
  public void deleteRole(Integer deleterId, Integer roleId)
      throws ResourceNotFoundException, OperationNotImplementException {
    Optional<Role> optional = roleRepository.findByIdAndIsDeletedFalse(roleId);
    if (optional.isEmpty()) {
      throw new ResourceNotFoundException("Role doesn't exist",
          ServiceInfo.getId() + AuthServiceMessageCode.ROLE_NOT_EXIST);
    }
    Role role = optional.get();
    if (role.getIsSystemRole().equals(Boolean.TRUE)) {
      throw new OperationNotImplementException("can't delete role",
          ServiceInfo.getId() + AuthServiceMessageCode.ROLE_INVALID);
    }

    role.setIsDeleted(Boolean.TRUE);
    role.setUpdaterUserId(deleterId);

    List<RoleDetail> roleDetailList = roleDetailRepository.findAllByRoleId(roleId).stream()
        .filter(m -> m.getIsDeleted().equals(Boolean.FALSE))
        .collect(Collectors.toList());
    roleDetailList.forEach(r -> {
      r.setIsDeleted(Boolean.TRUE);
      r.setDeleterUserId(deleterId);
    });
    roleDetailRepository.saveAll(roleDetailList);
//    this.forceLogout(role);
  }

  @Override
  public List<RolePermissionDto> findAllSysPermission() {
    List<SysPermission> permissions = sysPermissionRepository.findAll();
    return findAllPermission(permissions);
  }

  @Override
  public List<RoleDto> findAllRole() {
    List<Role> roleList = roleRepository.findAllByIsDeletedFalse();
    return roleList.stream()
        .filter(it -> it.getIsSystemRole().equals(Boolean.FALSE))
        .map(roleMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public DataPagingResponse<RoleDtoExtended> getAllRole(Integer page, Integer limit, String search,
      String sort, Boolean isSystemRole) {
    Map<String, String> map = SortingUtils.detectSortType(sort);
    Page<Role> marRolePage = roleRepository
        .findAll(new RoleFilter().getByFilter(search, map, false, isSystemRole),
            PageRequest.of(page - 1, limit));
    List<Role> list = marRolePage.getContent();
    List<RoleDtoExtended> data = list.stream()
        .map(roleMapper::toExtendedDto)
        .collect(Collectors.toList());
    DataPagingResponse<RoleDtoExtended> dataPagingResponse = new DataPagingResponse<>();
    dataPagingResponse.setList(data);
    dataPagingResponse.setNum(marRolePage.getTotalElements());
    dataPagingResponse.setTotalPage(marRolePage.getTotalPages());
    dataPagingResponse.setCurrentPage(page);
    return dataPagingResponse;
  }

  @Override
  public List<RoleCustomDto> getRoleByType(String type) {
    Optional<RoleType> roleTypeOptional = roleTypeRepository.findByCode(type);
    if (roleTypeOptional.isEmpty()) {
      return new ArrayList<>();
    }
    RoleType roleType = roleTypeOptional.get();
    List<Role> roles = roleRepository.findAllByIsDeletedFalseAndTypeId(roleType.getId());
    List<RoleCustomDto> listCustomDto = new ArrayList<>();
    roles.forEach(role -> listCustomDto.add(roleMapper.toRoleCustomerDto(role)));
    return listCustomDto;
  }

  @Override
  public List<RolePermissionDto> findListPermission(List<Integer> roleIds,
      List<String> objectCodes) {
    List<SysPermission> permissions = findAllSysPermissionByRoleIds(roleIds);
    List<SysPermission> permissionList = permissions.stream()
        .filter(it -> objectCodes.isEmpty() || objectCodes.contains(it.getObjectCode()))
        .collect(Collectors.toList());
    return findAllPermission(permissionList);
  }


  private List<SysPermission> findAllSysPermissionByRoleIds(List<Integer> ids) {
    List<RoleDetail> roleDetailList = roleDetailRepository.findAllByRoleIdIn(ids);
    return roleDetailList.stream()
        .filter(p -> p.getIsDeleted().equals(Boolean.FALSE))
        .map(RoleDetail::getPermission)
        .filter(p -> p.getIsDeleted() == null || p.getIsDeleted().equals(Boolean.FALSE))
        .collect(Collectors.toList());
  }

  private List<RolePermissionDto> findAllPermissionOfRoleList(List<Role> list) {
    List<Integer> ids = list.stream().map(Role::getId).collect(Collectors.toList());
    List<SysPermission> permissions = findAllSysPermissionByRoleIds(ids);
    return findAllPermission(permissions);
  }

  private List<RolePermissionDto> findAllPermission(List<SysPermission> permissions) {
    List<String> objectNames = permissions.stream()
        .map(SysPermission::getObjectCode)
        .distinct()
        .collect(Collectors.toList());
    List<RolePermissionDto> rolePermissionDtoList = new ArrayList<>();
    for (String name : objectNames) {
      RolePermissionDto rolePermissionDto = new RolePermissionDto();
      List<SysPermissionDto> permissionDtoList = new ArrayList<>();
      rolePermissionDto.setObjectCode(name);
      permissions.forEach(p -> {
        if (p.getObjectCode().equals(name)) {
          rolePermissionDto.setObjectName(p.getObjectName());
          rolePermissionDto.setService(p.getService());
          permissionDtoList.add(sysPermissionMapper.toDto(p));
        }
      });
      rolePermissionDto.setSysPermissions(permissionDtoList);
      rolePermissionDtoList.add(rolePermissionDto);
    }
    return rolePermissionDtoList;
  }

  void forceLogout(Role role) {
    List<RoleUser> roleUsers = roleUserRepository.findAllByRoleIdAndIsDeletedFalse(role.getId());
    List<RoleGroup> roleGroups = roleGroupRepository
        .findAllByRoleIdAndIsDeletedFalse(role.getId());

    List<String> userUUidList = roleUsers.stream()
        .map(RoleUser::getUser)
        .map(User::getUuid)
        .collect(Collectors.toList());
    List<Group> groups = roleGroups.stream()
        .map(RoleGroup::getGroup)
        .collect(Collectors.toList());
    List<Integer> grIdList = groups.stream()
        .map(Group::getId)
        .collect(Collectors.toList());
    // findAll User of group list
    List<GroupUser> groupUsers = groupUserRepository
        .findAllByGroupIdInAndIsDeletedFalse(grIdList);
    List<String> userGrUuidList = groupUsers.stream()
        .map(GroupUser::getUser)
        .map(User::getUuid)
        .collect(Collectors.toList());

    userUUidList.addAll(userGrUuidList);
    List<String> uuidList = new ArrayList<>(new HashSet<>(userUUidList));
    for (String id : uuidList) {
      String keyPattern = prefix + id + ":*";
      if (redisService.hasKeyPattern(keyPattern)) {
        redisService.setExpirePattern(keyPattern, 60L, TimeUnit.SECONDS);
      }
    }
  }

  @Override
  public List<RoleDto> findListRoleByListObjectCode(String objectCodeList) {
    List<String> objectCode = Arrays.stream(objectCodeList.split(","))
        .map(String::toUpperCase).collect(Collectors.toList());
    List<Integer> ids = roleDetailRepository.findRoleOnlyContainsPermissionObject(objectCode);
    List<Role> roleList = roleRepository.findAllByIsDeletedFalseAndIdIn(ids);
    return roleList.stream().map(roleMapper::toDto).collect(Collectors.toList());
  }
}
