package com.example.authservice.service.impl;


import com.example.authservice.config.GenerateUniqueKey;
import com.example.authservice.dto.SysPermissionDto;
import com.example.authservice.dto.auth.*;
import com.example.authservice.dto.filter.UserStatusDto;
import com.example.authservice.dto.group.GroupAndUserDto;
import com.example.authservice.dto.group.GroupUserCustomDto;
import com.example.authservice.dto.group.TypeUserGroup;
import com.example.authservice.dto.role.RoleCustomDto;
import com.example.authservice.dto.role.RoleDto;
import com.example.authservice.dto.user.GetCustomerResponse;
import com.example.authservice.dto.user.RegisterCustomerRequest;
import com.example.authservice.dto.user.UpdateCustomerRequest;
import com.example.authservice.dto.user.UserActivityResponse;
import com.example.authservice.dto.user.UserDto;
import com.example.authservice.entities.*;
import com.example.authservice.entities.role.Role;
import com.example.authservice.entities.role.RoleObject;
import com.example.authservice.entities.user.CustomerBalance;
import com.example.authservice.entities.user.User;
import com.example.authservice.exception.AuthServiceMessageCode;
import com.example.authservice.filter.UserAcvitityFilter;
import com.example.authservice.filter.UserFilter;
import com.example.authservice.mapper.GroupUserMapper;
import com.example.authservice.mapper.RoleMapper;
import com.example.authservice.mapper.RoleUserMapper;
import com.example.authservice.mapper.UserMapper;
import com.example.authservice.repo.*;
import com.example.authservice.service.iface.CryptoService;
import com.example.authservice.service.iface.RoleService;
import com.example.authservice.service.iface.UserService;
import com.example.authservice.utils.DateUtil;
import com.example.authservice.utils.ServiceInfo;
import com.example.authservice.utils.SortingUtils;
import com.example.authservice.utils.cache.CacheRedisService;
import com.example.authservice.utils.exception.*;
import com.example.authservice.utils.permission.ObjectPermission;
import com.example.authservice.utils.permission.Permission;
import com.example.authservice.utils.permission.SpecificPermission;
import com.example.authservice.utils.response.DataPagingResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import org.springframework.util.StringUtils;

@Service
public class UserServiceImpl implements UserService {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(UserService.class);

  private static final String FILTER_USER = "user";
  private static final String FILTER_GROUP = "group";
  private final String PATTERN = "yyyy-MM-dd";
  private final String PATTERN2 = "yyyy-MM-dd HH:mm:ss";

  private final SimpleDateFormat df = new SimpleDateFormat(PATTERN);
  private final SimpleDateFormat df2 = new SimpleDateFormat(PATTERN2);

  @Value("${auth.default-pass}")
  private String defaultPassword;

  @Value("${auth.crypto.private-key}")
  private String privateKey;
  @Value("${auth.crypto.public-key}")
  private String publicKey;

  @Value("${auth.code.prefix:auth:jwt:}")
  private String prefix;

  private final UserRepository userRepository;
  private final RoleUserRepository roleUserRepository;
  private final RoleGroupRepository roleGroupRepository;
  private final RoleRepository roleRepository;
  private final GroupRepository groupRepository;
  private final GroupUserRepository groupUserRepository;
  private final RoleObjectRepository roleObjectRepository;
  private final UserActivityRepository userActivityRepository;
  private final CustomerBalanceRepository customerBalanceRepository;
  private final UserMapper userMapper;
  private final RoleMapper roleMapper;
  private final GroupUserMapper groupUserMapper;
  private final RoleUserMapper roleUserMapper;
  private final PasswordEncoder passwordEncoder;

  private final RoleService roleService;
  private final CryptoService cryptoService;
  private final CacheRedisService redisService;


  public UserServiceImpl(UserRepository userRepository,
      RoleUserRepository roleUserRepository,
      RoleGroupRepository roleGroupRepository,
      RoleRepository roleRepository,
      GroupRepository groupRepository,
      GroupUserRepository groupUserRepository,
      RoleObjectRepository roleObjectRepository,
      UserActivityRepository userActivityRepository,
      CustomerBalanceRepository customerBalanceRepository, UserMapper userMapper,
      RoleMapper roleMapper,
      GroupUserMapper groupUserMapper,
      RoleUserMapper roleUserMapper,
      PasswordEncoder passwordEncoder,
      RoleService roleService,
      CryptoService cryptoService, CacheRedisService redisService) {
    this.userRepository = userRepository;
    this.roleUserRepository = roleUserRepository;
    this.roleGroupRepository = roleGroupRepository;
    this.roleRepository = roleRepository;
    this.groupRepository = groupRepository;
    this.groupUserRepository = groupUserRepository;
    this.roleObjectRepository = roleObjectRepository;
    this.userActivityRepository = userActivityRepository;
    this.customerBalanceRepository = customerBalanceRepository;
    this.userMapper = userMapper;
    this.roleMapper = roleMapper;
    this.groupUserMapper = groupUserMapper;
    this.roleUserMapper = roleUserMapper;
    this.passwordEncoder = passwordEncoder;
    this.roleService = roleService;
    this.cryptoService = cryptoService;
    this.redisService = redisService;
  }

