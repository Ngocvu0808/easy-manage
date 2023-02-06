//package com.example.authservice.service.impl;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//import java.util.*;
//import java.util.concurrent.TimeUnit;
//import java.util.stream.Collectors;
//
//@Service
//public class GroupServiceImpl implements GroupService {
//
//  private final Boolean IS_DELETED = true;
//  private final Boolean DELETE = false;
//
//  private final GroupRepository groupRepository;
//  private final UserRepository userRepository;
//  private final RoleRepository roleRepository;
//  private final RoleGroupRepository roleGroupRepository;
//  private final GroupUserRepository groupUserRepository;
//
//  private final RoleMapper roleMapper;
//  private final GroupMapper groupMapper;
//  private final UserMapper userMapper;
//  private final CustomGroupDtoMapper customGroupDtoMapper;
//
//  private final CacheRedisService redisService;
//
//  @Value("${auth.code.prefix:auth:jwt:}")
//  private String prefix;
//
//  public GroupServiceImpl(GroupRepository groupRepository,
//      UserRepository userRepository,
//      RoleRepository roleRepository,
//      RoleGroupRepository roleGroupRepository,
//      GroupUserRepository groupUserRepository,
//      RoleMapper roleMapper,
//      GroupMapper groupMapper,
//      UserMapper userMapper,
//      CustomGroupDtoMapper customGroupDtoMapper,
//      CacheRedisService redisService) {
//    this.groupRepository = groupRepository;
//    this.userRepository = userRepository;
//    this.roleRepository = roleRepository;
//    this.roleGroupRepository = roleGroupRepository;
//    this.groupUserRepository = groupUserRepository;
//    this.roleMapper = roleMapper;
//    this.groupMapper = groupMapper;
//    this.userMapper = userMapper;
//    this.customGroupDtoMapper = customGroupDtoMapper;
//    this.redisService = redisService;
//  }
//
//  @Override
//  public GroupDto createGroup(GroupDto groupDto, Integer creatorUserId)
//      throws DuplicateEntityException, ResourceNotFoundException {
//    Group groupByCode = groupRepository.findByCodeAndIsDeletedFalse(groupDto.getCode());
//    if (groupByCode != null) {
//      throw new DuplicateEntityException("Code of group exists " + groupDto.getCode(),
//          ServiceInfo.getId() + AuthServiceMessageCode.GROUP_DUPLICATE);
//    }
//    groupDto.setId(null);
//    Group group = groupMapper.fromDto(groupDto);
//    setListRole(group, groupDto.getRoleIds(), creatorUserId);
//    checkAndSetCreatorUser(group, creatorUserId);
//    return groupMapper.toDto(groupRepository.save(group));
//  }
//
//  public void checkAndSetCreatorUser(Group group, Integer userId)
//      throws ResourceNotFoundException {
//    Optional<User> userOptionalById = userRepository.findById(userId);
//    if (userOptionalById.isEmpty() || userOptionalById.get().getIsDeleted().equals(Boolean.TRUE)) {
//      throw new ResourceNotFoundException("User not found " + userId,
//          ServiceInfo.getId() + AuthServiceMessageCode.USER_NOT_FOUND);
//    }
//    User user = userOptionalById.get();
//    group.setCreatorUser(user);
//  }
//
//  public Role checkAndGetRole(Integer roleId) throws ResourceNotFoundException {
//    Optional<Role> roleOptionalById = roleRepository.findById(roleId);
//    if (roleOptionalById.isEmpty() || roleOptionalById.get().getIsDeleted().equals(Boolean.TRUE)) {
//      throw new ResourceNotFoundException("Role not found " + roleId,
//          ServiceInfo.getId() + AuthServiceMessageCode.ROLE_NOT_FOUND);
//    }
//    return roleOptionalById.get();
//  }
//
//  public void setListRole(Group group, Set<Integer> roleIds, Integer userId)
//      throws ResourceNotFoundException {
//    Set<RoleGroup> roleGroups = new HashSet<>();
//    for (Integer roleId : roleIds) {
//      RoleGroup roleGroup = new RoleGroup();
//      roleGroup.setRole(checkAndGetRole(roleId));
//      roleGroup.setGroup(group);
//      roleGroups.add(roleGroup);
//    }
//    group.setRoles(new ArrayList<>(roleGroups));
//  }
//
//  @Override
//  public GroupDto updateGroupDto(GroupDto groupDto, Integer updaterUserId)
//      throws ResourceNotFoundException, IdentifyBlankException, OperationNotImplementException {
//    if (groupDto.getId() == null) {
//      throw new IdentifyBlankException("Id Blank " + groupDto.getId(),
//          ServiceInfo.getId() + AuthServiceMessageCode.ID_NOT_NULL);
//    }
//    Optional<Group> groupOptionalById = groupRepository
//        .findById(groupDto.getId());
//
//    if (groupOptionalById.isEmpty() || groupOptionalById.get().getIsDeleted()
//        .equals(Boolean.TRUE)) {
//      throw new ResourceNotFoundException("Group not found " + groupDto.getId(),
//          ServiceInfo.getId() + AuthServiceMessageCode.GROUP_NOT_FOUND);
//    }
//    Group group = groupOptionalById.get();
//
//    if (!group.getCode().equals(groupDto.getCode())) {
//      throw new OperationNotImplementException(
//          "Don't change value of code-group " + groupDto.getCode(),
//          ServiceInfo.getId() + AuthServiceMessageCode.GROUP_CODE_CANNOT_MODIFY);
//    }
//    groupMapper.updateModel(group, groupDto);
//    group.setUpdaterUserId(updaterUserId);
//    for (Integer roleId : groupDto.getRoleIds()) {
//      checkAndGetRole(roleId);
//    }
//    setListRole(group, checkDuplicateAndSetNewRoleIdForGroup(group, groupDto.getRoleIds()),
//        updaterUserId);
//    Group res = groupRepository.save(group);
//    // force logout all user of group
////    this.forceLogout(res);
//    return groupMapper.toDto(res);
//  }
//
//  public Set<Integer> checkDuplicateAndSetNewRoleIdForGroup(Group group, Set<Integer> idRoles) {
//    List<RoleGroup> roleGroups = roleGroupRepository
//        .findByGroupIdAndIsDeletedFalse(group.getId());
//    Set<Integer> listRoleIdByGroup = roleGroups.stream().map(RoleGroup::getRoleId)
//        .collect(Collectors.toSet());
//
//    Set<Integer> listNewRoleIdNeedAddForGroup = new HashSet<>();
//    Set<Integer> listRoleIdNotUpdate = new HashSet<>();
//    Set<Integer> listRoleIdNeedDelete = new HashSet<>();
//    //extract idRole need add
//    for (Integer idRoleGroup : idRoles) {
//      if (!listRoleIdByGroup.contains(idRoleGroup)) {
//        listNewRoleIdNeedAddForGroup.add(idRoleGroup);
//      } else {
//        listRoleIdNotUpdate.add(idRoleGroup);//extract idRole not update
//      }
//    }
//    // extract idRole need delete
//    for (Integer idRoleGroup : listRoleIdByGroup) {
//      if (!listRoleIdNotUpdate.contains(idRoleGroup)) {
//        listRoleIdNeedDelete.add(idRoleGroup);
//      }
//    }
//    //delete groupRole by role id
//    List<RoleGroup> listRoleGroupNeedDelete = new ArrayList<>();
//    for (Integer roleGroupId : listRoleIdNeedDelete) {
//      List<RoleGroup> marRoleGroups = roleGroupRepository
//          .findByGroupIdAndRoleIdAndIsDeletedFalse(group.getId(), roleGroupId);
//      marRoleGroups.get(0).setIsDeleted(IS_DELETED);
//      listRoleGroupNeedDelete.add(marRoleGroups.get(0));
//
//    }
//    roleGroupRepository.saveAll(listRoleGroupNeedDelete);
//    return listNewRoleIdNeedAddForGroup;
//  }
//
//  void forceLogout(Group group) {
//    List<GroupUser> groupUserList = group.getUsers();
//    List<User> users = groupUserList.stream().map(GroupUser::getUser)
//        .collect(Collectors.toList());
//    List<String> uuidList = users.stream().map(User::getUuid).collect(Collectors.toList());
//    this.setExpireToken(uuidList);
//  }
//
//  void reloadPermission(List<Integer> userIdList) {
//    List<User> userList = userRepository.findAllById(userIdList);
//    List<String> uuidList = userList.stream().map(User::getUuid).collect(Collectors.toList());
//    this.setExpireToken(uuidList);
//  }
//
//  void setExpireToken(List<String> uuidList) {
//    for (String id : uuidList) {
//      String keyPattern = prefix + id + ":*";
//      if (redisService.hasKeyPattern(keyPattern)) {
//        redisService.setExpirePattern(keyPattern, 60L, TimeUnit.SECONDS);
//      }
//    }
//  }
//
//  @Override
//  public GroupDto findById(Integer groupId)
//      throws ResourceNotFoundException, IdentifyBlankException {
//    if (groupId == null) {
//      throw new IdentifyBlankException("Id Blank " + groupId,
//          ServiceInfo.getId() + AuthServiceMessageCode.ID_NOT_NULL);
//    }
//
//    Optional<Group> groupOptionalById = groupRepository
//        .findById(groupId);
//    if (groupOptionalById.isEmpty() || groupOptionalById.get().getIsDeleted()
//        .equals(Boolean.TRUE)) {
//      throw new ResourceNotFoundException("Group not found " + groupId,
//          ServiceInfo.getId() + AuthServiceMessageCode.GROUP_NOT_FOUND);
//    }
//    Group group = groupOptionalById.get();
//    List<RoleGroup> listRoleGroup = group.getRoles().stream()
//        .filter(roleGroup -> !roleGroup.getIsDeleted() && !roleGroup.getRole().getIsDeleted())
//        .collect(Collectors.toList());
//
//    List<GroupUser> listUser = group.getUsers().stream()
//        .filter(groupUser -> !groupUser.getIsDeleted()
//            && !groupUser.getUser().getIsDeleted()
//            && groupUser.getUser().getStatus().equals(UserStatus.ACTIVE))
//        .collect(Collectors.toList());
//
//    group.setRoles(listRoleGroup);
//    group.setUsers(listUser);
//
//    GroupDto groupDto = groupMapper.toDto(group);
//
//    countNumberUserOfGroup(groupDto);
//
//    return groupDto;
//  }
//
//  @Override
//  public Boolean deleteGroupById(Integer groupId, Integer deleterUserId)
//      throws IdentifyBlankException, ResourceNotFoundException, OperationNotImplementException {
//    if (groupId == null) {
//      throw new IdentifyBlankException("Id Blank " + groupId,
//          ServiceInfo.getId() + AuthServiceMessageCode.ID_NOT_NULL);
//    }
//    Optional<Group> groupOptionalById = groupRepository
//        .findById(groupId);
//    if (groupOptionalById.isEmpty()) {
//      throw new ResourceNotFoundException("Group not found " + groupId,
//          ServiceInfo.getId() + AuthServiceMessageCode.GROUP_NOT_FOUND);
//    }
//    Group group = groupOptionalById.get();
//    if (group.getIsDeleted().equals(DELETE)) {
//      group.setIsDeleted(IS_DELETED);
//      group.setDeleterUserId(deleterUserId);
//      groupRepository.save(group);
////      this.forceLogout(group);
//      return true;
//    } else {
//      throw new OperationNotImplementException("Group not found " + groupId,
//          ServiceInfo.getId() + AuthServiceMessageCode.GROUP_NOT_FOUND);
//    }
//  }
//
//  @Override
//  public GroupDto addUserToGroup(GroupCustomDto groupCustomDto, Integer updaterUserId)
//      throws IdentifyBlankException, ResourceNotFoundException, DuplicateEntityException {
//    if (groupCustomDto.getId() == null) {
//      throw new IdentifyBlankException("Id Blank " + groupCustomDto.getId());
//    }
//    Optional<Group> groupOptionalById = groupRepository
//        .findById(groupCustomDto.getId());
//
//    if (groupOptionalById.isEmpty() || groupOptionalById.get().getIsDeleted()
//        .equals(Boolean.TRUE)) {
//      throw new ResourceNotFoundException("Group not found " + groupCustomDto.getId(),
//          ServiceInfo.getId() + AuthServiceMessageCode.GROUP_NOT_FOUND);
//    }
//
//    Group group = groupOptionalById.get();
//    checkAndSetValidUser(groupCustomDto.getListUserId(), group);
//    group.setUpdaterUserId(updaterUserId);
//    GroupDto groupDto = groupMapper.toDto(groupRepository.save(group));
//    // force logout user of list
//    this.reloadPermission(new ArrayList<>(groupCustomDto.getListUserId()));
//    return groupDto;
//  }
//
//  @Override
//  public GroupDto updateUserToGroup(GroupCustomDto groupCustomDto, Integer updateUserId)
//      throws IdentifyBlankException, ResourceNotFoundException {
//    if (groupCustomDto.getId() == null) {
//      throw new IdentifyBlankException("Id Blank " + groupCustomDto.getId(),
//          ServiceInfo.getId() + AuthServiceMessageCode.ID_NOT_NULL);
//    }
//    Optional<Group> groupOptionalById = groupRepository
//        .findById(groupCustomDto.getId());
//
//    if (groupOptionalById.isEmpty() || groupOptionalById.get().getIsDeleted()
//        .equals(Boolean.TRUE)) {
//      throw new ResourceNotFoundException("Group not found " + groupCustomDto.getId(),
//          ServiceInfo.getId() + AuthServiceMessageCode.GROUP_NOT_FOUND);
//    }
//
//    Group group = groupOptionalById.get();
//    Set<Integer> listUserId = groupCustomDto.getListUserId();
//    for (Integer userId : listUserId) {
//      checkAndGetUser(userId);
//    }
//    group.setUpdaterUserId(updateUserId);
//    setListUser(group,
//        checkDuplicateAndSetNewUserIdForGroup(group, groupCustomDto.getListUserId()));
//    group.setUpdaterUserId(updateUserId);
//    GroupDto groupDto = groupMapper.toDto(groupRepository.save(group));
//    // force logout user after add to group
//    this.reloadPermission(new ArrayList<>(groupCustomDto.getListUserId()));
//    return groupDto;
//  }
//
//  @Override
//  public Boolean deleteUserFromGroup(Integer groupId, Integer userId, Integer deleterUserId)
//      throws IdentifyBlankException, ResourceNotFoundException, OperationNotImplementException {
//    if (groupId == null) {
//      throw new IdentifyBlankException("Id Blank " + groupId,
//          ServiceInfo.getId() + AuthServiceMessageCode.ID_NOT_NULL);
//    }
//    Optional<Group> groupOptionalById = groupRepository
//        .findById(groupId);
//    if (groupOptionalById.isEmpty()) {
//      throw new ResourceNotFoundException("Group not found " + groupId,
//          ServiceInfo.getId() + AuthServiceMessageCode.GROUP_NOT_FOUND);
//    }
//    Group group = groupOptionalById.get();
//    if (group.getIsDeleted().equals(DELETE)) {
//      List<GroupUser> groupUsers = groupUserRepository
//          .findByUserIdAndGroupIdAndIsDeletedFalse(userId, groupId);
//      if (groupUsers.isEmpty()) {
//        throw new ResourceNotFoundException("User not exists in group " + userId,
//            ServiceInfo.getId() + AuthServiceMessageCode.USER_NOT_EXIST_IN_GROUP);
//      }
//      groupUsers.get(0).setIsDeleted(IS_DELETED);
//      groupUsers.get(0).setDeleterUserId(deleterUserId);
//      groupUserRepository.save(groupUsers.get(0));
//      // force logout user
//      this.reloadPermission(Collections.singletonList(userId));
//      return true;
//    } else {
//      throw new OperationNotImplementException("Group not found " + groupId,
//          ServiceInfo.getId() + AuthServiceMessageCode.GROUP_NOT_FOUND);
//    }
//  }
//
//  @Override
//  public Boolean deleteRoleFromGroup(Integer groupId, Integer roleId, Integer deleterUserId)
//      throws IdentifyBlankException, ResourceNotFoundException, OperationNotImplementException {
//    if (groupId == null) {
//      throw new IdentifyBlankException("Id Blank " + groupId,
//          ServiceInfo.getId() + AuthServiceMessageCode.ID_NOT_NULL);
//    }
//    Optional<Group> groupOptionalById = groupRepository
//        .findById(groupId);
//    if (groupOptionalById.isEmpty()) {
//      throw new ResourceNotFoundException("Group not found " + groupId,
//          ServiceInfo.getId() + AuthServiceMessageCode.GROUP_NOT_FOUND);
//    }
//    Group group = groupOptionalById.get();
//    if (group.getIsDeleted().equals(DELETE)) {
//      List<RoleGroup> groupRoles = roleGroupRepository
//          .findByGroupIdAndRoleIdAndIsDeletedFalse(groupId, roleId);
//      if (groupRoles.isEmpty()) {
//        throw new ResourceNotFoundException("Role not exists in group " + roleId,
//            ServiceInfo.getId() + AuthServiceMessageCode.ROLE_NOT_EXIST_IN_GROUP);
//      }
//      groupRoles.get(0).setIsDeleted(IS_DELETED);
//      groupRoles.get(0).setDeleterUserId(deleterUserId);
//      roleGroupRepository.save(groupRoles.get(0));
//      // force logout all user of group
////      this.forceLogout(group);
//      return true;
//    } else {
//      throw new OperationNotImplementException("Group not found " + groupId,
//          ServiceInfo.getId() + AuthServiceMessageCode.GROUP_NOT_FOUND);
//    }
//  }
//
//  public Set<Integer> checkDuplicateAndSetNewUserIdForGroup(Group group, Set<Integer> userIds) {
//    List<GroupUser> userGroups = groupUserRepository
//        .findByGroupIdAndIsDeletedFalse(group.getId());
//    Set<Integer> listUserIdByGroup = userGroups.stream()
//        .map(GroupUser::getUserId).collect(Collectors.toSet());
//
//    Set<Integer> listNewUserIdNeedAddForGroup = new HashSet<>();
//    Set<Integer> listUserIdNotUpdate = new HashSet<>();
//    Set<Integer> listUserIdNeedDeleted = new HashSet<>();
//    //extract idRole need add
//    for (Integer idUserGroup : userIds) {
//      if (!listUserIdByGroup.contains(idUserGroup)) {
//        listNewUserIdNeedAddForGroup.add(idUserGroup);
//      } else {
//        listUserIdNotUpdate.add(idUserGroup);//extract idRole not update
//      }
//    }
//    // extract idUser need delete
//    for (Integer idUserGroup : listUserIdByGroup) {
//      if (!listUserIdNotUpdate.contains(idUserGroup)) {
//        listUserIdNeedDeleted.add(idUserGroup);
//      }
//    }
//    //delete groupUser by user id
//    List<GroupUser> listUserGroupNeedDelete = new ArrayList<>();
//    for (Integer userGroupId : listUserIdNeedDeleted) {
//      List<GroupUser> marUserGroup = groupUserRepository
//          .findByUserIdAndGroupIdAndIsDeletedFalse(userGroupId, group.getId());
//      marUserGroup.get(0).setIsDeleted(IS_DELETED);
//      listUserGroupNeedDelete.add(marUserGroup.get(0));
//
//    }
//    groupUserRepository.saveAll(listUserGroupNeedDelete);
//    return listNewUserIdNeedAddForGroup;
//  }
//
//  public User checkAndGetUser(Integer userId) throws ResourceNotFoundException {
//    Optional<User> userOptionalById = userRepository.findById(userId);
//    if (userOptionalById.isEmpty() || userOptionalById.get().getIsDeleted().equals(Boolean.TRUE)) {
//      throw new ResourceNotFoundException("User not found " + userId,
//          ServiceInfo.getId() + AuthServiceMessageCode.USER_NOT_FOUND);
//    }
//    return userOptionalById.get();
//  }
//
//  public void setListUser(Group group, Set<Integer> userIds) throws ResourceNotFoundException {
//    Set<GroupUser> users = new HashSet<>();
//    for (Integer userId : userIds) {
//      GroupUser marUserGroup = new GroupUser();
//      marUserGroup.setUser(checkAndGetUser(userId));
//      marUserGroup.setGroup(group);
//      users.add(marUserGroup);
//    }
//    group.setUsers(new ArrayList<>(users));
//  }
//
//
//  @Override
//  public GroupDto updateRoleToGroup(GroupCustomDto groupCustomDto, Integer updaterUserId)
//      throws IdentifyBlankException, ResourceNotFoundException {
//    if (groupCustomDto.getId() == null) {
//      throw new IdentifyBlankException("Id Blank " + groupCustomDto.getId(),
//          ServiceInfo.getId() + AuthServiceMessageCode.ID_NOT_NULL);
//    }
//    Optional<Group> groupOptionalById = groupRepository
//        .findById(groupCustomDto.getId());
//
//    if (groupOptionalById.isEmpty()) {
//      throw new ResourceNotFoundException("Group not found " + groupCustomDto.getId(),
//          ServiceInfo.getId() + AuthServiceMessageCode.GROUP_NOT_FOUND);
//    }
//
//    Group group = groupOptionalById.get();
//    if (group.getIsDeleted().equals(IS_DELETED)) {
//      throw new ResourceNotFoundException("Group not found " + groupCustomDto.getId(),
//          ServiceInfo.getId() + AuthServiceMessageCode.GROUP_NOT_FOUND);
//    }
//
//    group.setUpdaterUserId(updaterUserId);
//    for (Integer roleId : groupCustomDto.getListRoleId()) {
//      checkAndGetRole(roleId);
//    }
//    setListRole(group,
//        checkDuplicateAndSetNewRoleIdForGroup(group, groupCustomDto.getListRoleId()),
//        updaterUserId);
//    Group res = groupRepository.save(group);
//    // force logout all user of group
////    this.forceLogout(res);
//    return groupMapper.toDto(res);
//  }
//
//  @Override
//  public List<UserDto> findAllUserFromGroup(Integer groupId)
//      throws IdentifyBlankException, ResourceNotFoundException {
//    if (groupId == null) {
//      throw new IdentifyBlankException("Id Blank " + groupId,
//          ServiceInfo.getId() + AuthServiceMessageCode.ID_NOT_NULL);
//    }
//
//    Optional<Group> groupOptionalById = groupRepository.findById(groupId);
//    if (groupOptionalById.isEmpty() || groupOptionalById.get().getIsDeleted()
//        .equals(Boolean.TRUE)) {
//      throw new ResourceNotFoundException("Group not found " + groupId,
//          ServiceInfo.getId() + AuthServiceMessageCode.GROUP_NOT_FOUND);
//    }
//    Group group = groupOptionalById.get();
//    List<GroupUser> listGroupUser = group.getUsers().stream()
//        .filter(groupUser -> !groupUser.getIsDeleted() && !groupUser.getUser().getIsDeleted())
//        .collect(Collectors.toList());
//
//    return listGroupUser.stream()
//        .map(groupUser -> userMapper.toDto(groupUser.getUser())).collect(
//            Collectors.toList());
//  }
//
//  @Override
//  public List<RoleDto> findAllRoleFromGroup(Integer groupId)
//      throws IdentifyBlankException, ResourceNotFoundException {
//    if (groupId == null) {
//      throw new IdentifyBlankException("Id Blank " + groupId,
//          ServiceInfo.getId() + AuthServiceMessageCode.ID_NOT_NULL);
//    }
//
//    Optional<Group> groupOptionalById = groupRepository
//        .findById(groupId);
//    if (groupOptionalById.isEmpty() || groupOptionalById.get().getIsDeleted()
//        .equals(Boolean.TRUE)) {
//      throw new ResourceNotFoundException("Group not found " + groupId,
//          ServiceInfo.getId() + AuthServiceMessageCode.GROUP_NOT_FOUND);
//    }
//    Group group = groupOptionalById.get();
//
//    List<RoleGroup> listRoleGroup = group.getRoles().stream()
//        .filter(groupUser -> !groupUser.getIsDeleted() && !groupUser.getRole().getIsDeleted())
//        .collect(Collectors.toList());
//
//    return listRoleGroup.stream()
//        .map(groupRole -> roleMapper.toDto(groupRole.getRole())).collect(Collectors.toList());
//  }
//
//  @Override
//  public GroupDto addRoleToGroup(GroupCustomDto groupCustomDto, Integer updaterUserId)
//      throws IdentifyBlankException, ResourceNotFoundException, DuplicateEntityException {
//    if (groupCustomDto.getId() == null) {
//      throw new IdentifyBlankException("Id Blank " + groupCustomDto.getId(),
//          ServiceInfo.getId() + AuthServiceMessageCode.ID_NOT_NULL);
//    }
//    Optional<Group> groupOptionalById = groupRepository
//        .findById(groupCustomDto.getId());
//
//    if (groupOptionalById.isEmpty() || groupOptionalById.get().getIsDeleted()
//        .equals(Boolean.TRUE)) {
//      throw new ResourceNotFoundException("Group not found " + groupCustomDto.getId(),
//          ServiceInfo.getId() + AuthServiceMessageCode.GROUP_NOT_FOUND);
//    }
//
//    Group group = groupOptionalById.get();
//    checkAndSetValidRole(groupCustomDto.getListRoleId(), group);
//    group.setUpdaterUserId(updaterUserId);
//    Group res = groupRepository.save(group);
//    // force logout all user of group
////    this.forceLogout(res);
//    return groupMapper.toDto(res);
//  }
//
//  @Override
//  public List<RoleCustomDto> getAllRoleAssignedInGroup() {
//    List<RoleGroup> listRoleGroup = roleGroupRepository.findAllByIsDeletedFalse();
//    Set<Integer> listRoleId = listRoleGroup.stream().map(RoleGroup::getRoleId)
//        .collect(Collectors.toSet());
//    List<Role> listRole = roleRepository
//        .findAllByIsDeletedFalseAndIdIn(new ArrayList<>(listRoleId));
//    List<RoleCustomDto> roleGroupDtos = new ArrayList<>();
//    return listRole.stream().map(roleMapper::toRoleCustomerDto).collect(Collectors.toList());
//  }
//
//  public void checkAndSetValidRole(Set<Integer> listRoleId, Group group)
//      throws ResourceNotFoundException, DuplicateEntityException {
//    List<RoleGroup> listRoleGroup = new ArrayList<>();
//    for (Integer roleId : listRoleId) {
//      Optional<Role> roleOptionalById = roleRepository.findById(roleId);
//      if (roleOptionalById.isEmpty() || roleOptionalById.get().getIsDeleted()
//          .equals(Boolean.TRUE)) {
//        throw new ResourceNotFoundException("Role not found " + roleId,
//            ServiceInfo.getId() + AuthServiceMessageCode.ROLE_NOT_FOUND);
//      }
//      Role role = roleOptionalById.get();
//      List<RoleGroup> listGroupRole = roleGroupRepository
//          .findByGroupIdAndRoleIdAndIsDeletedFalse(group.getId(), roleId);
//
//      // if user exists in group then throw exception.
//      if (!listGroupRole.isEmpty()) {
//        throw new DuplicateEntityException("Role exists in group " + roleId,
//            ServiceInfo.getId() + AuthServiceMessageCode.ROLE_EXIST_IN_GROUP);
//      }
//      // if user not exists then create new groupUser.
//      RoleGroup roleGroup = new RoleGroup();
//      roleGroup.setGroup(group);
//      roleGroup.setRole(role);
//      listRoleGroup.add(roleGroup);
//    }
//    group.setRoles(listRoleGroup);
//  }
//
//  public void checkAndSetValidUser(Set<Integer> listUserId, Group group)
//      throws ResourceNotFoundException, DuplicateEntityException {
//    List<GroupUser> listUserGroup = new ArrayList<>();
//    for (Integer userId : listUserId) {
//      Optional<User> userOptionalById = userRepository.findById(userId);
//      if (userOptionalById.isEmpty() || userOptionalById.get().getIsDeleted()
//          .equals(Boolean.TRUE)) {
//        throw new ResourceNotFoundException("User not found " + userId,
//            ServiceInfo.getId() + AuthServiceMessageCode.USER_NOT_FOUND);
//      }
//      User user = userOptionalById.get();
//
//      List<GroupUser> listGroupUser = groupUserRepository
//          .findByGroupIdAndUserIdAndIsDeletedFalse(group.getId(), userId);
//
//      // if user exists in group then throw exception.
//      if (!listGroupUser.isEmpty()) {
//        throw new DuplicateEntityException("User exists in group " + userId,
//            ServiceInfo.getId() + AuthServiceMessageCode.USER_EXIST_IN_GROUP);
//      }
//
//      // if user not exists then create new groupUser.
//      GroupUser groupUser = new GroupUser();
//      groupUser.setGroup(group);
//      groupUser.setUser(user);
//      listUserGroup.add(groupUser);
//    }
//    group.setUsers(listUserGroup);
//  }
//
//  public void countNumberUserOfGroup(GroupDto groupDto) {
//    groupDto.setNumberMember(
//        groupDto.getUsers().size());
//  }
//
//  public void countNumberUserOfListGroup(List<GroupDto> listGroupDto) {
//    listGroupDto.forEach(this::countNumberUserOfGroup);
//  }
//
//  @Override
//  public DataPagingResponse<GroupDto> findAll(String search, String role, String sort,
//      Boolean isDeleted, Pageable pageable) {
//    List<Integer> listRole = new ArrayList<>();
//    if (role != null && !role.isEmpty()) {
//      String[] campaignArrays = role.trim().split(",");
//      for (String campaignArray : campaignArrays) {
//        listRole.add(Integer.parseInt(campaignArray));
//      }
//    }
//    Map<String, String> map = SortingUtils.detectSortType(sort);
//    Page<Group> groupPages = groupRepository
//        .findAll(new GroupFilter().getByFilter(search, listRole, map, isDeleted), pageable);
//    List<Group> groups = groupPages.getContent();
//    for (Group group : groups) {
//      List<RoleGroup> listRoleStandard = group.getRoles().stream()
//          .filter(roleGroup -> !roleGroup.getIsDeleted() && !roleGroup.getRole().getIsDeleted())
//          .collect(Collectors.toList());
//      group.setRoles(listRoleStandard);
//
//      List<GroupUser> listUserStandard = group.getUsers().stream()
//          .filter(groupUser -> !groupUser.getIsDeleted() && !groupUser.getUser().getIsDeleted()
//              && groupUser.getUser().getStatus().equals(UserStatus.ACTIVE))
//          .collect(Collectors.toList());
//      group.setUsers(listUserStandard);
//    }
//
//    List<GroupDto> groupDtos = groups.stream().map(groupMapper::toDto).collect(Collectors.toList());
//    countNumberUserOfListGroup(groupDtos);
//    DataPagingResponse<GroupDto> dataPagingResponses = new DataPagingResponse<>();
//    dataPagingResponses.setList(groupDtos);
//    dataPagingResponses.setTotalPage(groupPages.getTotalPages());
//    dataPagingResponses.setNum(groupPages.getTotalElements());
//    dataPagingResponses.setCurrentPage(pageable.getPageNumber() + 1L);
//    return dataPagingResponses;
//  }
//
//  @Override
//  public List<CustomGroupDto> getAllGroup() {
//    List<Group> listGroupDto = groupRepository.findAllByIsDeletedFalse();
//    return listGroupDto.stream().map(customGroupDtoMapper::toDto)
//        .collect(Collectors.toList());
//  }
//}