  /**
   * Tạo mới user
   *
   * @param request
   * @param userId
   * @return
   * @throws DuplicateEntityException
   */
  @Override
  public UserDto createUser(RegisterRequestDto request, Integer userId)
      throws DuplicateEntityException, IdentifyBlankException {
    if (request.getUsername() == null || request.getUsername().isBlank()) {
      throw new IdentifyBlankException("Username not null",
          ServiceInfo.getId() + AuthServiceMessageCode.USERNAME_NOT_NULL);
    }
    if (request.getEmail() == null || request.getEmail().isBlank()) {
      throw new IdentifyBlankException("Email not null",
          ServiceInfo.getId() + AuthServiceMessageCode.EMAIL_NOT_NULL);
    }
    List<User> optional = userRepository.findByUsername(request.getUsername());
    if (!optional.isEmpty()) {
      throw new DuplicateEntityException("Username already exist",
          ServiceInfo.getId() + AuthServiceMessageCode.USER_ALREADY_EXIST);
    }
    List<User> userOptional = userRepository.findByEmail(request.getEmail());
    if (!userOptional.isEmpty()) {
      throw new DuplicateEntityException("Email already exist",
          ServiceInfo.getId() + AuthServiceMessageCode.EMAIL_ALREADY_EXIST);
    }
    UUID uuid = UUID.randomUUID();
    User marUser = new User();
    marUser.setUuid(uuid.toString());
    marUser.setCreatorUserId(userId);
    marUser.setFirstName(request.getFirstName());
    marUser.setLastName(request.getLastName());
    marUser.setName(request.getFirstName() + " " + request.getLastName());
    marUser.setUsername(request.getUsername());
    marUser.setEmail(request.getEmail());
    marUser.setStatus(UserStatus.ACTIVE);
    marUser.setPassword(passwordEncoder.encode(defaultPassword));
    if (request.getInternal() != null && request.getInternal()) {
      marUser.setIsUserInternal(Boolean.TRUE);
      marUser.setSecretKey(new GenerateUniqueKey(UUID.randomUUID().toString(), 12)
          .encode(userRepository.count()));
      marUser.setPassword(null);
    }
    User user = userRepository.save(marUser);
    saveRoleUser(request.getRoles(), user, userId);
    return userMapper.toDto(user);
  }

  public void deleteUser(Integer userId, Integer deleterId) throws ResourceNotFoundException {
    Optional<User> optional = userRepository.findById(userId);
    if (optional.isEmpty() || optional.get().getIsDeleted().equals(Boolean.TRUE)) {
      throw new ResourceNotFoundException("User does not exist",
          ServiceInfo.getId() + AuthServiceMessageCode.USER_NOT_EXIST);
    }
    User user = optional.get();
    user.setIsDeleted(Boolean.TRUE);
    user.setDeleterUserId(deleterId);
    userRepository.save(user);
    this.forceLogout(Collections.singletonList(userId));
  }

  @Override
  public UserDto updateStatusUser(Integer userId, Integer updaterId, UserStatus status)
      throws ResourceNotFoundException {
    Optional<User> optional = userRepository.findById(userId);
    if (optional.isEmpty() || optional.get().getIsDeleted().equals(Boolean.TRUE)) {
      throw new ResourceNotFoundException("User does not exist",
          ServiceInfo.getId() + AuthServiceMessageCode.USER_NOT_EXIST);
    }
    User user = optional.get();
    user.setStatus(status);
    user.setUpdaterUserId(updaterId);
    if (status.equals(UserStatus.DEACTIVE)) {
      this.forceLogout(Collections.singletonList(userId));
    }
    return userMapper.toDto(userRepository.save(user));
  }

  @Override
  public List<RoleDto> getRolesUser(Integer userId) {
    List<RoleUser> roleUserList = roleUserRepository.findAllByUserId(userId);
    List<GroupUser> groupUserList = groupUserRepository.findByUserIdAndIsDeletedFalse(userId);
    List<Integer> groupIdList = groupUserList.stream()
        .filter(gr -> gr.getUser().getIsDeleted().equals(Boolean.FALSE))
        .map(GroupUser::getGroupId).collect(Collectors.toList());
    List<RoleGroup> roleGroupList = roleGroupRepository
        .findAllByIsDeletedFalseAndGroupIdIn(groupIdList);
    List<Integer> groupRoleIds = roleGroupList.stream().map(RoleGroup::getRoleId).collect(
        Collectors.toList());
    List<Integer> roleIds = roleUserList.stream()
        .filter(marRoleUser -> marRoleUser.getIsDeleted().equals(Boolean.FALSE))
        .map(RoleUser::getRoleId).collect(Collectors.toList());
    roleIds.addAll(groupRoleIds);
    List<Role> roles = roleRepository.findAllByIdIn(roleIds);
    return roles.stream().map(roleMapper::toDto).collect(Collectors.toList());
  }

  @Override
  public void addRoleUser(Integer userId, Integer creatorId, List<Integer> roleIds)
      throws ResourceNotFoundException, DuplicateEntityException {
    Optional<User> optionalUser = userRepository.findById(userId);
    if (optionalUser.isEmpty() || optionalUser.get().getIsDeleted().equals(Boolean.TRUE)) {
      throw new ResourceNotFoundException("User doesn't exist",
          ServiceInfo.getId() + AuthServiceMessageCode.USER_NOT_EXIST);
    }
    List<RoleUser> roles = new ArrayList<>();
    for (Integer role : roleIds) {
      Optional<Role> roleOptional = roleRepository.findById(role);
      if (roleOptional.isEmpty() || roleOptional.get().getIsDeleted().equals(Boolean.TRUE)) {
        throw new ResourceNotFoundException("Role does not exist",
            ServiceInfo.getId() + AuthServiceMessageCode.ROLE_NOT_EXIST);
      }
      Optional<RoleUser> existedRoleUser = roleUserRepository
          .findByRoleIdAndUserIdAndIsDeletedIsFalse(role, userId);
      if (existedRoleUser.isPresent()) {
        throw new DuplicateEntityException("Role already exist",
            ServiceInfo.getId() + AuthServiceMessageCode.ROLE_ALREADY_EXIST);
      }
      Optional<User> userOptional = userRepository.findById(userId);
      if (userOptional.isEmpty() || userOptional.get().getIsDeleted().equals(Boolean.TRUE)) {
        throw new ResourceNotFoundException("User not found",
            ServiceInfo.getId() + AuthServiceMessageCode.USER_NOT_FOUND);
      }
      Role r = roleOptional.get();
      User u = userOptional.get();
      RoleUser roleUser = new RoleUser();
      roleUser.setRole(r);
      roleUser.setUser(u);
      roleUser.setCreatorUserId(creatorId);
      roles.add(roleUser);
    }
    roleUserRepository.saveAll(roles);
  }

  @Override
  public void updateRoleUser(Integer userId, Integer updaterId, List<Integer> roleIds)
      throws ResourceNotFoundException, DuplicateEntityException {
    Optional<User> optionalUser = userRepository.findById(userId);
    if (optionalUser.isEmpty() || optionalUser.get().getIsDeleted().equals(Boolean.TRUE)) {
      throw new ResourceNotFoundException("User doesn't exist",
          ServiceInfo.getId() + AuthServiceMessageCode.USER_NOT_EXIST);
    }
    List<RoleUser> roleUserList = roleUserRepository
        .findListRoleActive(userId);
    List<Integer> idListActive = roleUserList.stream().map(RoleUser::getRoleId)
        .collect(Collectors.toList());
    logger.info("List role_user id active {}", idListActive);
    List<Integer> listIdAdd = roleIds.stream().distinct()
        .filter(id -> !idListActive.contains(id))
        .collect(Collectors.toList());
    logger.info("List role_user id add {}", listIdAdd);
    List<Integer> listIdRemove = idListActive.stream().distinct()
        .filter(id -> !roleIds.contains(id))
        .collect(Collectors.toList());
    logger.info("List role_user id remove {}", listIdRemove);
    for (Integer i : listIdRemove) {
      Optional<RoleUser> optional = roleUserRepository
          .findByUserIdAndRoleIdAndIsDeletedFalse(userId, i);
      if (optional.isPresent()) {
        RoleUser roleUser = optional.get();
        roleUser.setIsDeleted(Boolean.TRUE);
        roleUser.setDeleterUserId(updaterId);
        roleUserRepository.save(roleUser);
      }
    }
    addRoleUser(userId, updaterId, listIdAdd);
  }

  @Override
  public void deleteRoleUser(Integer userId, Integer deleterId, List<Integer> roleIds)
      throws ResourceNotFoundException {
    Optional<User> optionalUser = userRepository.findById(userId);
    if (optionalUser.isEmpty() || optionalUser.get().getIsDeleted().equals(Boolean.TRUE)) {
      throw new ResourceNotFoundException("User doesn't exist",
          ServiceInfo.getId() + AuthServiceMessageCode.USER_NOT_EXIST);
    }
    User user = optionalUser.get();
    List<RoleUser> roles = new ArrayList<>();
    for (Integer id : roleIds) {
      Optional<Role> roleOptional = roleRepository.findById(id);
      if (roleOptional.isEmpty() || roleOptional.get().getIsDeleted().equals(Boolean.TRUE)) {
        throw new ResourceNotFoundException("Role does not exist",
            ServiceInfo.getId() + AuthServiceMessageCode.ROLE_NOT_EXIST);
      }
      Optional<RoleUser> roleUserOptional = roleUserRepository
          .findByRoleIdAndUserIdAndIsDeletedIsFalse(id, userId);
      if (roleUserOptional.isEmpty()) {
        continue;
      }
      RoleUser roleUser = roleUserOptional.get();
      roleUser.setIsDeleted(Boolean.TRUE);
      roleUser.setDeleterUserId(deleterId);
      roles.add(roleUser);
    }
    roleUserRepository.saveAll(roles);
  }

  @Override
  public UserDto getUserById(String token, Integer userId)
      throws ResourceNotFoundException, IdentifyBlankException {
    if (userId == null) {
      throw new IdentifyBlankException("Id not blank " + userId,
          ServiceInfo.getId() + AuthServiceMessageCode.ID_NOT_NULL);
    }
    Optional<User> userOptional = userRepository.findById(userId);
    if (userOptional.isEmpty() || userOptional.get().getIsDeleted().equals(Boolean.TRUE)) {
      throw new ResourceNotFoundException("User not doesn't exist",
          ServiceInfo.getId() + AuthServiceMessageCode.USER_NOT_EXIST);
    }
    User user = userOptional.get();
    UserDto dto = userMapper.toDto(user);
    updateDto(token, dto);
    return dto;
  }

  @Override
  public UserDto updateUser(UserDto userDto, Integer updaterUserId)
      throws ResourceNotFoundException, IdentifyBlankException, DuplicateEntityException {
    if (userDto.getId() == null) {
      throw new IdentifyBlankException("Id Blank " + userDto.getId(),
          ServiceInfo.getId() + AuthServiceMessageCode.ID_NOT_NULL);
    }
    Optional<User> userOptionalById = userRepository
        .findById(userDto.getId());
    if (userOptionalById.isEmpty() || userOptionalById.get().getIsDeleted().equals(Boolean.TRUE)) {
      throw new ResourceNotFoundException("User not found " + userDto.getId(),
          ServiceInfo.getId() + AuthServiceMessageCode.USER_NOT_FOUND);
    }
    User marUser = userOptionalById.get();
    userMapper.updateModel(marUser, userDto);
    marUser.setUpdaterUserId(updaterUserId);
    User user = userRepository.save(marUser);
    List<Integer> roleIds = userDto.getRoles().stream()
        .map(RoleCustomDto::getId)
        .collect(Collectors.toList());
    updateRoleUser(user.getId(), updaterUserId, roleIds);
    return userMapper.toDto(user);
  }

  @Override
  public List<GroupAndUserDto> getAllGroupAndUser(String filter) {
    List<GroupAndUserDto> listGroupAndUserDto = new ArrayList<>();
    if (filter == null || filter.equals(FILTER_USER) || filter.isEmpty()) {
      // add user to map
      List<User> listUser = userRepository.findAllByStatus(UserStatus.ACTIVE);
      if (!listUser.isEmpty()) {
        for (User user : listUser) {
          if (!user.getIsDeleted()) {
            GroupAndUserDto groupAndUserDto = new GroupAndUserDto();
            groupAndUserDto.setId(user.getId());
            groupAndUserDto.setName(user.getName());
            groupAndUserDto.setUsername(user.getUsername());
            groupAndUserDto.setEmail(user.getEmail());
            groupAndUserDto.setType(TypeUserGroup.USER.toString());
            listGroupAndUserDto.add(groupAndUserDto);
          }
        }
      }
    }
    if (filter == null || filter.isBlank() || filter.equals(FILTER_GROUP)) {
      // add group to map
      List<Group> listGroup = groupRepository.findAll();
      if (!listGroup.isEmpty()) {
        for (Group group : listGroup) {
          if (!group.getIsDeleted()) {
            GroupAndUserDto groupAndUserDto = new GroupAndUserDto();
            groupAndUserDto.setId(group.getId());
            groupAndUserDto.setName(group.getName());
            groupAndUserDto.setUsername("");
            groupAndUserDto.setEmail("");
            groupAndUserDto.setType(TypeUserGroup.GROUP.toString());
            listGroupAndUserDto.add(groupAndUserDto);

          }
        }
      }
    }

    return listGroupAndUserDto;
  }

  @Override
  public List<RoleCustomDto> getAllRoleAssignedUser() {
    List<RoleUser> listRoleUser = roleUserRepository.findAllByIsDeletedFalse();
    Set<Integer> listRoleIds = listRoleUser.stream().map(RoleUser::getRoleId)
        .collect(Collectors.toSet());
    List<Role> listRole = roleRepository
        .findAllByIsDeletedFalseAndIdIn(new ArrayList<>(listRoleIds));
    return listRole.stream().map(roleMapper::toRoleCustomerDto).collect(Collectors.toList());
  }

//  @Override
//  public List<GroupUserCustomResponseDto> getAllGroupAssignedUser() {
//    List<GroupUser> listGroupUser = groupUserRepository.findAllByIsDeletedFalse();
//    List<Integer> ids = listGroupUser.stream().map(GroupUser::getGroupId).distinct()
//        .collect(Collectors.toList());
//    List<Group> listGroupDto = groupRepository.findAllByIdInAndIsDeletedFalse(ids);
//    List<GroupUserCustomResponseDto> listGroupUserResponse = new ArrayList<>();
//    for (Group group : listGroupDto) {
//      GroupUserCustomResponseDto groupUser = new GroupUserCustomResponseDto();
//      groupUser.setId(group.getId());
//      groupUser.setName(group.getName());
//      listGroupUserResponse.add(groupUser);
//    }
//    return listGroupUserResponse;
//  }

  @Override
  public DataPagingResponse<UserDto> getAll(Integer page, Integer limit, String status,
      String roles, String groups, String search, String sort) {
    Map<String, String> map = SortingUtils.detectSortType(sort);
    List<String> statusList = new ArrayList<>();
    if (status != null && !status.isEmpty()) {
      statusList = Arrays.asList(status.split(","));
    }
    List<Integer> roleList = new ArrayList<>();
    if (roles != null && !roles.isEmpty()) {
      roleList = Arrays.stream(roles.split(",")).map(Integer::parseInt).collect(
          Collectors.toList());
    }
    List<Integer> groupList = new ArrayList<>();
    if (groups != null && !groups.isEmpty()) {
      groupList = Arrays.stream(groups.split(",")).map(Integer::parseInt).collect(
          Collectors.toList());
    }
    Page<User> userPages = userRepository
        .findAll(new UserFilter().filter(statusList, roleList, groupList, search, map, false),
            PageRequest.of(page - 1, limit));
    List<User> users = userPages.getContent();
    users.forEach(this::updateModel);
    List<UserDto> userDtos = users.stream().map(userMapper::toDto).collect(Collectors.toList());
    DataPagingResponse<UserDto> dataPagingResponses = new DataPagingResponse<>();
    dataPagingResponses.setList(userDtos);
    dataPagingResponses.setTotalPage(userPages.getTotalPages());
    dataPagingResponses.setNum(userPages.getTotalElements());
    dataPagingResponses.setCurrentPage(page);
    return dataPagingResponses;
  }

  @Override
  public DataPagingResponse<UserActivityResponse> getAllUserSession(Integer page, Integer limit, String status,
      String search, String sort, String startDate, String endDate) throws ParseException {
    Map<String, String> map = SortingUtils.detectSortType(sort);
    Date startTime = (startDate == null) ? null :df.parse(startDate);
    Date endTime = (endDate == null) ? null :df.parse(endDate);
    if (endTime != null) {
      endTime = DateUtil.addDays(endTime, 1);
    }
    List<User> optional = userRepository.findByUsername(search);
    Set<Integer> ids = new HashSet<>();
    if (!optional.isEmpty()) {
      ids.add(optional.get(0).getId());
    }

    Specification<UserActivity> filter = new UserAcvitityFilter().filter(ids, search, status, map,
        startTime, endTime);

    Page<UserActivity> productPages = userActivityRepository.findAll(filter, PageRequest.of(page - 1, limit));

    List<UserActivityResponse> responseData = getResultData(productPages.getContent());
    DataPagingResponse<UserActivityResponse> dataPagingResponses = new DataPagingResponse<>();
    dataPagingResponses.setList(responseData);
    dataPagingResponses.setTotalPage(productPages.getTotalPages());
    dataPagingResponses.setNum(productPages.getTotalElements());
    dataPagingResponses.setCurrentPage(page);
    return dataPagingResponses;
  }

  private List<UserActivityResponse> getResultData(List<UserActivity> list) {
    List<UserActivityResponse> result = new ArrayList<>();
    for (UserActivity each: list) {
      UserActivityResponse one = new UserActivityResponse();
      BeanUtils.copyProperties(each, one, "createTime", "token", "id");
      Optional<User> optional = userRepository.findById(each.getUserId());
      optional.ifPresent(user -> one.setUserName(user.getUsername()));
      String date = df2.format(each.getCreateTime());
      one.setTime(date);
      result.add(one);
    }
    return result;
  }
  @Override
  @Transactional
  @Modifying
  public void updateStatusListUser(UpdateStatusUserListDto dto, Integer updaterId) {
    if (dto.getIsBlacklist() != null && dto.getIsBlacklist().equals(Boolean.TRUE)) {
      userRepository.updateStatusListUserBlacklist(dto.getStatus(), updaterId, dto.getIds());
      return;
    }
    userRepository.updateStatusListUser(dto.getStatus(), updaterId, dto.getIds());
    if (dto.getStatus().equals(UserStatus.DEACTIVE)) {
      this.forceLogout(dto.getIds());
    }
  }

  @Override
  @Transactional
  @Modifying
  public void deleteUserList(DeleteUserListDto dto, Integer deleterId)
      throws ResourceNotFoundException {
    Optional<User> optional = userRepository.findById(deleterId);
    if (optional.isEmpty() || optional.get().getIsDeleted().equals(Boolean.TRUE)) {
      throw new ResourceNotFoundException("User invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.USER_INVALID);
    }
    if (dto.getIsBlacklist() != null && dto.getIsBlacklist().equals(Boolean.TRUE)) {
      userRepository.deleteListUserBlacklist(deleterId, dto.getIds());
      return;
    }
    userRepository.deleteListUser(deleterId, dto.getIds());
    this.forceLogout(dto.getIds());
  }

  private void saveRoleUser(Set<Integer> roles, User user, Integer creatorId) {
    List<RoleUser> roleUserList = new ArrayList<>();
    for (Integer role : roles) {
      Optional<Role> roleOptional = roleRepository.findById(role);
      if (roleOptional.isEmpty() || roleOptional.get().getIsDeleted().equals(Boolean.TRUE)) {
        continue;
      }
      Optional<RoleUser> existedRoleUser = roleUserRepository
          .findByRoleIdAndUserIdAndIsDeletedIsFalse(role, user.getId());
      if (existedRoleUser.isPresent()) {
        continue;
      }
      Role r = roleOptional.get();
      RoleUser roleUser = new RoleUser();
      roleUser.setRole(r);
      roleUser.setUser(user);
      roleUser.setCreatorUserId(creatorId);
      roleUserList.add(roleUser);
    }
    roleUserRepository.saveAll(roleUserList);
  }

  public void updateModel(User user) {
    List<GroupUser> userGroups = user.getGroups();
    List<RoleUser> roleUsers = user.getRoles();

    List<GroupUser> latUserGroups = userGroups.stream()
        .filter(marGroupUser -> !marGroupUser.getIsDeleted() && !marGroupUser.getUser()
            .getIsDeleted()).collect(Collectors.toList());

    List<RoleUser> lastRoleUsers = roleUsers.stream()
        .filter(marRoleUser -> !marRoleUser.getIsDeleted() && !marRoleUser.getRole().getIsDeleted())
        .collect(Collectors.toList());
    user.setRoles(lastRoleUsers);
    user.setGroups(latUserGroups);
  }

  public void updateDto(String token, UserDto dto) {
//     update group and role
    List<GroupUser> groupUserActive = groupUserRepository
        .findByUserIdAndIsDeletedFalse(dto.getId());
    List<GroupUserCustomDto> groupUsers = groupUserActive.stream()
        .filter(gr -> gr.getUser().getIsDeleted().equals(Boolean.FALSE))
        .map(groupUserMapper::toDto).collect(Collectors.toList());
    dto.setGroups(groupUsers);

    List<RoleUser> roleActive = roleUserRepository.findListRoleActive(dto.getId());
    List<RoleCustomDto> roleUsers = roleActive.stream()
        .filter(role -> role.getRole().getIsDeleted().equals(Boolean.FALSE))
        .map(roleUserMapper::toDto).collect(Collectors.toList());

    dto.setRoles(roleUsers);
  }

  @Override
  public List<UserStatusDto> getListStatusUser() {
    List<UserStatusDto> filterList = new ArrayList<>();
    UserStatus[] statusList = UserStatus.class.getEnumConstants();
    for (UserStatus status : statusList) {
      UserStatusDto filter = new UserStatusDto();
      filter.setName(status.name());
      filter.setValue(status.getValue());
      filter.setDescription(status.getDescription());
      filterList.add(filter);
    }
    return filterList;
  }

  @Override
  public void deleteAll(Integer deleterId) throws ResourceNotFoundException {
    Optional<User> optional = userRepository.findById(deleterId);
    if (optional.isEmpty() || optional.get().getIsDeleted().equals(Boolean.TRUE)) {
      throw new ResourceNotFoundException("User invalid " + deleterId,
          ServiceInfo.getId() + AuthServiceMessageCode.USER_INVALID);
    }
    userRepository.deleteAllUser(deleterId);
    roleUserRepository.deleteAllRoleUser(deleterId);
    this.forgeLogoutAll();
  }

  @Override
  public void updateStatusAll(Integer updaterId, UserStatus status)
      throws ResourceNotFoundException {
    Optional<User> optional = userRepository.findById(updaterId);
    if (optional.isEmpty() || optional.get().getIsDeleted().equals(Boolean.TRUE)) {
      throw new ResourceNotFoundException("User invalid " + updaterId,
          ServiceInfo.getId() + AuthServiceMessageCode.USER_INVALID);
    }
    userRepository.updateStatusAll(status, updaterId);
    if (status.equals(UserStatus.DEACTIVE)) {
      this.forgeLogoutAll();
    }
  }

  // get all permission user can access
  @Override
  public Permission getPermissionsOfUser(Integer userId) throws IdentifyBlankException {

    if (userId == null) {
      throw new IdentifyBlankException("userid not null",
          ServiceInfo.getId() + AuthServiceMessageCode.ID_NOT_NULL);
    }

    Permission permission = new Permission();
    // get all role of user and group which user join
    List<RoleDto> userRoles = getRolesUser(userId);
    List<Integer> ids = userRoles.stream().map(RoleDto::getId).collect(Collectors.toList());
    List<String> generalPermissions = findListPermission(ids, new ArrayList<>());
    permission.setGeneralPermissions(generalPermissions);

    List<ObjectPermission> objectPermissions = findListObjectPermission(userId);
    permission.setSpecificPermissions(objectPermissions);

    return permission;
  }

  List<String> findListPermission(List<Integer> ids, List<String> objectCodes) {
    List<RolePermissionDto> rolePermissionDtoList = roleService
        .findListPermission(ids, objectCodes);
    List<String> permissionList = new ArrayList<>();
    rolePermissionDtoList.forEach(rolePermissionDto -> {
      List<SysPermissionDto> sysPermissionDtoList = rolePermissionDto.getSysPermissions();
      List<String> permissions = sysPermissionDtoList.stream()
          .map(SysPermissionDto::getCode).collect(Collectors.toList());
      permissionList.addAll(permissions);
    });
    return permissionList;
  }

  List<ObjectPermission> findListObjectPermission(Integer userId) {
    List<ObjectPermission> permissions = new ArrayList<>();
    List<RoleObject> roleObjectList = roleObjectRepository.findAllByUserIdAndIsDeletedFalse(userId);
    if (!roleObjectList.isEmpty()) {
      Set<String> services = roleObjectList.stream()
          .map(RoleObject::getServiceName)
          .collect(Collectors.toSet());
      for (String service : services) {
        ObjectPermission objectPermission = new ObjectPermission();
        objectPermission.setName(service);
        List<SpecificPermission> permissionList = new ArrayList<>();
        List<RoleObject> roleObjects = roleObjectList.stream()
            .filter(it -> it.getServiceName().equals(service))
            .collect(Collectors.toList());
        Set<Integer> objectIdSet = roleObjects.stream()
            .map(RoleObject::getObjectId)
            .collect(Collectors.toSet());
        for (Integer id : objectIdSet) {
          SpecificPermission specificPermission = new SpecificPermission();
          specificPermission.setId(id);
          List<Integer> roleIds = roleObjects.stream()
              .filter(it -> it.getObjectId().equals(id))
              .map(RoleObject::getRoleId)
              .collect(Collectors.toList());
          List<String> permissionCode = findListPermission(roleIds, new ArrayList<>());
          specificPermission.setPermissions(permissionCode);
          permissionList.add(specificPermission);
        }
        objectPermission.setPermissionList(permissionList);
        permissions.add(objectPermission);
      }
    }
    return permissions;
  }

  @Override
  public UserDto resetPassword(Integer userId, Integer updaterId) throws ResourceNotFoundException {
    Optional<User> userOptional = userRepository.findById(userId);
    if (userOptional.isEmpty() || userOptional.get().getIsDeleted().equals(Boolean.TRUE)) {
      throw new ResourceNotFoundException("User invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.USER_INVALID);
    }
    String passwordEncode = passwordEncoder.encode(defaultPassword);
    User user = userOptional.get();
    user.setPassword(passwordEncode);
    user.setUpdaterUserId(updaterId);
    User userRes = userRepository.save(user);
    return userMapper.toDto(userRes);
  }

  @Override
  public UserDto changePass(Integer userId, ChangePassRequestDto dto)
      throws ResourceNotFoundException, CryptoException, OperationNotImplementException {
    Optional<User> userOptional = userRepository.findById(userId);
    if (userOptional.isEmpty() || userOptional.get().getIsDeleted().equals(Boolean.TRUE)) {
      throw new ResourceNotFoundException("User invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.USER_INVALID);
    }
    User user = userOptional.get();

    if (dto.getPassword() == null || dto.getPassword().isBlank()) {
      throw new OperationNotImplementException("password not null",
          ServiceInfo.getId() + AuthServiceMessageCode.PASSWORD_NOT_NULL);
    }
    if (dto.getNewPass() == null || dto.getNewPass().isBlank()) {
      throw new OperationNotImplementException("new password not null",
          ServiceInfo.getId() + AuthServiceMessageCode.NEW_PASSWORD_INVALID);
    }
    if (dto.getNewPassConfirm() == null || dto.getNewPassConfirm().isBlank()) {
      throw new OperationNotImplementException("password confirm not null",
          ServiceInfo.getId() + AuthServiceMessageCode.NEW_PASSWORD_CONFIRM_INVALID);
    }
    String password;
    String newPass;
    String newPassConfirm;
    try {
      password = cryptoService.rsaDecrypt(dto.getPassword(), privateKey);
    } catch (Exception e) {
      e.printStackTrace();
      throw new CryptoException("decrypt pass error",
          ServiceInfo.getId() + AuthServiceMessageCode.DECRYPT_PASSWORD_ERROR);
    }
    try {
      newPass = cryptoService.rsaDecrypt(dto.getNewPass(), privateKey);
    } catch (Exception e) {
      e.printStackTrace();
      throw new CryptoException("decrypt new pass error",
          ServiceInfo.getId() + AuthServiceMessageCode.DECRYPT_PASSWORD_ERROR);
    }
    try {
      newPassConfirm = cryptoService.rsaDecrypt(dto.getNewPassConfirm(), privateKey);
    } catch (Exception e) {
      e.printStackTrace();
      throw new CryptoException("decrypt new pass confirm error",
          ServiceInfo.getId() + AuthServiceMessageCode.DECRYPT_PASSWORD_ERROR);
    }

    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new OperationNotImplementException("password incorrect",
          ServiceInfo.getId() + AuthServiceMessageCode.PASSWORD_INCORRECT);
    }
    if (newPass == null || newPass.isBlank() || password.equals(newPass)) {
      throw new OperationNotImplementException("new pass invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.NEW_PASSWORD_INVALID);
    }
    if (newPassConfirm == null || newPassConfirm.isBlank()) {
      throw new OperationNotImplementException("new pass confirm invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.NEW_PASSWORD_CONFIRM_INVALID);
    }
    if (!newPass.equals(newPassConfirm)) {
      throw new OperationNotImplementException("new pass not match",
          ServiceInfo.getId()
              + AuthServiceMessageCode.PASSWORD_CONFIRM_NOT_MATCH_PASSWORD);
    }
    user.setPassword(passwordEncoder.encode(newPass));
    User marUser = userRepository.save(user);
    return userMapper.toDto(marUser);
  }

  void forceLogout(List<Integer> userIds) {
    List<User> users = userRepository.findAllById(userIds);

    List<String> userUUidList = users.stream()
        .map(User::getUuid)
        .collect(Collectors.toList());

    List<String> uuidList = new ArrayList<>(new HashSet<>(userUUidList));
    for (String id : uuidList) {
      String keyPattern = prefix + id + ":*";
      try {
        if (redisService.hasKeyPattern(keyPattern)) {
          redisService.removePattern(keyPattern);
        }
      } catch (Exception e) {
        logger.info("error when remove key Redis, reason: {}", e.getMessage());
      }
    }
  }

  void forgeLogoutAll() {
    String keyPattern = prefix + ":*";
    try {
      if (redisService.hasKeyPattern(keyPattern)) {
        redisService.removePattern(keyPattern);
      }
    } catch (Exception e) {
      logger.info("error when remove key Redis, reason: {}", e.getMessage());
    }
  }

  public GetCustomerResponse getCustomer(Integer userId) {
    GetCustomerResponse result = new GetCustomerResponse();
    Optional<User> userOptional = userRepository.findById(userId);
    if (userOptional.isPresent()) {
      User user = userOptional.get();
      result.setId(userId);
      result.setUsername(user.getUsername());
      result.setEmail(user.getEmail());
      result.setName(user.getName());
      Optional<CustomerBalance> customerOptional = customerBalanceRepository.findByCustomerId(userId);
      if (customerOptional.isPresent()) {
        CustomerBalance customerBalance = customerOptional.get();
        result.setBalance(customerBalance.getBalance());
      } else
        result.setBalance(0);
    }
    return result;
  }

  public int addCustomer(RegisterCustomerRequest request) throws CryptoException {
    User u = new User();
    u.setEmail(request.getEmail());
    u.setPassword(passwordEncoder.encode(request.getPassword()));
    u.setName(request.getName());
    u.setUsername(request.getUsername());
    u.setIsUserInternal(false);
    User user = userRepository.save(u);
    return user.getId();
  }


  public int updateCustomer(UpdateCustomerRequest request, int id) {
    Optional<User> userOptional = userRepository.findById(id);
    if (userOptional.isPresent()) {
      User u = userOptional.get();
      u.setEmail(StringUtils.isEmpty(request.getEmail()) ? u.getEmail() : request.getEmail());
      u.setPassword(StringUtils.isEmpty(request.getPassword()) ? u.getPassword() : passwordEncoder.encode(request.getPassword()));
      u.setName(StringUtils.isEmpty(request.getName()) ? u.getName() : request.getName());
      u.setUsername(StringUtils.isEmpty(request.getUsername()) ? u.getUsername() : request.getUsername());
      u.setIsUserInternal(false);
      if (request.getBalance() != 0l) {
        Optional<CustomerBalance> customerBalanceOptional = customerBalanceRepository.findByCustomerId(id);
        if (customerBalanceOptional.isPresent()) {
          CustomerBalance customerBalance = customerBalanceOptional.get();
          customerBalance.setBalance(request.getBalance());
          customerBalanceRepository.save(customerBalance);
        } else {
          CustomerBalance customerBalance = new CustomerBalance();
          customerBalance.setCustomerId(id);
          customerBalance.setBalance(request.getBalance());
          customerBalanceRepository.save(customerBalance);
        }
      }
      User user = userRepository.save(u);
      return user.getId();
    }
    return 0;
  }

  public DataPagingResponse<GetCustomerResponse> getCustomerList(Integer page, Integer limit, String search) {
    Page<User> userPages = userRepository
        .findAll(new UserFilter().getCustomerFilter(search), PageRequest.of(page - 1, limit));
    List<User> users = userPages.getContent();
    List<GetCustomerResponse> responseDataList = new ArrayList<>();
    for (User user : users) {
      if (user != null) {
        GetCustomerResponse each = getCustomer(user.getId());
        responseDataList.add(each);
      }
    }
    DataPagingResponse<GetCustomerResponse> dataPagingResponses = new DataPagingResponse<>();
    dataPagingResponses.setList(responseDataList);
    dataPagingResponses.setTotalPage(userPages.getTotalPages());
    dataPagingResponses.setNum(userPages.getTotalElements());
    dataPagingResponses.setCurrentPage(page);
    return dataPagingResponses;
  }

}
